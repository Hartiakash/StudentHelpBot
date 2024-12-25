package com.help.bot.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.help.bot.dto.ChatHistory;
import com.help.bot.dto.Student;
import com.help.bot.repository.ChatHistoryRepository;

@Service
public class ChatHistoryService {
    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

   
    public void saveChat(Student student, String question, String response) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setStudent(student);
        chatHistory.setQuestion(question);
        chatHistory.setResponse(response);
        chatHistory.setTimestamp(LocalDateTime.now());
        chatHistoryRepository.save(chatHistory);
    }

    
    public List<ChatHistory> getChatHistory(int studentId) {
        return chatHistoryRepository.findByStudentId(studentId);
    }
}
