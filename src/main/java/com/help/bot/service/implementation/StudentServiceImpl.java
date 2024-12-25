package com.help.bot.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.help.bot.dto.ChatHistory;
import com.help.bot.dto.QusAns;
import com.help.bot.dto.Student;
import com.help.bot.helper.AES;
import com.help.bot.helper.MyEmailSender;
import com.help.bot.repository.QuestionRepository;
import com.help.bot.repository.QusAnsRepository;
import com.help.bot.repository.StudentRepositery;
import com.help.bot.repository.TeacherRepositery;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class StudentServiceImpl implements StudentService{
	@Autowired
	Student student;
	
	@Autowired
	ChatHistoryService chatHistoryService;
	
	@Autowired
	MyEmailSender emailSender;
	
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	StudentRepositery studentRepositery;
	
	@Autowired
	TeacherRepositery teacherRepositery;
	
	@Autowired
	QusAnsRepository qusAnsRepositery;

	@Override
	public String loadRegister(ModelMap map) {
		map.put("student", student);
		return "student-register.html";
		
	}

	@Override
	public String loadRegister(@Valid Student student, BindingResult result, HttpSession session) {
		if(!student.getPassword().equals(student.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "* Password Missmatch");
		if(studentRepositery.existsByEmail(student.getEmail()) || teacherRepositery.existsByEmail(student.getEmail()))
			result.rejectValue("email", "error.email", "* Email should be Unique");	
		if(studentRepositery.existsByMobile(student.getMobile()) || teacherRepositery.existsByMobile(student.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* mobile number should be Unique");	
		
		if(result.hasErrors())
			return "student-register.html";
		else {
			int otp=new Random().nextInt(100000,1000000);
			student.setOtp(otp);
			student.setPassword(AES.encrypt(student.getPassword(), "123"));
			studentRepositery.save(student);
			emailSender.sendOtp(student);
			session.setAttribute("success", "Otp sent success");
			session.setAttribute("id", student.getId());
			return "redirect:/student/otp";
		}
	}

	@Override
	public String submitOtp(int id, int otp, HttpSession session) {
		Student student=studentRepositery.findById(id).orElseThrow();
		if(student.getOtp()==otp)
		{
			student.setVerified(true);
			studentRepositery.save(student);
			session.setAttribute("success", "Account Created Success");
			return "redirect:/";
		}
		else {
			session.setAttribute("failure", "Invalid OTP");
			session.setAttribute("id", student.getId());
			return "redirect:/student/otp";
		}
		
	}

	@Override
	public String viewQuestions(HttpSession session, ModelMap map) {
	    if (session.getAttribute("student") != null) {
	        Student student = (Student) session.getAttribute("student");
	        List<ChatHistory> chatHistory = chatHistoryService.getChatHistory(student.getId());
	        map.put("chatHistory", chatHistory);

	        List<QusAns> qusAnss = qusAnsRepositery.findByApprovedTrue();
	        if (!qusAnss.isEmpty()) {
	            if (student.getCart() != null) {
	                map.put("ques", student.getCart().getQuestions());
	            }
	            map.put("qusAnss", qusAnss);
	        } else {
	            session.setAttribute("failure", "No Questions Found");
	            return "redirect:/student/home";
	        }
	        return "student-questions.html";
	    } else {
	        session.setAttribute("failure", "Invalid Session, Login Again");
	        return "redirect:/login";
	    }
	}


	@Override
	public String loadHome(HttpSession session) {
		if(session.getAttribute("student")!=null)
			return "student-home.html";
		else {
			session.setAttribute("failure", "Invalid session,Login Again");
			return "redirect:/login";
		}
	}
	
	
	@GetMapping("/getAnswer")
	public String getAnswer(@RequestParam("question") String question, ModelMap map, HttpSession session) {
	    Student student = (Student) session.getAttribute("student");
	    if (student == null) {
	        session.setAttribute("failure", "Invalid session, Login Again");
	        return "redirect:/login";
	    }

	    // Retrieve existing chat history
	    List<ChatHistory> chatHistory = chatHistoryService.getChatHistory(student.getId());
	    map.put("chatHistory", chatHistory);

	    // Handle the new question
	    Optional<QusAns> qusAnsOptional = qusAnsRepositery.findFirstByQuestionAndApprovedTrue(question);
	    if (qusAnsOptional.isPresent()) {
	        String answer = qusAnsOptional.get().getAnswer();

	        // Save the chat interaction
	        chatHistoryService.saveChat(student, question, answer);

	        // Add new chat entry to the map
	        map.put("question", question);
	        map.put("answer", answer);
	    } else {
	        session.setAttribute("failure", "No answer found for the given question.");
	    }

	    return "student-questions.html";
	}


	@Override
	public String viewChatHistory(HttpSession session, ModelMap map) {
		
		Student student = (Student) session.getAttribute("student");
	    if (student != null) {
	        List<ChatHistory> chatHistory = chatHistoryService.getChatHistory(student.getId());
	        map.put("chatHistory", chatHistory);
	        return "student-history.html"; // Create a Thymeleaf or JSP template for displaying history
	    } else {
	        session.setAttribute("failure", "Invalid session, Login Again");
	        return "redirect:/login";
	    }
		
		
		
		
	}

	}
