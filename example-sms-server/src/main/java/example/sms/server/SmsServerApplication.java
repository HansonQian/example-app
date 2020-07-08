package example.sms.server;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableSwagger2Doc
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EntityScan(basePackages = {"example.model.sms"})
@EnableFeignClients(basePackages = "example.tx.client.feign")
public class SmsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmsServerApplication.class);
    }
}
