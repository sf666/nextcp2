import { ChangeDetectionStrategy, Component, OnDestroy, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatAiResponse, ChatAiService } from 'src/app/service/chat-ai.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
}

@Component({
  selector: 'app-chat-ai',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-ai.component.html',
  styleUrl: './chat-ai.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ChatAiComponent implements OnDestroy {

  userInput = '';
  isLoading = signal(false);
  messages = signal<ChatMessage[]>([
    { role: 'assistant', content: 'Hello! I am your AI assistant. Feel free to ask me anything.' },
  ]);
  private activeRequest?: Subscription;

  constructor(private chatAiService: ChatAiService) {}

  ngOnDestroy(): void {
    this.activeRequest?.unsubscribe();
  }

  send(): void {
    const message = this.userInput.trim();
    if (!message || this.isLoading()) {
      return;
    }

    this.activeRequest?.unsubscribe();

    this.messages.update((items) => [
      ...items,
      { role: 'user', content: message },
      { role: 'assistant', content: '' },
    ]);
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
        this.setLatestAssistantMessage(`The request could not be processed (${details.statusText}).`);
        this.isLoading.set(false);
      },
      complete: () => {
        this.ensureAssistantResponsePresent();
        this.isLoading.set(false);
      },
    });
  }

  private appendToLatestAssistantMessage(chunk: string): void {
    this.messages.update((items) => {
      const next = [...items];
      const idx = this.findLatestAssistantMessageIndex(next);

      if (idx < 0) {
        next.push({ role: 'assistant', content: chunk });
        return next;
      }

      const current = next[idx];
      next[idx] = { ...current, content: `${current.content}${chunk}` };
      return next;
    });
  }

  private setLatestAssistantMessage(content: string): void {
    this.messages.update((items) => {
      const next = [...items];
      const idx = this.findLatestAssistantMessageIndex(next);

      if (idx < 0) {
        next.push({ role: 'assistant', content });
        return next;
      }

      next[idx] = { ...next[idx], content };
      return next;
    });
  }

  private ensureAssistantResponsePresent(): void {
    this.messages.update((items) => {
      const next = [...items];
      const idx = this.findLatestAssistantMessageIndex(next);

      if (idx < 0) {
        next.push({ role: 'assistant', content: 'No response received from the server.' });
        return next;
      }

      if (!next[idx].content.trim()) {
        next[idx] = { ...next[idx], content: 'No response received from the server.' };
      }

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
    return data?.response ?? 'No response received from the server.';
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
