package example.tx.client.config;

import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
    /**
     * 日志配置
     *
     * @return 日志级别
     * NONE:不输出日志
     * BASIC:只输出请求方法的URL和响应的状态码以及接口执行的时间
     * HEADERS:将BASIC信息和请求头信息输出
     * FULL:输出完整的请求信息
     * @see Logger.Level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 超时配置
     * 配置连接超时时间和读取超时时间
     *
     * @return 超时请求配置
     */
    @Bean
    public Request.Options options() {
        //参数1：连接超时时间(ms),默认是10*1000
        //参数2：读取超时时间(ms),默认是60*1000
        return new Request.Options(5000, 10000);
    }
}
