package example.tx.server.repository;

import example.model.tx.SysTransactionMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TXMessageRepository extends JpaRepository<SysTransactionMessage, Long> {
    /**
     * 分页查询
     *
     * @param spec     查询条件
     * @param pageable 分页条件
     * @return 事务消息
     */
    Page<SysTransactionMessage> findAll(Specification<SysTransactionMessage> spec, Pageable pageable);

    /**
     * 重发当前已死亡消息
     *
     * @param sendCount  发送次数 更新为0
     * @param waitStatus 置位等待状态
     * @param dieStatus  更新已经死亡的消息
     * @return 影响行数
     */
    @Modifying
    @Query("update SysTransactionMessage m set m.sendCount =?1, m.msgStatus =?2 where m.msgStatus =?3")
    int updateTransactionMessageByMsgStatus(int sendCount, int waitStatus, int dieStatus);

}
