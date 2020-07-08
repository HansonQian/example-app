package example.result;

import example.code.ResultCode;
import example.enums.CommonCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@NoArgsConstructor
public class BaseResult implements BaseResponse {
    /**
     * 是否成功
     */
    private boolean flag = SUCCESS;
    /**
     * 返回码
     */
    private String code = SUCCESS_CODE;
    /**
     * 返回信息
     */
    private String message;

    public BaseResult(ResultCode code) {
        this.flag = code.isSuccess();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public static BaseResult ok() {
        return new BaseResult(CommonCodeEnum.SUCCESS);
    }

    public static BaseResult fail() {
        return new BaseResult(CommonCodeEnum.FAIL);
    }
}
