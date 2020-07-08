package example.tx.client.feign.fallback;

import example.model.tx.SysTransactionMessage;
import example.result.BaseResult;
import example.result.PageResult;
import example.tx.client.feign.TXMessageFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TXMessageFeignClientFallbackFactory implements FallbackFactory<TXMessageFeignClient> {

    @Override
    public TXMessageFeignClient create(Throwable throwable) {
        return new TXMessageFeignClient() {
            @Override
            public BaseResult saveSingleMessage(SysTransactionMessage message) {
                log.error("远程调用保存单条事务信息失败，失败原因:{}", throwable.getCause());
                String exMsg = throwable.getMessage();
                BaseResult result = BaseResult.fail();
                result.setMessage(exMsg);
                return result;
            }

            @Override
            public BaseResult saveMultiMessage(List<SysTransactionMessage> messages) {
                return null;
            }

            @Override
            public BaseResult confirmConsumeMessage(String consumeSystem, Long messageId) {
                return null;
            }

            @Override
            public PageResult findByWaitingMessage(int limit) {
                return null;
            }

            @Override
            public BaseResult confirmDie(long messageId) {
                return null;
            }

            @Override
            public BaseResult incrSendCount(long messageId, Long sendTimeMillis) {
                return null;
            }

            @Override
            public BaseResult retrySendDieMessage() {
                return null;
            }
        };
    }
}
