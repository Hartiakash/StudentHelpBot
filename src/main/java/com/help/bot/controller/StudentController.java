package com.help.bot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.help.bot.dto.Student;
import com.help.bot.service.implementation.StudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	
	@GetMapping("/register")
	public String loadRegister(ModelMap map) {
		return studentService.loadRegister(map);
	}
	
	@PostMapping("/register")
	public String loadRegister(@Valid Student student,BindingResult result,HttpSession session) {
		return studentService.loadRegister(student,result,session);
	}
	
	@GetMapping("/otp")
	public String loadOtpPage() {
		return "student-otp.html";
	}
	
	@PostMapping("/submit-otp/{id}")
	public String submitOtp(@PathVariable int id,@RequestParam int otp,HttpSession session)
	{
		return studentService.submitOtp(id,otp,session);
	}
	
	@GetMapping("/home")
	public String loadHome(HttpSession session)
	{
		return studentService.loadHome(session);
	}
	
	@GetMapping("/questions")
	public String viewQuestions(HttpSession session,ModelMap map) {
		return studentService.viewQuestions(session,map);
	}
	
	@GetMapping("/getAnswer")
	public String getAnswer(@RequestParam("question") String question, ModelMap map, HttpSession session) {
	    return studentService.getAnswer(question, map, session);
	}
	
	
	
	
	

}
