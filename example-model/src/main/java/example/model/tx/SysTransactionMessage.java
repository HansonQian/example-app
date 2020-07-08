package example.model.tx;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "sys_transaction_message")
public class SysTransactionMessage {

    /**
     * 消息id
     */
    //@GenericGenerator(name = "generator", strategy = "uuid")
    @Id
   // @GeneratedValue(generator = "generator")
    @Column(name = "message_id", nullable = false, columnDefinition = "bigint(64) COMMENT '消息id'")
    private Long messageId;

    /**
     * 消息内容
     */
    @Column(name = "message", nullable = false, columnDefinition = "VARCHAR(1000) COMMENT '消息内容'")
    private String message;

    /**
     * 消息队列名称
     */
    @Column(name = "queue", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '消息队列名称'")
    private String queue;


    /**
     * 发送消息的系统
     */
    @Column(name = "send_system", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '发送消息的系统'")
    private String sendSystem;


    /**
     * 重复发送消息次数
     */
    @Column(name = "send_count", nullable = false, columnDefinition = "INT(4) DEFAULT 0 COMMENT '重复发送消息次数'")
    private int sendCount = 0;

    /**
     * 创建时间
     */
    @Column(name = "create_date", nullable = false, columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createDate;

    /**
     * 最近发送消息时间
     */
    @Column(name = "send_date", columnDefinition = "DATETIME COMMENT '最近发送消息时间'")
    private Date sendDate;

    /**
     * 消息状态: 0等待消费 1已消费 2已死亡
     */
    @Column(name = "msg_status", nullable = false, columnDefinition = "INT(4) DEFAULT 0 COMMENT '消息状态: 0等待消费 1已消费 2已死亡'")
    private int msgStatus = 0;

    /**
     * 死亡次数条件,由使用方决定,默认为发送10次还没被消费则标记死亡，人工介入
     */
    @Column(name = "die_count", nullable = false, columnDefinition = "INT(4) DEFAULT 0 COMMENT '死亡次数条件,由使用方决定,默认为发送10次还没被消费则标记死亡，人工介入'")
    private int dieCount = 0;

    /**
     * 消费时间
     */
    @Column(name = "consume_date", columnDefinition = "DATETIME COMMENT '消费时间'")
    private Date consumeDate;

    /**
     * 消费信息的系统
     */
    @Column(name = "consume_system", columnDefinition = "VARCHAR(50) COMMENT '消费信息的系统'")
    private String consumeSystem;

    /**
     * 死亡时间
     */
    @Column(name = "die_date", columnDefinition = "DATETIME COMMENT '死亡时间'")
    private Date dieDate;
}
