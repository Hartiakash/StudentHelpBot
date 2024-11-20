package com.help.bot.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.bot.dto.QusAns;

public interface QusAnsRepository extends JpaRepository<QusAns, Integer> {

	List<QusAns> findByTeacherId(int id);

	List<QusAns> findByApprovedTrue();

	Optional<QusAns> findFirstByQuestionAndApprovedTrue(String question);



	




	





	
	

	

}
