package nextcp.eventBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import nextcp.ai.ChatHistoryChanged;

/**
 * Bridges {@link ChatHistoryChanged} domain events to the SSE channel so that
 * AI chat clients receive history updates by push instead of polling
 * {@code /api/ai/history}.
 */
@Controller
public class ChatHistorySseEvents
{
    public static final String CHAT_HISTORY_CHANGED = "CHAT_HISTORY_CHANGED";

    @Autowired
    private SsePublisher ssePublisher = null;

    @EventListener
    public void chatHistoryChanged(ChatHistoryChanged event)
    {
        ssePublisher.sendObjectAsJson(CHAT_HISTORY_CHANGED, event.history);
    }
}
