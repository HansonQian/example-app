package example.tx.task;

import example.tx.task.service.ProcessTXMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "example.tx.client.feign")
public class TXTaskApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context
                = SpringApplication.run(TXTaskApplication.class);
        try {
            ProcessTXMessageService processTXMessageService
                    = context.getBean(ProcessTXMessageService.class);
            processTXMessageService.start();
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            log.error("项目启动异常", e);
        }
    }
}
