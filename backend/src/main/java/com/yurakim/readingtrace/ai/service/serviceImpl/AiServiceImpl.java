package com.yurakim.readingtrace.ai.service.serviceImpl;

import com.yurakim.readingtrace.ai.config.ChatClientConfig;
import com.yurakim.readingtrace.ai.dto.ChatRecordDto;
import com.yurakim.readingtrace.ai.dto.UserMessageDto;
import com.yurakim.readingtrace.ai.entity.ChatRecord;
import com.yurakim.readingtrace.ai.mapper.ChatMapper;
import com.yurakim.readingtrace.ai.repository.ChatRepository;
import com.yurakim.readingtrace.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AiServiceImpl implements AiService {

    private final @Qualifier("googleGenAiChatClient") ChatClient googleGenAiChatClient;
    private final @Qualifier("openAiChatClient") ChatClient openAiChatClient;

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public ChatRecordDto generateChatModelResponse(UserMessageDto userMessageDto, Long userId, String chatModel) {
        ChatClient client;
        switch (chatModel.toLowerCase()) {
            case "gemini": client = googleGenAiChatClient; break;
            case "chatgpt": client = openAiChatClient; break;
            default: throw new IllegalArgumentException("Unknown model: " + chatModel);
        }

        String bookInfo = String.format("""
                [Book Info] title: %s, author: %s, publisher: %s, publishedDate: %s,
                isbn10: %s, isbn13: %s, lanaguage: %s
                """,
                userMessageDto.getTitle(), userMessageDto.getAuthor(), userMessageDto.getPublisher(), userMessageDto.getPublishedDate(),
                userMessageDto.getIsbn10(), userMessageDto.getIsbn13(), userMessageDto.getLanguage());
        String systemMessage = ChatClientConfig.DEFAULT_SYSTEM_MESSAGE + bookInfo;

        ChatRecord chatRecord = new ChatRecord();
        try{
            ChatResponse chatResponse = client.prompt().user(userMessageDto.getUserMessage()).system(systemMessage).call().chatResponse();
            chatRecord.setUserId(userId);
            chatRecord.setBookId(userMessageDto.getBookId());
            chatRecord.setTimestamp(userMessageDto.getTimestamp());
            chatRecord.setModel(chatModel);
            chatRecord.setUserMessage(userMessageDto.getUserMessage());
            chatRecord.setAssistantMessage(chatResponse.getResult().getOutput().getText());
            chatRecord.setPromptTokens(chatResponse.getMetadata().getUsage().getPromptTokens());
            chatRecord.setCompletionTokens(chatResponse.getMetadata().getUsage().getCompletionTokens());
            chatRecord.setTotalTokens(chatResponse.getMetadata().getUsage().getTotalTokens());
            chatRecord.setFinishReason(chatResponse.getResult().getMetadata().getFinishReason());
            chatRecord.setIsSuccess(true);
        } catch(Exception e) {
            chatRecord.setIsSuccess(false);
            chatRecord.setError(e.getMessage());
        }finally{
            chatRepository.save(chatRecord);
            return chatMapper.toChatRecordDto(chatRecord);
        }
    }

}
