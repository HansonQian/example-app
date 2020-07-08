package example.result;

import example.code.ResultCode;
import example.enums.CommonCodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PageResult extends BaseResult {
    private Long total;
    private List<?> rows;

    public PageResult(ResultCode codeEnum) {
        super(codeEnum);
    }

    public PageResult(ResultCode codeEnum, Long total, List<?> rows) {
        super(codeEnum);
        this.total = total;
        this.rows = rows;
    }

    public static PageResult ok(Long total, List<?> rows) {
        return new PageResult(CommonCodeEnum.SUCCESS, total, rows);
    }


}
