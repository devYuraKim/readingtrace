package com.yurakim.readingtrace.auth.controller;

import com.yurakim.readingtrace.shared.constant.ApiPath;
import com.yurakim.readingtrace.user.dto.UserDto;
import com.yurakim.readingtrace.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.ADMIN)
public class AdminController {

    private final UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping("/update-user-roles/{id}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @RequestBody List<String> roleList) {
        UserDto updatedUser = userService.updateUserRoles(id, roleList);
        return ResponseEntity.ok(updatedUser);
    }

}