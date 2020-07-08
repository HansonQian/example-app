package example.result;

import example.code.ResultCode;
import example.enums.CommonCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @className: DataResult
 * @description: 数据结果类
 * @author: Hanson
 * @version: V1.0
 * @create: 2020-01-28 15:32
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DataResult extends BaseResult {
    private Object data;

    public DataResult(ResultCode codeEnum) {
        super(codeEnum);
    }

    public DataResult(ResultCode codeEnum, Object data) {
        super(codeEnum);
        this.data = data;
    }

    public static DataResult ok(Object data) {
        return new DataResult(CommonCodeEnum.SUCCESS, data);
    }
}
