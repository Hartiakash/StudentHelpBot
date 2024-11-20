package com.help.bot.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.help.bot.dto.Student;
import com.help.bot.dto.Teacher;


import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;

@Service
public class MyEmailSender {

	
	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void sendOtp(Teacher teacher) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("hartiakash17@gmail.com", "HelpBot Site");
			helper.setTo(teacher.getEmail());
			helper.setSubject("Otp for Account Creation");

			Context context = new Context();
			context.setVariable("teacher", teacher);

			String text = templateEngine.process("teacher-email.html", context);
			helper.setText(text, true);
			
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("**************" + teacher.getOtp() + "***********************");
	}

	public void sendOtp(@Valid Student student) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("hartiakash17@gmail.com", "HelpBot Site");
			helper.setTo(student.getEmail());
			helper.setSubject("Otp for Account Creation");

			Context context = new Context();
			context.setVariable("student", student);

			String text = templateEngine.process("student-email.html", context);
			helper.setText(text, true);
			
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("**************" + student.getOtp() + "***********************");
	}
}
