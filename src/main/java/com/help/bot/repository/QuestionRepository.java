package com.help.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.bot.dto.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

}
