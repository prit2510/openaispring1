package com.openaispring.openaispring.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.openaispring.openaispring.Repositorys.QuestionRepository;
import com.openaispring.openaispring.Repositorys.QuizRepository;
import com.openaispring.openaispring.entitys.Quiz;
import com.openaispring.openaispring.entitys.que;
import com.openaispring.openaispring.forms.QuizResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Quiz generateQuiz(int grade, String subject, int totalQuestions, int maxScore, String difficulty,QuizResponse quizResponse) {
        Gson gson = new Gson();

        Quiz quiz = new Quiz();
        quiz.setQuizId(UUID.randomUUID().toString());
        quiz.setGrade(grade);
        quiz.setSubject(subject);
        quiz.setTotalQuestions(totalQuestions);
        quiz.setMaxScore(maxScore);
        quiz.setDifficulty(difficulty);
        quiz.setCreatedDate(LocalDateTime.now());

        quizRepository.save(quiz);
         List<que> questionEntities = quizResponse.getQuestions().stream().map(question -> {
                    que queEntity = new que();
                    queEntity.setQuestionId(UUID.randomUUID().toString());
                    queEntity.setQuiz(quiz);
                    queEntity.setQuestionText(question.getQuestion());
                    queEntity.setOptions(gson.toJson(question.getOptions())); // Convert options to JSON format
                    queEntity.setCorrectAnswer(question.getCorrectAnswer());
                    return queEntity;
                }).collect(Collectors.toList());

                questionRepository.saveAll(questionEntities);  

        return quizRepository.save(quiz);
    }

    public List<que> getQuestionsForQuiz(String quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        return questionRepository.findByQuiz(quiz);
    }
}

