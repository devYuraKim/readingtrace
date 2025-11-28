package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.user.entity.UserProfile;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
import com.yurakim.readingtrace.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

   private final UserProfileRepository userProfileRepository;

   @Override
   public UserProfile getUserProfileByUserId(Long userId){
       return userProfileRepository.findById(userId).orElse(null);
   }
}
