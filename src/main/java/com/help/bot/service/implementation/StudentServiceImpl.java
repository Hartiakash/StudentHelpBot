package com.help.bot.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

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
	public String viewQuestions(HttpSession session, ModelMap map){
        if (session.getAttribute("student") != null) {
            List<QusAns> qusAnss = qusAnsRepositery.findByApprovedTrue();
            if (qusAnss.isEmpty()) {
                session.setAttribute("failure", "No Questions Found");
                return "redirect:/student/home";
            } else {
                Student student = (Student) session.getAttribute("student");
                if (student.getCart() != null) {
                    map.put("ques", student.getCart().getQuestions());
                }
                map.put("qusAnss", qusAnss);
                return "student-questions.html";
            }
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

	@Override
	public String getAnswer(String question, ModelMap map, HttpSession session) {
		
		{
		    Optional<QusAns> qusAnsOptional = qusAnsRepositery.findFirstByQuestionAndApprovedTrue(question);
		    if (qusAnsOptional.isPresent()) {
				map.put("question", qusAnsOptional.get().getQuestion());
		        map.put("answer", qusAnsOptional.get().getAnswer());
		        return "student-questions.html";
		    } else {
		        session.setAttribute("failure", "No answer found for the given question.");
		        return "redirect:/student/questions"; 
		    }
		}
		
		
		
		
}
}
