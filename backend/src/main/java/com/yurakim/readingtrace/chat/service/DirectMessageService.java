package com.yurakim.readingtrace.chat.service;

import com.yurakim.readingtrace.chat.dto.DirectMessageDto;
import java.util.List;

public interface DirectMessageService {

    void saveDirectMessage(DirectMessageDto directMessageDto);

    List<DirectMessageDto> getAllDirectMessages(Long senderId, Long receiverId);

}
