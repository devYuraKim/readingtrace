package com.yurakim.readingtrace.user.service.serviceImpl;

import com.yurakim.readingtrace.user.entity.Friend;
import com.yurakim.readingtrace.user.repository.FriendRepository;
import com.yurakim.readingtrace.user.repository.UserRepository;
import com.yurakim.readingtrace.user.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public void toggleFollow(Long followingUserId, Long followedUserId) {
        friendRepository.findByFollowingUserIdAndFollowedUserId(followingUserId, followedUserId).ifPresentOrElse(
                friend -> {
                    friendRepository.deleteById(friend.getId());
                },
                () -> {
                    Friend friend = new Friend();
                    userRepository.findById(followingUserId).ifPresent(friend::setFollowingUser);
                    userRepository.findById(followedUserId).ifPresent(friend::setFollowedUser);
                    friendRepository.save(friend);
                }
        );
    }
}
