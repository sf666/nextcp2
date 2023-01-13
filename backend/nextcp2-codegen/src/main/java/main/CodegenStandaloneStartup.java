package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import codegen.ICodegenConfig;

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
    public ICodegenConfig createCodegenConfigElements()
    {
        return new ICodegenConfig()
        {
            
            @Override
            public boolean isGenerateUpnpCode()
            {
                return true;
            }
            
            @Override
            public String getGenerateUpnpCodePath()
            {
                return "/Volumes/Data/svn/nextcp2/backend/nextcp2-modelgen/src/main/java";
            }
        };
    }
    
}
