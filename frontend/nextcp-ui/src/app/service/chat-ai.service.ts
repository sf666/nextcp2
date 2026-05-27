import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { HttpService } from './http.service';

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

  constructor(private httpService: HttpService) {}

  public sendMessage(message: string): Subject<ChatAiResponse | string> {
    const req: ChatAiRequest = { message };
    return this.httpService.post<ChatAiResponse | string>(this.baseUri, this.actionUri, req, 'AI Chat communication error');
  }
}
