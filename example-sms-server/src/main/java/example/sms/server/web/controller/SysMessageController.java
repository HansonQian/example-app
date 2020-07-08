package example.sms.server.web.controller;

import example.model.pay.SysPay;
import example.model.sms.SysMessage;
import example.result.PageResult;
import example.sms.server.service.SysMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "短信接口")
@RestController
@RequestMapping("/sms")
public class SysMessageController {

    private final SysMessageService messageService;

    public SysMessageController(SysMessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/query")
    @ApiOperation(value = "查询短信信息", notes = "查询短信信息")
    public PageResult findAllPayInfo() {
        List<SysMessage> messageList
                = messageService.findAllPayInfo();
        int size = messageList.size();
        return PageResult.ok((long) size, messageList);
    }


}
