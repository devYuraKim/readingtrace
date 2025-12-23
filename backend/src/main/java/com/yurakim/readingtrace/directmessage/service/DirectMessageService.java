package com.yurakim.readingtrace.directmessage.service;

import com.yurakim.readingtrace.directmessage.dto.DirectMessageDto;
import java.util.List;

public interface DirectMessageService {

    void saveDirectMessage(DirectMessageDto directMessageDto);

    List<DirectMessageDto> getAllDirectMessages(Long senderId, Long receiverId);

}
