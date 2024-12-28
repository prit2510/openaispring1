package com.openaispring.openaispring.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuizRequestform {
    private int grad;
    private String subject;
    private int totalQuestions;
    private int maxScore;
    private String difficulty;
}
