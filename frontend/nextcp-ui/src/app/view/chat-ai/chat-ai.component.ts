import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatAiResponse, ChatAiService } from 'src/app/service/chat-ai.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { ChatMessageDto } from 'src/app/service/dto.d';

type MessageStatus = 'COMPLETE' | 'PENDING' | 'ERROR';

interface ChatMessage {
  id?: number;
  role: 'user' | 'assistant';
  content: string;
  status: MessageStatus;
}

// Marker entry shown when the history is empty after a fresh boot. It is
// purely client-side and is never sent to / received from the backend.
const WELCOME_MESSAGE: ChatMessage = {
  role: 'assistant',
  content: 'Hello! I am your AI assistant. Feel free to ask me anything.',
  status: 'COMPLETE',
};

// Interval used when polling the history endpoint for a PENDING response.
const HISTORY_POLL_INTERVAL_MS = 1500;

@Component({
  selector: 'app-chat-ai',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-ai.component.html',
  styleUrl: './chat-ai.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChatAiComponent implements OnInit, OnDestroy {

  userInput = '';
  isLoading = signal(false);
  messages = signal<ChatMessage[]>([WELCOME_MESSAGE]);

  // friendlyName of the Media Renderer / Media Server currently selected by the LLM.
  selectedRendererName = signal<string | null>(null);
  selectedServerName = signal<string | null>(null);

  private activeRequest?: Subscription;
  private selectionRequest?: Subscription;
  private historyRequest?: Subscription;
  private pollTimer?: ReturnType<typeof setTimeout>;

  constructor(private chatAiService: ChatAiService) {}

  ngOnInit(): void {
    this.refreshSelectedDevices();
    // Restore previous conversation on page load. Also kicks off polling if a
    // pending response is still being computed by the backend.
    this.loadHistory();
  }

  ngOnDestroy(): void {
    this.activeRequest?.unsubscribe();
    this.selectionRequest?.unsubscribe();
    this.historyRequest?.unsubscribe();
    this.clearPollTimer();
  }

  /**
   * Loads the currently selected Media Renderer/Server from the backend so the
   * UI reflects the LLM-driven selection. Called on init and after every chat
   * exchange because the LLM may switch devices via MCP tools.
   */
  private refreshSelectedDevices(): void {
    this.selectionRequest?.unsubscribe();
    this.selectionRequest = this.chatAiService.getSelectedDevices().subscribe({
      next: (dto) => {
        this.selectedRendererName.set(dto?.mediaRenderer?.friendlyName ?? null);
        this.selectedServerName.set(dto?.mediaServer?.friendlyName ?? null);
      },
      error: (err: unknown) => {
        console.warn('[chat-ai] could not load selected devices', err);
      },
    });
  }

  /**
   * Loads the chat history from the backend and applies it to the messages
   * signal. If any message is still PENDING, starts polling until it
   * transitions to COMPLETE or ERROR.
   */
  private loadHistory(): void {
    this.historyRequest?.unsubscribe();
    this.historyRequest = this.chatAiService.getHistory().subscribe({
      next: (dto) => {
        const items = (dto?.messages ?? []).map((m) => this.mapDto(m));
        if (items.length === 0) {
          this.messages.set([WELCOME_MESSAGE]);
        } else {
          this.messages.set(items);
        }
        const hasPending = items.some((m) => m.status === 'PENDING');
        this.isLoading.set(hasPending);
        if (hasPending) {
          this.schedulePoll();
        } else {
          this.clearPollTimer();
        }
      },
      error: (err: unknown) => {
        console.warn('[chat-ai] could not load chat history', err);
      },
    });
  }

  private schedulePoll(): void {
    this.clearPollTimer();
    this.pollTimer = setTimeout(() => {
      this.pollTimer = undefined;
      this.loadHistory();
    }, HISTORY_POLL_INTERVAL_MS);
  }

  private clearPollTimer(): void {
    if (this.pollTimer !== undefined) {
      clearTimeout(this.pollTimer);
      this.pollTimer = undefined;
    }
  }

  private mapDto(dto: ChatMessageDto): ChatMessage {
    const role = dto.role === 'USER' ? 'user' : 'assistant';
    const status: MessageStatus = dto.status === 'PENDING' || dto.status === 'ERROR' ? dto.status : 'COMPLETE';
    return {
      id: dto.id,
      role,
      content: dto.content ?? '',
      status,
    };
  }

  send(): void {
    const message = this.userInput.trim();
    if (!message || this.isLoading()) {
      return;
    }

    this.activeRequest?.unsubscribe();

    // Optimistic UI update: drop the welcome placeholder once the user
    // engages, append the user message and a pending assistant entry. The
    // backend will create matching history entries; on completion we
    // reconcile against the server state via loadHistory().
    this.messages.update((items) => {
      const withoutWelcome = items.length === 1 && items[0] === WELCOME_MESSAGE ? [] : items;
      return [
        ...withoutWelcome,
        { role: 'user', content: message, status: 'COMPLETE' },
        { role: 'assistant', content: '', status: 'PENDING' },
      ];
    });
    this.userInput = '';
    this.isLoading.set(true);
    console.debug('[chat-ai] sending request', { endpoint: '/api/ai/doAction', message });

    this.activeRequest = this.chatAiService.sendMessage(message).subscribe({
      next: (data) => {
        console.debug('[chat-ai] response received', data);
        const text = this.extractResponse(data);
        if (!text) {
          return;
        }
        this.appendToLatestAssistantMessage(text);
      },
      error: (err: unknown) => {
        const details = this.extractErrorDetails(err);
        console.error('[chat-ai] request failed', details.rawError);
        // The backend keeps the answer in history even when the SSE channel
        // breaks. Pull the authoritative state instead of synthesising one.
        this.loadHistory();
      },
      complete: () => {
        // Reconcile with the backend's authoritative history; this also
        // clears the loading flag and stops polling when no more PENDING
        // entries remain.
        this.loadHistory();
        // The LLM may have called select_renderer / select_server during the
        // exchange — refresh the displayed selection.
        this.refreshSelectedDevices();
      },
    });
  }

  private appendToLatestAssistantMessage(chunk: string): void {
    this.messages.update((items) => {
      const next = [...items];
      const idx = this.findLatestAssistantMessageIndex(next);

      if (idx < 0) {
        next.push({ role: 'assistant', content: chunk, status: 'COMPLETE' });
        return next;
      }

      const current = next[idx];
      next[idx] = { ...current, content: `${current.content}${chunk}`, status: 'COMPLETE' };
      return next;
    });
  }

  private findLatestAssistantMessageIndex(messages: ChatMessage[]): number {
    for (let idx = messages.length - 1; idx >= 0; idx -= 1) {
      if (messages[idx].role === 'assistant') {
        return idx;
      }
    }

    return -1;
  }

  private extractResponse(data: ChatAiResponse | string): string {
    if (typeof data === 'string') {
      return data;
    }
    return data?.response ?? '';
  }

  private extractErrorDetails(err: unknown): { statusText: string; rawError: unknown } {
    if (err instanceof HttpErrorResponse) {
      const backendMessage = this.extractBackendMessage(err.error);
      const statusText = `${err.status} ${err.statusText}${backendMessage ? ` - ${backendMessage}` : ''}`;
      return { statusText, rawError: err };
    }

    return {
      statusText: 'unknown error',
      rawError: err,
    };
  }

  private extractBackendMessage(errorPayload: unknown): string {
    if (!errorPayload) {
      return '';
    }

    if (typeof errorPayload === 'string') {
      return errorPayload;
    }

    if (typeof errorPayload === 'object') {
      const payload = errorPayload as Record<string, unknown>;
      const message = payload.message;
      const error = payload.error;
      const path = payload.path;
      const parts = [message, error, path].filter((p) => typeof p === 'string' && p.length > 0) as string[];
      return parts.join(' | ');
    }

    return '';
  }
}
