package com.help.bot.service.implementation;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.help.bot.dto.QusAns;
import com.help.bot.dto.Teacher;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface TeacherService {

	String loadRegister(ModelMap map);

	String loadRegister(@Valid Teacher teacher, BindingResult result, HttpSession session);

	String submitOtp(int id, int otp, HttpSession session);

	String loadHome(HttpSession session);

	String addQuestions(HttpSession session, ModelMap map);

	String addQuestions(HttpSession session, @Valid QusAns qusAns, BindingResult result);

	String viewQuestions(HttpSession session, ModelMap map);

	String deleteQuestion(HttpSession session, int id);

	String editQuestion(HttpSession session, int id, ModelMap map);

	String updateQuestion(HttpSession session, @Valid QusAns qusAns, BindingResult result);

	String resendOtp(int id, HttpSession session);



	
	


}
