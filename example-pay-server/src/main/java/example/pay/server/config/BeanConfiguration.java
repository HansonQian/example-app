package example.pay.server.config;

import example.exeption.OverseasExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    // 异常处理
    @Bean
    public OverseasExceptionHandler overseasExceptionHandler() {
        return new OverseasExceptionHandler();
    }
}
