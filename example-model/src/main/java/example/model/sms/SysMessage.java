package example.model.sms;

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
@Table(name = "sys_message")
public class SysMessage {
    @Id
    @Column(name = "message_id", nullable = false, columnDefinition = "bigint(64) COMMENT '主键id'")
    private Long messageId;

    @Column(name = "msg_content", nullable = false, columnDefinition = "VARCHAR(2000) COMMENT '短信内容'")
    private String msgContent;

    @Column(name = "create_time", columnDefinition = " DATETIME COMMENT '短信创建时间'")
    private Date createTime;

    @Column(name = "send_time", columnDefinition = " DATETIME COMMENT '短信发送时间'")
    private Date sendTime;

    @Column(name = "status", columnDefinition = "VARCHAR(4) COMMENT '短信状态;1已发送2待发送'")
    private String status;

}
