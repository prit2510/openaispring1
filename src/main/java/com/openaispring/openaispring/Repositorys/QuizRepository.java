package com.openaispring.openaispring.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openaispring.openaispring.entitys.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, String> {
}

