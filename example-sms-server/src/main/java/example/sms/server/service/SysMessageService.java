package example.sms.server.service;

import example.model.sms.SysMessage;
import example.sms.server.repository.SysMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysMessageService {

    private final SysMessageRepository messageRepository;

    public SysMessageService(SysMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public boolean saveMessage(SysMessage message) {
        this.messageRepository.save(message);
        return true;
    }

    public List<SysMessage> findAllPayInfo() {
        return messageRepository.findAll();
    }
}
