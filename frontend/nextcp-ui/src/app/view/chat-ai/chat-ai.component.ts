import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatAiService } from 'src/app/service/chat-ai.service';
import { SseService } from 'src/app/service/sse/sse.service';
import { Subscription } from 'rxjs';
import { ChatHistoryDto, ChatMessageDto } from 'src/app/service/dto.d';

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
  private historyPushSub?: Subscription;

  constructor(private chatAiService: ChatAiService, private sseService: SseService) {}

  ngOnInit(): void {
    this.refreshSelectedDevices();
    // Restore the previous conversation once on page load. All later changes
    // (pending placeholder, completed answer, errors) arrive by SSE push.
    this.loadInitialHistory();
    this.historyPushSub = this.sseService.chatHistoryChanged$.subscribe({
      next: (dto) => this.applyHistory(dto),
    });
  }

  ngOnDestroy(): void {
    this.activeRequest?.unsubscribe();
    this.selectionRequest?.unsubscribe();
    this.historyRequest?.unsubscribe();
    this.historyPushSub?.unsubscribe();
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
   * One-off fetch of the chat history used to restore the conversation on page
   * load. Ongoing updates are delivered via the CHAT_HISTORY_CHANGED SSE push.
   */
  private loadInitialHistory(): void {
    this.historyRequest?.unsubscribe();
    this.historyRequest = this.chatAiService.getHistory().subscribe({
      next: (dto) => this.applyHistory(dto),
      error: (err: unknown) => {
        console.warn('[chat-ai] could not load chat history', err);
      },
    });
  }

  /**
   * Applies an authoritative history snapshot (from the initial fetch or an SSE
   * push) to the UI: rebuilds the message list and reflects whether a response
   * is still pending.
   */
  private applyHistory(dto: ChatHistoryDto | null): void {
    const items = (dto?.messages ?? []).map((m) => this.mapDto(m));
    if (items.length === 0) {
      this.messages.set([WELCOME_MESSAGE]);
    } else {
      this.messages.set(items);
    }
    this.isLoading.set(items.some((m) => m.status === 'PENDING'));
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

    // Optimistic UI update for instant feedback: drop the welcome placeholder,
    // append the user message and a pending assistant entry. The backend
    // immediately pushes a matching CHAT_HISTORY_CHANGED snapshot which becomes
    // the authoritative state (and again once the answer or an error arrives).
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

    // POST /doAction only triggers the backend exchange; the response content
    // and status are delivered through the history push, so we do not consume
    // the streamed body here.
    this.activeRequest = this.chatAiService.sendMessage(message).subscribe({
      next: () => { /* content is applied via chatHistoryChanged$ push */ },
      error: (err: unknown) => {
        // The backend records the failure in the history and pushes it; this is
        // only a network/transport problem with the trigger call itself.
        console.warn('[chat-ai] doAction request failed', err);
        this.refreshSelectedDevices();
      },
      complete: () => {
        // The LLM may have called select_renderer / select_server during the
        // exchange — refresh the displayed selection.
        this.refreshSelectedDevices();
      },
    });
  }
}
