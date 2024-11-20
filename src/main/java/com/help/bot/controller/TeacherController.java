package com.help.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.help.bot.dto.QusAns;
import com.help.bot.dto.Teacher;
import com.help.bot.service.implementation.TeacherService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	TeacherService teacherService;

	@GetMapping("/register")
	public String loadRegister(ModelMap map) {
		return teacherService.loadRegister(map);
	}
	
	@PostMapping("/register")
	public String register(@Valid Teacher teacher,BindingResult result,HttpSession session) {
	return teacherService.loadRegister(teacher,result,session);

	}
	
	@GetMapping("/otp")
	public String loadOtpPage()
	{
		return "teacher-otp.html";
	}
	
	@PostMapping("/submit-otp/{id}")
	public String submitOtp(@PathVariable int id,@RequestParam int otp,HttpSession session){
		return teacherService.submitOtp(id,otp,session);
	}
	
	@GetMapping("/home")
	public String loadHome(HttpSession session)
	{
		return teacherService.loadHome(session);
	}
	
	@GetMapping("/add-questions")
	public String addQuestions(HttpSession session,ModelMap map) {
		return teacherService.addQuestions(session, map);
	}
	
	@PostMapping("/add-questions")
	public String addQuestions(HttpSession session,@Valid QusAns qusAns,BindingResult result)
	{
		return teacherService.addQuestions(session,qusAns,result);
	}
	
	@GetMapping("/manage-questions")
	public String viewQuestions(HttpSession session,ModelMap map)
	{
		return teacherService.viewQuestions(session,map);
	}
	
	@GetMapping("/delete-Question/{id}")
	public String deleteQuestion(HttpSession session,@PathVariable int id) {
		return teacherService.deleteQuestion(session,id);
	}
	
	
	@GetMapping("/edit-question/{id}")
	public String editQuestion(HttpSession session,@PathVariable int id,ModelMap map) {
		return teacherService.editQuestion(session,id,map);
	}
	
	@PostMapping("edit-question")
	public String updateQuestion(HttpSession session,@Valid QusAns qusAns,BindingResult result) {
		return teacherService.updateQuestion(session,qusAns,result);
	}
	
	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id,HttpSession session) {
		return teacherService.resendOtp(id,session);
	}
	
	
	}
