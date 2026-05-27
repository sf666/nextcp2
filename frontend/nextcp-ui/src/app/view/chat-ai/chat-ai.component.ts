import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChatAiResponse, ChatAiService } from 'src/app/service/chat-ai.service';
import { HttpErrorResponse } from '@angular/common/http';

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
export class ChatAiComponent {

  userInput = '';
  isLoading = signal(false);
  messages = signal<ChatMessage[]>([
    { role: 'assistant', content: 'Hallo! Ich bin dein AI Assistent. Stelle mir gerne eine Frage.' },
  ]);

  constructor(private chatAiService: ChatAiService) {}

  send(): void {
    const message = this.userInput.trim();
    if (!message || this.isLoading()) {
      return;
    }

    this.messages.update((items) => [...items, { role: 'user', content: message }]);
    this.userInput = '';
    this.isLoading.set(true);
    console.debug('[chat-ai] sending request', { endpoint: '/api/ai/doAction', message });

    this.chatAiService.sendMessage(message).subscribe({
      next: (data) => {
        console.debug('[chat-ai] response received', data);
        const text = this.extractResponse(data);
        this.messages.update((items) => [...items, { role: 'assistant', content: text }]);
      },
      error: (err: unknown) => {
        const details = this.extractErrorDetails(err);
        console.error('[chat-ai] request failed', details.rawError);
        this.messages.update((items) => [
          ...items,
          {
            role: 'assistant',
            content: `Die Anfrage konnte nicht verarbeitet werden (${details.statusText}).`,
          },
        ]);
      },
      complete: () => this.isLoading.set(false),
    });
  }

  private extractResponse(data: ChatAiResponse | string): string {
    if (typeof data === 'string') {
      return data;
    }
    return data?.response ?? 'Keine Antwort vom Server erhalten.';
  }

  private extractErrorDetails(err: unknown): { statusText: string; rawError: unknown } {
    if (err instanceof HttpErrorResponse) {
      const backendMessage = this.extractBackendMessage(err.error);
      const statusText = `${err.status} ${err.statusText}${backendMessage ? ` - ${backendMessage}` : ''}`;
      return { statusText, rawError: err };
    }

    return {
      statusText: 'unbekannter Fehler',
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
