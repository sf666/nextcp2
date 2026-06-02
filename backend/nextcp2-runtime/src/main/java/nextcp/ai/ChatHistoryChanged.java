package nextcp.ai;

import nextcp.dto.ChatHistoryDto;

/**
 * Domain event published by {@link ChatHistoryService} whenever the in-memory
 * chat history changes (a new exchange was started, completed or failed).
 *
 * <p>The event carries an immutable snapshot of the history at the time of the
 * change so that listeners (e.g. the SSE bridge) can push it to clients without
 * having to query the service again. This replaces the former client-side
 * polling of {@code /api/ai/history}.
 */
public class ChatHistoryChanged
{
    public final ChatHistoryDto history;

    public ChatHistoryChanged(ChatHistoryDto history)
    {
        this.history = history;
    }
}
