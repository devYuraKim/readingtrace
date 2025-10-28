package com.yurakim.readingtrace.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
        return chatClientBuilder.build();
    }

    @Bean
    public ChatClient googleGenAiChatClient(GoogleGenAiChatModel chatModel){
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
        return chatClientBuilder.build();
    }
}
