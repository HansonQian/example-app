package example.tx.client.feign;

import example.constant.ServiceInstanceList;
import example.model.tx.SysTransactionMessage;
import example.result.BaseResult;
import example.result.PageResult;
import example.tx.client.feign.fallback.TXMessageFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = ServiceInstanceList.FRAMEWORK_TX_SERVER,
        path = "/transaction",
        fallbackFactory = TXMessageFeignClientFallbackFactory.class)
public interface TXMessageFeignClient {

    /**
     * 发送消息
     *
     * @param message 事务消息
     * @return BaseResult
     */
    @PostMapping("/send")
    BaseResult saveSingleMessage(@RequestBody SysTransactionMessage message);


    /**
     * 发送消息(Multi)
     *
     * @param messages 事务消息集合
     * @return BaseResult
     */
    @PostMapping("/sends")
    BaseResult saveMultiMessage(@RequestBody List<SysTransactionMessage> messages);

    /**
     * 确认消息被消费
     *
     * @param consumeSystem 消费系统
     * @param messageId     事务消息标识
     * @return BaseResult
     */
    @PostMapping("/confirm/")
    BaseResult confirmConsumeMessage(
            @RequestParam("consumeSystem") String consumeSystem,
            @RequestParam("messageId") Long messageId);

    /**
     * 查询最早没有被消费的事务消息
     *
     * @param limit 查询数量
     * @return ResponseDataResult
     */
    @GetMapping("/waiting")
    PageResult findByWaitingMessage(@RequestParam("limit") int limit);

    /**
     * 确认消息死亡
     *
     * @param messageId 事务消息标识
     * @return BaseResult
     */
    @PostMapping("/confirm/die")
    BaseResult confirmDie(@RequestParam("messageId") long messageId);

    /**
     * 累加发送次数
     *
     * @param messageId      事务消息标识
     * @param sendTimeMillis 发送时间(毫秒值)
     * @return BaseResult
     */
    @PostMapping("/incrSendCount")
    BaseResult incrSendCount(
            @RequestParam("messageId") long messageId,
            @RequestParam("sendDate") Long sendTimeMillis);

    /**
     * 重复发送当前已死亡的消息
     *
     * @return BaseResult
     */
    @GetMapping("/send/retry/")
    BaseResult retrySendDieMessage();

}
