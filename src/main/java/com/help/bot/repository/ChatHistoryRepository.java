package com.help.bot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.bot.dto.ChatHistory;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Integer>{

	List<ChatHistory> findByStudentId(int studentId);

}
