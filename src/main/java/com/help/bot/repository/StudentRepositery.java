package com.help.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.bot.dto.Student;

public interface StudentRepositery extends JpaRepository<Student, Integer>{

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

	Student findByEmail(String email);
	

}
