package com.yurakim.readingtrace.ai.controller;

import com.yurakim.readingtrace.ai.dto.ChatResponseDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;
import com.yurakim.readingtrace.ai.service.AiService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.USERAI) // /api/v1/users/{userId}/ai
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public ResponseEntity<ChatResponseDto> sendPrompt(@RequestBody UserMessageDto umDto,
                                                      @RequestParam String model) {
        ChatResponseDto amDto = aiService.getResponseFromChatModel(umDto, model);
        return ResponseEntity.ok(amDto);
    }

}