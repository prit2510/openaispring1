package com.openaispring.openaispring.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage {
    private String role;
    private String content;
}
