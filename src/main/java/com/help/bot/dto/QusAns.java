package com.help.bot.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Data
@Component
public class QusAns {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message = "* Enter Something")
	private String question;
	@NotEmpty(message = "* Enter Something")
	private String answer;
	private boolean approved;
	@ManyToOne
	Teacher teacher;
	
	public int getQuantity(List<Question> ques) {
        if (ques == null) {
            return 0;
        } else {
            for (Question qus : ques) {
                if (this.question.equals(qus.getQuestion())) {
                    return qus.getQuantity();
                }
            }
            return 0;
        }
    }

}
