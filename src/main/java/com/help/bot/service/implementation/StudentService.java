package com.help.bot.service.implementation;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.help.bot.dto.Student;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface StudentService {

	String loadRegister(ModelMap map);

	String loadRegister(@Valid Student student, BindingResult result, HttpSession session);

	String submitOtp(int id, int otp, HttpSession session);

	String viewQuestions(HttpSession session, ModelMap map);

	String loadHome(HttpSession session);

	String getAnswer(String question, ModelMap map, HttpSession session);

	String viewChatHistory(HttpSession session, ModelMap map);
	
	

}
