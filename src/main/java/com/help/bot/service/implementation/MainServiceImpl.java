package com.help.bot.service.implementation;



import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.help.bot.helper.AES;
import com.help.bot.helper.MyEmailSender;
import com.help.bot.repository.QusAnsRepository;
import com.help.bot.repository.StudentRepositery;
import com.help.bot.repository.TeacherRepositery;
import com.help.bot.dto.QusAns;
import com.help.bot.dto.Student;
import com.help.bot.dto.Teacher;

import jakarta.servlet.http.HttpSession;



@Service
public class MainServiceImpl implements MainService {
	
	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.password}")
	private String adminPassword;

	@Autowired
	MyEmailSender emailSender;

	@Autowired
	TeacherRepositery teacherRepositery;

	@Autowired
	StudentRepositery studentRepositery;
	
	@Autowired
	QusAnsRepository qusAnsRepository;

	@Override
	public String loadHome(ModelMap map) {
	List<QusAns> qusAns=qusAnsRepository.findByApprovedTrue();
	 if (!qusAns.isEmpty()) {
         map.put("qusAns", qusAns);
     }

     return "home.html";	}
	
	
	public String loadLogin()
	{
		return "Login.html";
	}
	
	
	public String login(String email,String password,HttpSession session)
	{
		 if (email.equals(adminEmail) && password.equals(adminPassword)) {
	            session.setAttribute("admin", "admin");
	            session.setAttribute("success", "Login Success");
	            return "redirect:/admin/home";
	        } else {
	            Teacher teacher = teacherRepositery.findByEmail(email);
	            Student student = studentRepositery.findByEmail(email);

	            if (teacher == null && student == null) {
	                session.setAttribute("failure", "Invalid Email");
	                return "redirect:/login";
	            } else {
	                if (teacher == null) {
	                    if (AES.decrypt(student.getPassword(), "123").equals(password)) {
	                        if (student.isVerified()) {
	                            session.setAttribute("student", student);
	                            session.setAttribute("success", "Login Success");
	                            return "redirect:/student/home";
	                        } else {
	                            student.setOtp(new Random().nextInt(100000, 1000000));
	                            studentRepositery.save(student);
	                            emailSender.sendOtp(student);
	                            session.setAttribute("success", "Otp Sent Success");
	                            session.setAttribute("id", student.getId());
	                            return "redirect:/student/otp";
	                        }
	                    } else {
	                        session.setAttribute("failure", "Invalid Passowrd");
	                        return "redirect:/login";
	                    }
	                } else {
	                    if (AES.decrypt(teacher.getPassword(), "123").equals(password)) {
	                        if (teacher.isVerified()) {
	                            session.setAttribute("teacher", teacher);
	                            session.setAttribute("success", "Login Success");
	                            return "redirect:/teacher/home";
	                        } else {
	                            teacher.setOtp(new Random().nextInt(100000, 1000000));
	                            teacherRepositery.save(teacher);
	                            emailSender.sendOtp(teacher);
	                            session.setAttribute("success", "Otp Sent Success");
	                            session.setAttribute("id", teacher.getId());
	                            return "redirect:/teacher/otp";
	                        }
	                    } else {
	                        session.setAttribute("failure", "Invalid Passowrd");
	                        return "redirect:/login";
	                    }
	                }
	            }
	        }
	}


	@Override
	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		session.removeAttribute("teacher");
		session.removeAttribute("student");
		session.setAttribute("success", "Logged out Success");
		return "redirect:/";
	}
	
	
	


	



}
