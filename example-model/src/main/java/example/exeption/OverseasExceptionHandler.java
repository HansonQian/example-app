package example.exeption;

import com.google.common.collect.ImmutableMap;
import example.enums.CommonCodeEnum;
import example.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class OverseasExceptionHandler {
    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, CommonCodeEnum> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    public static ImmutableMap.Builder<Class<? extends Throwable>, CommonCodeEnum> builder = ImmutableMap.builder();

    static {
        //定义异常类型所对应的错误代码
        // 参数错误
        builder.put(HttpMessageNotReadableException.class, CommonCodeEnum.INVALID_PARAM);
        // 权限不足,禁止访问
        // builder.put(AccessDeniedException.class, CommonCodeEnum.NO_AUTH_CODE);
    }

    /**
     * 处理自定义异常拦截器
     *
     * @param e 异常信息
     * @return BaseResult
     */
    @ExceptionHandler(OverseasException.class)
    public BaseResult handleOVException(OverseasException e) {
        e.printStackTrace();
        log.error("自定义异常 catch exception:{}", e.getMessage());
        BaseResult result = new BaseResult();
        result.setMessage(e.getMsg());
        result.setCode(e.getCode());
        return result;
    }


    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        e.printStackTrace();

        log.error("catch exception:{}", e.getMessage());

        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应500异常
        CommonCodeEnum codeEnum = EXCEPTIONS.get(e.getClass());
        // 构建返回结果
        BaseResult result = new BaseResult();
        if (codeEnum != null) {
            result.setCode(codeEnum.getCode());
            result.setMessage(codeEnum.getMessage());
        } else {
            CommonCodeEnum serverErrorCode = CommonCodeEnum.SERVER_ERROR;
            result.setMessage(serverErrorCode.getMessage());
            result.setCode(serverErrorCode.getCode());
        }
        return result;
    }

}
