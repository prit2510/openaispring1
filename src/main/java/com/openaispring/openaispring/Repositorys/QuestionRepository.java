package com.openaispring.openaispring.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openaispring.openaispring.entitys.Quiz;
import com.openaispring.openaispring.entitys.que;

public interface QuestionRepository extends JpaRepository<que, String> {
    List<que> findByQuiz(Quiz quiz);
}

