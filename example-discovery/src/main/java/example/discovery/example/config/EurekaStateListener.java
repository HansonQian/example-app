package example.discovery.example.config;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class EurekaStateListener {
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("服务 [{}]  已下线", event.getAppName());
        log.info("server地址信息 [{}]  ", event.getServerId());
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info("服务 [{}]  进行注册", instanceInfo.getInstanceId());
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("服务 [{}]  进行续约", event.getInstanceInfo().getInstanceId());
    }

    public void listen(EurekaRegistryAvailableEvent event) {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("注册中心启动,当前时间:{}", currentTime);
    }

    public void listen(EurekaServerStartedEvent event) {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("注册中心服务端启动,当前时间:{}", currentTime);
    }
}
