package example.pay.server.service;

import example.constant.OverseasConstant;
import example.constant.ServiceInstanceList;
import example.enums.TXMessageStatusEnum;
import example.exeption.OverseasException;
import example.model.sms.SysMessage;
import example.model.pay.SysPay;
import example.model.tx.SysTransactionMessage;
import example.pay.server.repository.SysPayRepository;
import example.result.BaseResult;
import example.tx.client.feign.TXMessageFeignClient;
import example.utils.JsonUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PayService {

    private final SysPayRepository payRepository;

    private final TXMessageFeignClient messageFeignClient;

    public PayService(SysPayRepository payRepository,
                      TXMessageFeignClient messageFeignClient) {
        this.payRepository = payRepository;
        this.messageFeignClient = messageFeignClient;
    }

    /**
     * @param acctNo 帐号
     * @param money  金额
     * @return true 支付成功 |false 支付失败
     */
    public boolean pay(String acctNo, String money) {
        SysPay sysPay = new SysPay();
        sysPay.setAcctNo(acctNo);
        Example<SysPay> example = Example.of(sysPay);
        Optional<SysPay> one = payRepository.findOne(example);
        if (!one.isPresent()) {
            throw new OverseasException("帐号信息不存在");
        }
        SysPay retSysPay = one.get();
        BigDecimal balance = retSysPay.getBalance();
        BigDecimal money_ = new BigDecimal(money);
        BigDecimal add = balance.add(money_);
        retSysPay.setBalance(add);
        payRepository.save(retSysPay);
        SysTransactionMessage transactionMessage =
                new SysTransactionMessage()
                        .setMessage(JsonUtil.serialize(
                                new SysMessage()
                                        .setCreateTime(new Date())
                                        .setStatus("1")
                                        .setMsgContent("帐号" + acctNo + "成功缴纳" + money + "元")
                                )
                        )
                        .setConsumeDate(new Date())
                        .setCreateDate(new Date())
                        .setSendSystem(ServiceInstanceList.FRAMEWORK_PAY_SERVER)
                        .setQueue(OverseasConstant.MQ_SMS_TOPIC);
        BaseResult baseResult = messageFeignClient.saveSingleMessage(transactionMessage);
        boolean flag = baseResult.isFlag();
        if (!flag) {
            throw new OverseasException("回滚事务");
        }
        return flag;
    }

    public boolean save(SysPay pay) {
        this.payRepository.save(pay);
        return true;
    }

    public List<SysPay> findAllPayInfo() {
        return this.payRepository.findAll();
    }
}
