package com.yurakim.readingtrace.ai.controller;

import com.yurakim.readingtrace.ai.dto.UserMessageDto;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.USERAI) // /api/v1/users/{userId}/ai
@RequiredArgsConstructor
public class AiController {

    private final @Qualifier("googleGenAiChatClient") ChatClient googleGenAiChatClient;
    private final @Qualifier("openAiChatClient") ChatClient openAiChatClient;

    @PostMapping
    public ResponseEntity<String> sendPrompt(@RequestBody UserMessageDto dto,
                                             @RequestParam String model) {
        ChatClient client;
        switch (model.toLowerCase()) {
            case "gemini": client = googleGenAiChatClient; break;
            case "chatgpt": client = openAiChatClient; break;
            default: throw new IllegalArgumentException("Unknown model: " + model);
        }

//        add bookInfo to system message for context
//        String bookInfo = String.format("""
//                        [Book Info] title: %s, author: %s, publisher: %s, publishedDate: %s,
//                        isbn10: %s, isbn13: %s, lanaguage: %s
//                        """,
//                        dto.getTitle(), dto.getAuthor(), dto.getPublisher(), dto.getPublishedDate(),
//                        dto.getIsbn10(), dto.getIsbn13(), dto.getLanguage());

        String response = client.prompt().user(dto.getUserMessage()).call().content();
        return ResponseEntity.ok(response);
    }

}