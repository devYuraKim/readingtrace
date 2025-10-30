package com.yurakim.readingtrace.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    public static final String DEFAULT_SYSTEM_MESSAGE = "You are a reading companion. Always focus on the book and the user's questions. Provide clear, concise explanations, summaries, or insights on content, characters, themes, or context. If the user strays, gently redirect them back to the book.";

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
        return chatClientBuilder.defaultSystem(DEFAULT_SYSTEM_MESSAGE).build();
    }

    @Bean
    public ChatClient googleGenAiChatClient(GoogleGenAiChatModel chatModel){
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);
        return chatClientBuilder.defaultSystem(DEFAULT_SYSTEM_MESSAGE).build();
    }
}
