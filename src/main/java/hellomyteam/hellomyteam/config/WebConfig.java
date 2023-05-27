package hellomyteam.hellomyteam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://hellomyteam-front-o8od-1djv80i8p-chlcken2.vercel.app",
                        "https://hellomyteam.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}


