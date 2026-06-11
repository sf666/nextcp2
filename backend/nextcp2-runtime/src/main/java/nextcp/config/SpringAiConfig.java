package nextcp.config;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import nextcp.ai.mcp.McpDevices;
import nextcp.ai.mcp.McpLocale;

@Configuration
public class SpringAiConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringAiConfig.class.getName());

	/**
	 * Registers the MCP Tools (UPnP device / language control) that are exposed to
	 * the LLM. The actual {@link org.springframework.ai.chat.client.ChatClient} is
	 * built on demand by {@link ChatClientProvider} so that AI provider changes
	 * take effect without an application restart.
	 */
	@Bean
	public ToolCallbackProvider upnpControlPointTools(McpDevices mcpDevices, McpLocale mcpLocale) {
		ToolCallbackProvider provider = MethodToolCallbackProvider.builder().toolObjects(mcpDevices, mcpLocale).build();

		log.info("=== Registered MCP Tools ===");
		Arrays.asList(provider.getToolCallbacks()).forEach(tool -> log.info("  Tool registered: name={}", tool.getToolDefinition().name()));

		return provider;
	}
}
