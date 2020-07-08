package example.exeption;

import example.code.ResultCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverseasException extends RuntimeException {
    private String msg;
    private String code = "500";

    public OverseasException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OverseasException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public OverseasException(String msg, String code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public OverseasException(String msg, String code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public OverseasException(ResultCode codeEnum) {
        super(codeEnum.getMessage());
        this.msg = codeEnum.getMessage();
        this.code = codeEnum.getCode();
    }

    public OverseasException(ResultCode codeEnum, Throwable e) {
        super(codeEnum.getMessage(), e);
        this.msg = codeEnum.getMessage();
        this.code = codeEnum.getCode();
    }
}
