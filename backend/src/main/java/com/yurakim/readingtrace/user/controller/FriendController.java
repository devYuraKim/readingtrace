package com.yurakim.readingtrace.user.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.user.service.FriendService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(ApiPath.USERFRIEND) // api/v1/users/{userId}/friends
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/{followedUserId}")
    public ResponseEntity<Void> toggleFollow(@PathVariable Long userId, @PathVariable Long followedUserId) {
        friendService.toggleFollow(userId, followedUserId);
        return ResponseEntity.ok().build();
    }

}
