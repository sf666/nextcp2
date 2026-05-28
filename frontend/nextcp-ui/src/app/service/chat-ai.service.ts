import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ChatAiRequest {
  message: string;
}

export interface ChatAiResponse {
  response: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatAiService {

  private readonly baseUri = '/api/ai';
  private readonly actionUri = '/doAction';
  private readonly headers: HeadersInit = {
    'Content-Type': 'application/json',
    'Accept': 'text/event-stream',
    'Application': 'nextcp2',
  };

  constructor() {}

  public sendMessage(message: string): Observable<ChatAiResponse | string> {
    const req: ChatAiRequest = { message };
    const endpoint = `${this.baseUri}${this.actionUri}`;

    return new Observable<ChatAiResponse | string>((observer) => {
      const abortController = new AbortController();

      fetch(endpoint, {
        method: 'POST',
        headers: this.headers,
        body: JSON.stringify(req),
        signal: abortController.signal,
      })
        .then(async (response) => {
          if (!response.ok) {
            throw new Error(`${response.status} ${response.statusText}`.trim());
          }

          if (!response.body) {
            throw new Error('Leere Antwort vom Server.');
          }

          const reader = response.body.getReader();
          const decoder = new TextDecoder('utf-8');
          let buffer = '';

          while (true) {
            const { value, done } = await reader.read();
            if (done) {
              buffer += decoder.decode();
              this.emitBufferedEvents(buffer, observer);
              observer.complete();
              return;
            }

            buffer += decoder.decode(value, { stream: true });
            buffer = this.emitBufferedEvents(buffer, observer);
          }
        })
        .catch((err: unknown) => {
          if (abortController.signal.aborted) {
            return;
          }
          observer.error(err);
        });

      return () => abortController.abort();
    });
  }

  private emitBufferedEvents(buffer: string, observer: { next: (value: ChatAiResponse | string) => void; complete?: () => void }): string {
    let workingBuffer = buffer;
    const delimiter = '\n\n';

    while (workingBuffer.includes(delimiter)) {
      const delimiterIndex = workingBuffer.indexOf(delimiter);
      const rawEvent = workingBuffer.slice(0, delimiterIndex);
      workingBuffer = workingBuffer.slice(delimiterIndex + delimiter.length);

      const payload = this.extractPayload(rawEvent);
      if (!payload) {
        continue;
      }

      if (payload === '[DONE]') {
        observer.complete?.();
        return '';
      }

      observer.next(this.parsePayload(payload));
    }

    return workingBuffer;
  }

  private extractPayload(rawEvent: string): string {
    const dataLines = rawEvent
      .replace(/\r/g, '')
      .split('\n')
      .filter((line) => line.startsWith('data:'))
      .map((line) => line.slice(5).trimStart());

    return dataLines.join('\n').trim();
  }

  private parsePayload(payload: string): ChatAiResponse | string {
    try {
      const parsed = JSON.parse(payload) as ChatAiResponse;
      if (typeof parsed?.response === 'string') {
        return parsed;
      }
    } catch (_ignored) {
      // Non-JSON chunks are forwarded as plain text.
    }

    return payload;
  }
}
