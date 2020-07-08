package example.sms.server.repository;

import example.model.sms.SysMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysMessageRepository extends JpaRepository<SysMessage, Long> {
}
