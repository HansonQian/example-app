package example.gateway.config;

import example.exeption.OverseasExceptionHandler;
import example.gateway.fallback.ZuulGatewayFallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanConfiguration {

    // 异常处理
    @Bean
    public OverseasExceptionHandler overseasExceptionHandler() {
        return new OverseasExceptionHandler();
    }

    // 网关回退
    @Bean
    public FallbackProvider zuulGatewayFallback() {
        return new ZuulGatewayFallback();
    }


}
