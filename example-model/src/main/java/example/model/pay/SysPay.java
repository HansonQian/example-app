package example.model.pay;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "sys_pay")
public class SysPay {
    @Id
    @Column(name = "pay_id", nullable = false, columnDefinition = "bigint(64) COMMENT '主键id'")
    private Long payId;

    @Column(name = "acct_no", nullable = false, columnDefinition = "VARCHAR(32) COMMENT '账户编号'")
    private String acctNo;

    @Digits(integer=5, fraction=2)
    @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(5,2) COMMENT '余额'")
    private BigDecimal balance;

    @Column(name = "last_charge_date", columnDefinition = "DATETIME COMMENT '上次缴费时间'")
    private Date lastChargeDate;

}
