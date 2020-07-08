package example.pay.server.repository;

import example.model.pay.SysPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysPayRepository extends JpaRepository<SysPay,Long> {

}
