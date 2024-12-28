package com.openaispring.openaispring.Controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.openaispring.openaispring.Models.ChatCompletionRequest;
import com.openaispring.openaispring.Models.ChatCompletionResponse;
import com.openaispring.openaispring.Services.QuizService;
import com.openaispring.openaispring.entitys.Quiz;
import com.openaispring.openaispring.entitys.que;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.openaispring.openaispring.forms.QuizRequestform;
import com.openaispring.openaispring.forms.QuizResponse;

@RestController
public class MainController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @PostMapping("/hitGroqApi")
    public ResponseEntity<Quiz> getGroqResponseAndSaveQuiz(@RequestBody QuizRequestform quizRequest) {
        int grade = quizRequest.getGrad();
        String subject = quizRequest.getSubject();
        int totalQuestions = quizRequest.getTotalQuestions();
        String difficulty =quizRequest.getDifficulty();
        // String prompt = "generate " + noQue + " mcqs quiz on " + topic + " give response in json format";
        String prompt ="generate " + totalQuestions + " mcqs quiz on " + subject + " the difficulty of the question should be "+ difficulty +" give response in json format. Make sure you use the format as per the following:- question: value, options: [values], correctAnswer: value. Consider the options as a inner array in json";
        // String prompt = "Generate " + totalQuestions + " MCQs quiz on " + subject + " with difficulty level " + difficulty + ". Provide the response in JSON format. Use the following structure: {  question: value,  options: [  'A. option1',  'B. option2',  'C. option3',  'D. option4'  ],  correctAnswer: 'A'  }Ensure that the 'correctAnswer' field contains only the label (A, B, C, or D), corresponding to the correct option." ; 

        try {
            // Call the external AI API
            ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest("llama3-groq-70b-8192-tool-use-preview", prompt);
            ChatCompletionResponse response = restTemplate.postForObject(groqApiUrl, chatCompletionRequest, ChatCompletionResponse.class);
            
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                throw new RuntimeException("Invalid response from AI API");
            }

            String responseContent = response.getChoices().get(0).getMessage().getContent();

            // Check if the response is JSON
            if (responseContent.startsWith("{") || responseContent.startsWith("[")) {
                Gson gson = new Gson();
                QuizResponse quizResponse = gson.fromJson(responseContent, QuizResponse.class);
                  
    
        Quiz quiz = quizService.generateQuiz(
            quizRequest.getGrad(),
            quizRequest.getSubject(),
            quizRequest.getTotalQuestions(),
            quizRequest.getMaxScore(),
            quizRequest.getDifficulty(),
            quizResponse
        );
                return ResponseEntity.ok(quiz);

            } else {
                // If response is not JSON
                return ResponseEntity.badRequest().body(null);
            }

        } catch (JsonSyntaxException e) {
            // Handle JSON parsing errors
            return ResponseEntity.badRequest().body(null);
        } catch (HttpClientErrorException e) {
            // Handle HTTP errors
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            // Handle general errors
            return ResponseEntity.status(500).body(null);
        }
    }

    // Get quiz questions
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<que>> getQuestions(@PathVariable String quizId) {
        List<que> questions = quizService.getQuestionsForQuiz(quizId);
        return ResponseEntity.ok(questions);
    }
}

