package example.tx.client.config;

import example.tx.client.feign.fallback.TXMessageFeignClientFallbackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public TXMessageFeignClientFallbackFactory txMessageFeignClientFallbackFactory(){
        return new TXMessageFeignClientFallbackFactory();
    }


}
