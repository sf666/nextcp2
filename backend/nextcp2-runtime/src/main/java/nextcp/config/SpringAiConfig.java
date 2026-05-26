package nextcp.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import com.google.genai.Client;
import nextcp.dto.Config;

@Configuration
public class SpringAiConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringAiConfig.class.getName());
	
    @Bean
    @DependsOn("rendererConfigProducer")
    public GoogleGenAiChatModel googleGenAiChatModel(Config config) {
    	try {
            if (config != null && config.aiConfig != null && "google".equalsIgnoreCase(config.aiConfig.aiProvider)) {
                Client genAiClient = Client.builder()
                    .apiKey(config.aiConfig.aiApiKey)
                    .build();

                GoogleGenAiChatOptions defaultOptions = GoogleGenAiChatOptions.builder()
                        .model(config.aiConfig.aiModel)
                        .build();

                return GoogleGenAiChatModel.builder()
                        .genAiClient(genAiClient)
                        .defaultOptions(defaultOptions)
                        .build();
            }
		} catch (Exception e) {
			log.error("Error initializing GoogleGenAiChatModel: {}", e.getMessage(), e);
		}
        return null;
    }
}