package example.sms.server.config;

import com.rabbitmq.client.Channel;
import example.constant.OverseasConstant;
import example.constant.ServiceInstanceList;
import example.exeption.OverseasException;
import example.model.sms.SysMessage;
import example.model.tx.SysTransactionMessage;
import example.result.BaseResult;
import example.sms.server.service.SysMessageService;
import example.tx.client.feign.TXMessageFeignClient;
import example.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class RabbitMqConsumer {

    private final TXMessageFeignClient messageFeignClient;

    private final SysMessageService messageService;

    public RabbitMqConsumer(
            TXMessageFeignClient messageFeignClient,
            SysMessageService messageService) {
        this.messageFeignClient = messageFeignClient;
        this.messageService = messageService;
    }

    @Bean
    public Queue logOpQueue() {
        return new Queue(OverseasConstant.MQ_SMS_TOPIC);
    }

    @RabbitListener(queues = OverseasConstant.MQ_SMS_TOPIC)
    @RabbitHandler
    public void onMessage(Message message, Channel channel) {
        Jackson2JsonMessageConverter
                jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        Object object = jackson2JsonMessageConverter.fromMessage(message);
        byte[] body = message.getBody();
        try {
            channel.basicQos(1);
            String content = new String(body);
            log.info("消费端Payload:{}", content);
            SysTransactionMessage txMessage = JsonUtil.parse(content, SysTransactionMessage.class);
            if (null == txMessage) {
                return;
            }
            String sms = txMessage.getMessage();
            SysMessage sysMessage = JsonUtil.parse(sms, SysMessage.class);
            if (null != sysMessage) {
                sysMessage.setMessageId(System.currentTimeMillis());
                sysMessage.setSendTime(new Date());
                this.messageService.saveMessage(sysMessage);

                Long messageId = txMessage.getMessageId();
                BaseResult baseResult = messageFeignClient.confirmConsumeMessage(ServiceInstanceList.EXAMPLE_SMS_SERVER, messageId);
                boolean flag = baseResult.isFlag();
                if (flag) {
                    //手工ACK
                    long deliveryTag = message.getMessageProperties().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                }
            }
        } catch (IOException e) {
            throw new OverseasException("回滚事务");
        }

    }
}
