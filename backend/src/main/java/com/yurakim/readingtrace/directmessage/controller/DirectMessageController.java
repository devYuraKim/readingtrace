package com.yurakim.readingtrace.directmessage.controller;

import com.yurakim.readingtrace.directmessage.dto.DirectMessageDto;
import com.yurakim.readingtrace.directmessage.dto.MarkReadDto;
import com.yurakim.readingtrace.directmessage.service.DirectMessageService;
import com.yurakim.readingtrace.directmessage.service.MarkReadService;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.USERDM) // /api/v1/users/{userId}/dms
@RequiredArgsConstructor
public class DirectMessageController {

    private final DirectMessageService directMessageService;
    private final MarkReadService markReadService;

    @GetMapping
    public ResponseEntity<List<DirectMessageDto>> getChunkedDirectMessages(
            @PathVariable("userId") Long senderId,
            @RequestParam("to") Long receiverId,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset){
        List<DirectMessageDto> directMessageDtos = directMessageService.getChunkedDirectMessages(senderId, receiverId, limit, offset);
        return ResponseEntity.ok(directMessageDtos);
    }

    @PostMapping("/read")
    public ResponseEntity<MarkReadDto> markRead(@PathVariable("userId") Long scrolledUserId, @RequestBody MarkReadDto markReadDto) {
        markReadDto.setScrolledUserId(scrolledUserId);
        MarkReadDto responseMarkReadDto = markReadService.saveMarkRead(markReadDto);
        return ResponseEntity.ok(responseMarkReadDto);
    }

}