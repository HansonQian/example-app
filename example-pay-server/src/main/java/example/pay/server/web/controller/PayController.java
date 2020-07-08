package example.pay.server.web.controller;

import example.model.pay.SysPay;
import example.pay.server.service.PayService;
import example.result.BaseResult;
import example.result.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "支付接口")
@RestController
@RequestMapping("/pay")
public class PayController {

    private final PayService payService;

    public PayController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping("/charge")
    @ApiOperation(value = "收费", notes = "根据帐号和金额进行缴费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "acctNo", value = "账户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "money", value = "交易金额", required = true, dataType = "String")})
    public BaseResult pay(String acctNo, String money) {
        boolean flag = payService.pay(acctNo, money);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }


    @PostMapping("/create")
    @ApiOperation(value = "创建一个账户", notes = "新增一个账户账户，用于测试缴费")
    @ApiImplicitParam(name = "pay", value = "SysPay实体", required = true, dataType = "SysPay")
    public BaseResult create(@RequestBody SysPay pay) {
        boolean flag = payService.save(pay);
        if (flag) {
            return BaseResult.ok();
        } else {
            return BaseResult.fail();
        }
    }


    @GetMapping("/query")
    @ApiOperation(value = "查询账户信息", notes = "查询账户信息")
    public PageResult findAllPayInfo() {
        List<SysPay> messageList
                = payService.findAllPayInfo();
        int size = messageList.size();
        return PageResult.ok((long) size, messageList);
    }


}
