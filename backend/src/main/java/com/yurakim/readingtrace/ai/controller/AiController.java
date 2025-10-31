package com.yurakim.readingtrace.ai.controller;

import com.yurakim.readingtrace.ai.dto.ChatRecordDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;
import com.yurakim.readingtrace.ai.service.AiService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.USERAI) // /api/v1/users/{userId}/ai
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public ResponseEntity<ChatRecordDto> generateChatModelResponse(@RequestBody UserMessageDto userMessageDto,
                                                     @PathVariable Long userId, @RequestParam String chatModel) {
        ChatRecordDto chatRecordDto = aiService.generateChatModelResponse(userMessageDto, userId, chatModel);
        return ResponseEntity.ok(chatRecordDto);
    }

    @GetMapping
    public ResponseEntity<List<ChatRecordDto>> getChatRecords(@PathVariable Long userId, @RequestParam Long bookId){
        List<ChatRecordDto> chatRecordDtos = aiService.getChatRecords(userId, bookId);
        return ResponseEntity.ok(chatRecordDtos);
    }

}