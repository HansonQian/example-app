package example.tx.server.web.controller;

import example.model.tx.SysTransactionMessage;
import example.result.BaseResult;
import example.result.PageResult;
import example.tx.server.service.TXMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transaction")
@Api(tags = "事务消息web接口")
public class TXMessageController {
    private final TXMessageService txMessageService;

    public TXMessageController(TXMessageService txMessageService) {
        this.txMessageService = txMessageService;
    }

    /**
     * 发送消息(single)
     *
     * @param message 事务消息
     * @return BaseResult
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送消息", notes = "发送消息(单条)")
    @ApiImplicitParam(name = "message", value = "SysTransactionMessage请求实体", required = true, dataType = "SysTransactionMessage")
    public BaseResult saveSingleMessage(@RequestBody SysTransactionMessage message) {
        log.info("发送消息:{}", message);
        message.setMessageId(System.currentTimeMillis());
        boolean flag = txMessageService.saveMessage(message);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }


    /**
     * 发送消息(Multi)
     *
     * @param messages 事务消息集合
     * @return BaseResult
     */
    @PostMapping("/sends")
    @ApiOperation(value = "发送消息", notes = "发送消息(多条)")
    @ApiImplicitParam(name = "messages", value = "List<SysTransactionMessage>请求实体", required = true, dataType = "List")
    public BaseResult saveMultiMessage(@RequestBody List<SysTransactionMessage> messages) {
        boolean flag = txMessageService.sendMessage(messages);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }

    /**
     * 确认消息被消费
     *
     * @param consumeSystem 消费系统
     * @param messageId     事务消息标识
     * @return BaseResult
     */
    @PostMapping("/confirm/")
    public BaseResult confirmConsumeMessage(
            @RequestParam("consumeSystem") String consumeSystem,
            @RequestParam("messageId") Long messageId) {
        boolean flag =
                txMessageService.confirmConsumeMessage(consumeSystem, messageId);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }

    /**
     * 查询最早没有被消费的事务消息
     *
     * @param limit 查询数量
     * @return ResponseDataResult
     */
    @GetMapping("/waiting")
    public PageResult findByWaitingMessage(@RequestParam("limit") int limit) {
        List<SysTransactionMessage> messageList
                = txMessageService.queryByWaitingMessage(limit);
        int size = messageList.size();
        return PageResult.ok((long) size, messageList);
    }

    /**
     * 确认消息死亡
     *
     * @param messageId 事务消息标识
     * @return BaseResult
     */
    @PostMapping("/confirm/die")
    public BaseResult confirmDie(@RequestParam("messageId") long messageId) {
        boolean flag = txMessageService.confirmDie(messageId);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }

    /**
     * 累加发送次数
     *
     * @param messageId      事务消息标识
     * @param sendTimeMillis 发送时间(毫秒值)
     * @return BaseResult
     */
    @PostMapping("/incrSendCount")
    public BaseResult incrSendCount(
            @RequestParam("messageId") long messageId,
            @RequestParam("sendDate") Long sendTimeMillis) {
        boolean flag = txMessageService.incrSendCount(messageId, sendTimeMillis);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }

    /**
     * 重复发送当前已死亡的消息
     *
     * @return BaseResult
     */
    @GetMapping("/send/retry/")
    public BaseResult retrySendDieMessage() {
        boolean flag = txMessageService.retrySendDieMessage();
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }

    @ApiOperation(value = "查看全部事务消息", notes = "查看全部事务消息")
    @GetMapping("/findAll")
    public PageResult findAll() {
        List<SysTransactionMessage> messageList
                = txMessageService.findAll();
        int size = messageList.size();
        return PageResult.ok((long) size, messageList);
    }
}
