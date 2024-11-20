package com.help.bot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.help.bot.dto.QusAns;
import com.help.bot.repository.QusAnsRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	QusAnsRepository qusAnsRepository;
	
	@GetMapping("/home")
	public String loadHome(HttpSession session)
	{
		if(session.getAttribute("admin")!=null)
			return "admin-home.html";
		else {
			session.setAttribute("failure", "Invalid Session,Login Again");
			return "redirect:/login";
		}
	}
	
	@GetMapping("/question")
	public String displayQuestions(HttpSession session,ModelMap map) {
		if(session.getAttribute("admin")!=null) {
		List<QusAns> question=qusAnsRepository.findAll();
		if(question.isEmpty()) {
			session.setAttribute("failure", "No Questions Added Yet");
			return "redirect:admin/home";
		}
		else {
			map.put("question", question);
			return "admin-questions.html";
		}
		
		}
		else {
			session.setAttribute("failure", "Invalid Session,Login Again");
			return "redirect:/login";
		}
	}
	
	@GetMapping("/approve-question/{id}")
	public String displayQuestions(HttpSession session,ModelMap map,@PathVariable int id) {
		if(session.getAttribute("admin")!=null) {
			QusAns qusAns=qusAnsRepository.findById(id).orElseThrow();
			qusAns.setApproved(true);
			qusAnsRepository.save(qusAns);
			session.setAttribute("failure", "Question Approved Succesfully");
			return "redirect:/admin/question";
		}
		else {
			session.setAttribute("failure", "Invalid session,Login Again");
			return "redirect:/login";
		}
	}
	
	@GetMapping("/reject-question/{id}")
	public String displayQuestion(HttpSession session,ModelMap map,@PathVariable int id) {
		if(session.getAttribute("admin")!=null) {
			QusAns qusAns=qusAnsRepository.findById(id).orElseThrow();
			qusAns.setApproved(false);
			qusAnsRepository.save(qusAns);
			session.setAttribute("failure", "Question Rejected Successfully");
			return "redirect:/admin/question";
			
		}
		else {
			session.setAttribute("failure","Invalid Session,Login Again");
			return "redirect:/login";
		}
	}

}
