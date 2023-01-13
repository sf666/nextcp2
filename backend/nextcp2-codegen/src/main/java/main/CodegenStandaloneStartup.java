package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import nextcp.dto.ApplicationConfig;
import nextcp.dto.Config;

@SpringBootApplication(scanBasePackages = "nextcp, codegen, main")
public class CodegenStandaloneStartup
{

    public CodegenStandaloneStartup()
    {
    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(CodegenStandaloneStartup.class, args);
    }
    
    @Bean
    public Config createCodegenConfigElements()
    {
        Config c = new Config();
        c.applicationConfig = new ApplicationConfig();
        c.applicationConfig.generateUpnpCode = true;
        c.applicationConfig.generateUpnpCodePath = "/Volumes/Data/svn/nextcp2/backend/nextcp2-modelgen/src/main/java";
        return c;
    }
    
}
