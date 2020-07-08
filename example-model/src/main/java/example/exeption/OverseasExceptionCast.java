package example.exeption;

import example.code.ResultCode;

public class OverseasExceptionCast {
    public OverseasException cast(ResultCode code) {
        return new OverseasException(code);
    }
}
