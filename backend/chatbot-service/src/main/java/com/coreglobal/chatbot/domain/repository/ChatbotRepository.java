package com.coreglobal.chatbot.domain.repository;

import com.coreglobal.chatbot.domain.model.ChatbotRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotRepository extends JpaRepository<ChatbotRecord, UUID> {
    Page<ChatbotRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
