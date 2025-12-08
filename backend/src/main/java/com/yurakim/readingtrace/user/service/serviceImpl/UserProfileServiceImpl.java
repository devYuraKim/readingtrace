package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.book.entity.UserReadingStatus;
import com.yurakim.readingtrace.book.repository.UserReadingStatusRepository;
import com.yurakim.readingtrace.user.dto.UserProfileResponseDto;
import com.yurakim.readingtrace.user.entity.Friend;
import com.yurakim.readingtrace.user.entity.UserProfile;
import com.yurakim.readingtrace.user.repository.FriendRepository;
import com.yurakim.readingtrace.user.repository.UserProfileRepository;
import com.yurakim.readingtrace.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

   private final UserProfileRepository userProfileRepository;
    private final FriendRepository friendRepository;
    private final UserReadingStatusRepository userReadingStatusRepository;

    @Override
   public UserProfile getUserProfileByUserId(Long userId){
       return userProfileRepository.findByUserId(userId);
   }

    @Override
    public List<UserProfileResponseDto> getAllUserProfilesExceptUserId(Long userId) {
       //1.Get UserProfiles
       List<UserProfile> profiles = userProfileRepository.findAllByUserIdNot(userId);
       Set<Long> userIdsFromProfiles = profiles.stream().map(profile -> profile.getUser().getId()).collect(Collectors.toSet());

       //2.Get UserReadingStatus and separate them by 'public' and 'friends'
       List<UserReadingStatus> userReadingStatuses = userReadingStatusRepository.findAllByUserIdIn(userIdsFromProfiles);
       List<UserReadingStatus> publicUserReadingStatuses = userReadingStatuses.stream().filter(status -> status.getVisibility().equals("public")).collect(Collectors.toList());
       List<UserReadingStatus> friendsUserReadingStatuses = userReadingStatuses.stream().filter(status -> !status.getVisibility().equals("private")).collect(Collectors.toList());
;

       Set<Friend> friends = friendRepository.findByFollowingUserIdAndFollowedUserIdIn(userId, userIdsFromProfiles);
       Set<Long> userIdsFromFriends = friends.stream().map(friend -> friend.getFollowedUser().getId()).collect(Collectors.toSet());

       return userProfileRepository.findAllByUserIdNot(userId).stream().map(userProfile -> {
            UserProfileResponseDto dto = new UserProfileResponseDto();
            dto.setUserProfileId(userProfile.getId());
            dto.setUserId(userProfile.getUser().getId());
            dto.setNickname(userProfile.getNickname());
            dto.setProfileImageUrl(userProfile.getProfileImageUrl());
            dto.setReadingGoalCount(userProfile.getReadingGoalCount());
            dto.setReadingGoalUnit(userProfile.getReadingGoalUnit());
            dto.setReadingGoalTimeframe(userProfile.getReadingGoalTimeframe());
            dto.setFavoredGenres(userProfile.getFavoredGenres());
            dto.setIsOnboardingCompleted(userProfile.getIsOnboardingCompleted());
            boolean isFriend = userIdsFromFriends.contains(userProfile.getUser().getId());
            dto.setIsFriend(isFriend);
            if(isFriend) {
                Set<UserReadingStatus> friendsUserReadingStatus = friendsUserReadingStatuses.stream().filter(status -> status.getUserId().equals(userProfile.getUser().getId())).collect(Collectors.toSet());
                dto.setBookIds(friendsUserReadingStatus.stream().map(status -> status.getBook().getId()).collect(Collectors.toSet()));

            }
            else{
                Set<UserReadingStatus> publicUserReadingStatus = publicUserReadingStatuses.stream().filter(status -> status.getUserId().equals(userProfile.getUser().getId())).collect(Collectors.toSet());
                dto.setBookIds(publicUserReadingStatus.stream().map(status -> status.getBook().getId()).collect(Collectors.toSet()));
            }
            return dto;
       }).toList();
    }


}
