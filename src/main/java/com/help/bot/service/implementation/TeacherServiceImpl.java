package com.help.bot.service.implementation;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.help.bot.dto.QusAns;
import com.help.bot.dto.Teacher;
import com.help.bot.helper.AES;
import com.help.bot.helper.MyEmailSender;
import com.help.bot.repository.QusAnsRepository;
import com.help.bot.repository.StudentRepositery;
import com.help.bot.repository.TeacherRepositery;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class TeacherServiceImpl implements TeacherService{
	@Autowired
	Teacher teacher;
	
	@Autowired
	QusAns qusAns;
	
	@Autowired
	QusAnsRepository qusAnsRepository;
	
	@Autowired
	StudentRepositery studentRepositery;
	
	@Autowired
	TeacherRepositery teacherRepositery;
	
	@Autowired
	MyEmailSender emailsender;
	
	public String loadRegister(ModelMap map)
	{
		map.put("teacher", teacher);
		return "teacher-register.html";
	}

	@Override
	public String loadRegister(@Valid Teacher teacher, BindingResult result, HttpSession session) {
		if(!teacher.getPassword().equals(teacher.getConfirmpassword()))
			result.rejectValue("confirmpassword", "error.confirmpassword", "* Password Missmatch");
		if(teacherRepositery.existsByEmail(teacher.getEmail()) ||studentRepositery.existsByEmail(teacher.getEmail()))	
			result.rejectValue("email", "error.email", "* Email should be Unique");
		if(teacherRepositery.existsByMobile(teacher.getMobile())|| studentRepositery.existsByMobile(teacher.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number should be Unique");
		
		if(result.hasErrors())
		return "teacher-register.html";
		else {
			int otp=new Random().nextInt(100000,1000000);
			teacher.setOtp(otp);
			teacher.setPassword(AES.encrypt(teacher.getPassword(), "123"));
			teacherRepositery.save(teacher);
			emailsender.sendOtp(teacher);
			
			session.setAttribute("success", "Otp Sent Success");
			session.setAttribute("id", teacher.getId());
			return "redirect:/teacher/otp";
			
			
		}
			
	}

	@Override
	public String submitOtp(int id, int otp, HttpSession session) {
		
		Teacher teacher=teacherRepositery.findById(id).orElseThrow();
		if(teacher.getOtp()==otp)
		{
			teacher.setVerified(true);
			teacherRepositery.save(teacher);
			session.setAttribute("success", "Account Created Success");
			return "redirect:/";
		}
		else {
			session.setAttribute("failure", "Invalid Otp");
			session.setAttribute("id", teacher.getId());
			return "redirect:/teacher/otp";
		}
		
	}

	@Override
	public String loadHome(HttpSession session) {
		if (session.getAttribute("teacher") != null)
			return "teacher-home.html";
		else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
	}
		
	}

	@Override
	public String addQuestions(HttpSession session, ModelMap map) {
		if(session.getAttribute("teacher")!=null) {
			map.put("qusAns", qusAns);
			return "add-questions.html";
		}
		else {
			session.setAttribute("failure", "Invalid session,Login Again");
			return "redirect:/login";
		}
	}

	@Override
	public String addQuestions(HttpSession session, @Valid QusAns qusAns, BindingResult result) {
		if(session.getAttribute("teacher")!=null) {
			if(result.hasErrors()) {
				return "add-questions.html";
			}
			else {
				qusAns.setTeacher((Teacher)session.getAttribute("teacher"));
				qusAnsRepository.save(qusAns);
				
				session.setAttribute("success", "Question and answer is add success");
				return "redirect:/teacher/home";
				
			}
		}
		else {
			session.setAttribute("failure", "Invalid Session,Login Again");
			return "redirect:/login";
		}
	}

	@Override
	public String viewQuestions(HttpSession session, ModelMap map) {
	    if (session.getAttribute("teacher") != null) {
	        Teacher teacher = (Teacher) session.getAttribute("teacher");

	   
	        List<QusAns> qusAnsList = qusAnsRepository.findByTeacherId(teacher.getId());

	        if (qusAnsList.isEmpty()) {
	            session.setAttribute("failure", "No Questions Added Yet");
	            return "redirect:/teacher/home";
	        } else {
	            // Add the list of questions to the ModelMap to make it accessible in the view
	            map.addAttribute("questions", qusAnsList);
	            return "manage-questions.html"; // Change to your actual questions display view
	        }
	    } else {
	        session.setAttribute("failure", "Invalid Session, Login Again");
	        return "redirect:/login";
	    }
	}

	@Override
	public String deleteQuestion(HttpSession session, int id) {
		if(session.getAttribute("teacher")!=null) {
			qusAnsRepository.deleteById(id);
			session.setAttribute("success", "Question and answer Deleted Sucess");
			return "redirect:/teacher/manage-questions";
		
		}else {
			session.setAttribute("failure", "Invalid Session,Login Again");
			return "redirect:/login";
		}
		}

	@Override
	public String editQuestion(HttpSession session, int id, ModelMap map) {
		if(session.getAttribute("teacher")!=null) {
			QusAns qusAns=qusAnsRepository.findById(id).orElseThrow();
			map.put("qusAns", qusAns);
			return "edit-question.html";
		}
		else {
			session.setAttribute("failure", "Invalid Session,Login Again");
			return "redirect:/login";
		}
	
	}

	@Override
	public String updateQuestion(HttpSession session, @Valid QusAns qusAns, BindingResult result) {
		if (session.getAttribute("teacher") != null) {
			if (result.hasErrors()) {
				return "edit-product.html";
			} else {
				qusAns.setTeacher((Teacher)session.getAttribute("teacher"));
				
				
				qusAnsRepository.save(qusAns);

				session.setAttribute("success", "Question Updated Success");
				
				return "redirect:/teacher/manage-questions";
			}
		}else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/login";
		}
	}

	@Override
	public String resendOtp(int id, HttpSession session) {
		Teacher teacher=teacherRepositery.findById(id).orElseThrow();
		int otp=new Random().nextInt(100000,1000000);
		teacher.setOtp(otp);
		teacherRepositery.save(teacher);
		
		session.setAttribute("success", "Otp Resent Success");
		session.setAttribute("id", teacher.getId());
		return "redirect:/teacher/otp";
	
	
	
	
	
	
	}


	
	

	
	

	

	
	
	
}


