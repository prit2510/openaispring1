package com.openaispring.openaispring.forms;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {
    private String question;
    private List<String> options;
    @SerializedName("correctAnswer")
    private String correctAnswer;
}
