package com.yurakim.readingtrace.ai.service.serviceImpl;

import com.yurakim.readingtrace.ai.dto.UserMessageDto;
import com.yurakim.readingtrace.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AiServiceImpl implements AiService {

    private final @Qualifier("googleGenAiChatClient") ChatClient googleGenAiChatClient;
    private final @Qualifier("openAiChatClient") ChatClient openAiChatClient;

    @Override
    public String getResponseFromChatModel(UserMessageDto dto, String model) {
        ChatClient client;
        switch (model.toLowerCase()) {
            case "gemini": client = googleGenAiChatClient; break;
            case "chatgpt": client = openAiChatClient; break;
            default: throw new IllegalArgumentException("Unknown model: " + model);
        }

//        String bookInfo = String.format("""
//                        [Book Info] title: %s, author: %s, publisher: %s, publishedDate: %s,
//                        isbn10: %s, isbn13: %s, lanaguage: %s
//                        """,
//                        dto.getTitle(), dto.getAuthor(), dto.getPublisher(), dto.getPublishedDate(),
//                        dto.getIsbn10(), dto.getIsbn13(), dto.getLanguage());

        ChatResponse chatResponse = client.prompt().user(dto.getUserMessage()).call().chatResponse();
        System.out.println("Full ChatResponse: " + chatResponse);

        return chatResponse.getResult().getOutput().getText();
    }
}
