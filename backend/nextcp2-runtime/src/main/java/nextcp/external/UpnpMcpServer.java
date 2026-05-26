package nextcp.external;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Service;

@Service
public class UpnpMcpServer {

    private static final Logger log = LoggerFactory.getLogger(UpnpMcpServer.class);

    // Diese Methode wird über MCP für das LLM bereitgestellt
    @McpTool(description = "Spielt einen bestimmten Radiosender ab, z.B. 'FFN'.")
    public String spieleRadiosender(String senderName) {
        log.info("UPnP Befehl empfangen: Spiele Sender {}", senderName);
        
        // HIER kommt deine existierende UPnP Control Point Logik hin!
        // z.B. upnpService.getControlPoint().execute(new PlayAction(senderUrl));
        
        if ("ffn".equalsIgnoreCase(senderName)) {
            return "UPnP-Erfolg: Der Radiosender ffn wurde auf den UPnP-Lautsprechern gestartet.";
        }
        
        return "UPnP-Erfolg: Versuche den Sender " + senderName + " abzuspielen.";
    }

    @McpTool(description = "Stoppt die aktuelle Musik- oder Radiowiedergabe auf den UPnP-Geräten.")
    public String stoppeWiedergabe() {
        log.info("UPnP Befehl empfangen: Stoppe Wiedergabe");
        // Deine UPnP Stopp-Logik
        return "UPnP-Erfolg: Die Wiedergabe wurde gestoppt.";
    }
}