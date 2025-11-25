package com.yurakim.readingtrace.note.controller;

import com.yurakim.readingtrace.note.dto.UserNoteDto;
import com.yurakim.readingtrace.shared.constant.ApiPath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPath.USERNOTE) // /api/v1/users/{userId}/notes
@RestController
public class NoteController {

    @PostMapping()
    ResponseEntity<Void> createUserNote(@PathVariable Long userId, @RequestBody UserNoteDto userNoteDto){
        //set path variable userId as the single source of truth
        userNoteDto.setUserId(userId);

        return null;
    }
}
