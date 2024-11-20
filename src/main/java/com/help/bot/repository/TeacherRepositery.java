package com.help.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.bot.dto.Teacher;

public interface TeacherRepositery extends JpaRepository<Teacher, Integer>{

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

	Teacher findByEmail(String email);



	
	

}
