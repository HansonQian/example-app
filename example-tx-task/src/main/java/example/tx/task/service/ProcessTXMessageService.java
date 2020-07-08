package example.tx.task.service;

import com.fasterxml.jackson.core.type.TypeReference;
import example.model.tx.SysTransactionMessage;
import example.result.PageResult;
import example.tx.client.feign.TXMessageFeignClient;
import example.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@Service("processTXMessageService")
public class ProcessTXMessageService {
    // 事务消息Feign客户端API
    private final TXMessageFeignClient txMessageFeignClient;
    // 用于实现分布式锁
    private final RedissonClient redissonClient;
    // 创建固定大小的线程池
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    // 信号量
    private Semaphore semaphore = new Semaphore(20);

    private final RabbitTemplate rabbitTemplate;

    public ProcessTXMessageService(TXMessageFeignClient txMessageFeignClient,
                                   RedissonClient redissonClient,
                                   RabbitTemplate rabbitTemplate) {
        this.txMessageFeignClient = txMessageFeignClient;
        this.redissonClient = redissonClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    private final static String LOCK_NAME = "EXAMPLE_TX_TASK";

    public void start() {
        log.info("==============start启动==============");
        Thread thread = new Thread(() -> {
            while (true) {
                RLock lock = redissonClient.getLock(LOCK_NAME);
                try {
                    lock.lock();
                    log.info("开始发送消息");
                    int sleepTime = process();
                    if (sleepTime > 0) {
                        Thread.sleep(10000);
                    }
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    lock.unlock();
                }
            }
        });
        thread.start();
    }


    private int process() throws Exception {
        // 默认执行完成之后等待10秒
        int sleepTime = 10000;

        PageResult pageResult = txMessageFeignClient.findByWaitingMessage(5000);

        List<?> rows = pageResult.getRows();


        int size = rows.size();
        log.info("当前共有{}条待消费消息", size);

        if (size == 5000) {
            sleepTime = 0;
        }
        log.info("待消费事务消息:{}",rows);

        CountDownLatch countDownLatch = new CountDownLatch(size);

        List<SysTransactionMessage> messageList =
                JsonUtil.mapper.convertValue(rows, new TypeReference<List<SysTransactionMessage>>() {});

        if (CollectionUtils.isEmpty(messageList)) {
            sleepTime = 0;
        } else {
            for (SysTransactionMessage message : messageList) {
                semaphore.acquire();
                fixedThreadPool.execute(() -> {
                    try {
                        doProcess(message);
                    } catch (Exception e) {
                        log.error("", e);
                    } finally {
                        semaphore.release();
                        countDownLatch.countDown();
                    }
                });
            }
        }
        countDownLatch.await();
        return sleepTime;

    }

    /**
     * 具体进行消息发送的逻辑
     *
     * @param message 消息
     */
    private void doProcess(SysTransactionMessage message) {
        if (message.getSendCount() > message.getDieCount()) {
            txMessageFeignClient.confirmDie(message.getMessageId());
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();

        long sendTime = 0;

        if (message.getSendDate() != null) {
            sendTime = message.getSendDate().getTime();
        }

        if (currentTimeMillis - sendTime > 60000) {
            log.info("发送具体消息:{}", message);
            rabbitTemplate.convertAndSend(message.getQueue(), JsonUtil.serialize(message));
            txMessageFeignClient.incrSendCount(message.getMessageId(), new Date().getTime());
        }


    }


}
