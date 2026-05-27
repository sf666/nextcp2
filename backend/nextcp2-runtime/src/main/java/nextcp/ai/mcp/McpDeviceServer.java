package nextcp.ai.mcp;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class McpDeviceServer {

    private static final Logger log = LoggerFactory.getLogger(McpDeviceServer.class);

    @Autowired
    private MessageSource messageSource;

    // Diese Methode wird über MCP für das LLM bereitgestellt
    @McpTool(description = "Liefert eine Liste der verfügbaren Media Renderer UPnP-Geräte (auch Player genannt).")
    public String listeMediaRenderer() {
        log.info("UPnP command received: Liste Media Renderer");

        // TODO: Echte Geräteliste aus dem UPnP-Service laden
        List<String> devices = List.of("Wohnzimmer Lautsprecher", "Küche Radio", "Schlafzimmer Soundbar");

        StringBuilder sb = new StringBuilder();
        sb.append(messageSource.getMessage("mcp.device.list.header", null, Locale.getDefault()));
        sb.append("\n");
        for (int i = 0; i < devices.size(); i++) {
            sb.append(messageSource.getMessage("mcp.device.renderer.list.item.response", new Object[]{i + 1, devices.get(i)}, Locale.getDefault()));
            sb.append("\n");
        }
        return sb.toString();
    }

    @McpTool(description = "Wählt einen Media Renderer aus, für die interaktion mit dem LLM.")
    public String selectMediaRenderer(String mediaRendererName) {
        log.info("UPnP command received: Select Media Renderer - {}", mediaRendererName);
        return messageSource.getMessage("mcp.device.renderer.select.response", new Object[]{mediaRendererName}, Locale.getDefault());
    }
}