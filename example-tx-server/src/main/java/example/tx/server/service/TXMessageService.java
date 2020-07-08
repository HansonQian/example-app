package example.tx.server.service;

import example.enums.TXMessageStatusEnum;
import example.model.tx.SysTransactionMessage;
import example.tx.server.repository.TXMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("txMessageService")
@Transactional(rollbackFor = Exception.class)
public class TXMessageService {

    private final TXMessageRepository txMessageRepository;

    public TXMessageService(TXMessageRepository txMessageRepository) {
        this.txMessageRepository = txMessageRepository;
    }

    /**
     * 新增事务消息
     *
     * @param message 事务消息内容
     * @return true 成功 | false 失败
     */
    public boolean saveMessage(SysTransactionMessage message) {
        if (check(message)) {
            txMessageRepository.save(message);
            return true;
        }
        return false;
    }

    /**
     * 批量新增事务信息
     *
     * @param messageList 事务信息集合
     * @return true 成功 | false 失败
     */
    public boolean sendMessage(List<SysTransactionMessage> messageList) {
        for (SysTransactionMessage message : messageList) {
            if (!check(message)) {
                return false;
            }
        }
        this.txMessageRepository.saveAll(messageList);
        return true;
    }

    /**
     * 确认消息被消费
     *
     * @param consumeSystem 消费系统名称
     * @param messageId     事务消息标识
     * @return true 成功 | false 失败
     */
    public boolean confirmConsumeMessage(String consumeSystem, Long messageId) {
        Optional<SysTransactionMessage> messageOptional = this.txMessageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            SysTransactionMessage message = messageOptional.get();
            message.setConsumeSystem(consumeSystem);
            message.setConsumeDate(new Date());
            message.setMsgStatus(TXMessageStatusEnum.OVER.getStatus());
            this.txMessageRepository.save(message);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 分页查询等待状态的事务消息信息
     *
     * @param limit 查询数量
     * @return 返回满足条件的事务信息
     */
    public List<SysTransactionMessage> queryByWaitingMessage(int limit) {
        if (limit > 1000) {
            limit = 1000;
        }
        SysTransactionMessage message = new SysTransactionMessage();
        message.setMsgStatus(TXMessageStatusEnum.WAITING.getStatus());
        Example<SysTransactionMessage> example = Example.of(message);
        ExampleMatcher matcher = ExampleMatcher.matching();
        matcher.withIgnorePaths("sendCount", "dieCount");
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.ASC, "messageId"));
        Page<SysTransactionMessage> messagePage = txMessageRepository.findAll(example, pageable);
        return messagePage.getContent();
    }

    /**
     * 确认消息死亡
     *
     * @param messageId 事务消息标识
     * @return true 成功 | false 失败
     */
    public boolean confirmDie(Long messageId) {
        Optional<SysTransactionMessage> messageOptional = this.txMessageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            SysTransactionMessage message = messageOptional.get();
            message.setMsgStatus(TXMessageStatusEnum.DIE.getStatus());
            message.setDieDate(new Date());
            this.txMessageRepository.save(message);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 累加发送次数
     *
     * @param messageId      消息标识
     * @param sendTimeMillis 发送时间
     * @return true 成功 | false 失败
     */
    public boolean incrSendCount(long messageId, Long sendTimeMillis) {
        Optional<SysTransactionMessage> messageOptional = this.txMessageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            SysTransactionMessage message = messageOptional.get();
            message.setSendDate(new Date(sendTimeMillis));
            message.setSendCount(message.getSendCount() + 1);
            this.txMessageRepository.save(message);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 重复发送当前已死亡的消息
     *
     * @return true | false
     */
    public boolean retrySendDieMessage() {

        int effectRows = this.txMessageRepository
                .updateTransactionMessageByMsgStatus(0,
                        TXMessageStatusEnum.WAITING.getStatus(),
                        TXMessageStatusEnum.DIE.getStatus());
        if (effectRows > 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查事务消息是否合法
     *
     * @param message 事务消息
     * @return true | false
     */
    private boolean check(SysTransactionMessage message) {
        if (!StringUtils.hasText(message.getMessage()) || !StringUtils.hasText(message.getQueue())
                || !StringUtils.hasText(message.getSendSystem())) {
            return false;
        }
        return message.getCreateDate() != null;
    }

    public List<SysTransactionMessage> findAll() {
        return this.txMessageRepository.findAll();
    }
}
