package com.yurakim.readingtrace.ai.service.serviceImpl;

import com.yurakim.readingtrace.ai.config.ChatClientConfig;
import com.yurakim.readingtrace.ai.dto.ChatResponseDto;
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
    public ChatResponseDto getResponseFromChatModel(UserMessageDto umDto, String model) {
        ChatClient client;
        switch (model.toLowerCase()) {
            case "gemini": client = googleGenAiChatClient; break;
            case "chatgpt": client = openAiChatClient; break;
            default: throw new IllegalArgumentException("Unknown model: " + model);
        }

        String bookInfo = String.format("""
                [Book Info] title: %s, author: %s, publisher: %s, publishedDate: %s,
                isbn10: %s, isbn13: %s, lanaguage: %s
                """,
                umDto.getTitle(), umDto.getAuthor(), umDto.getPublisher(), umDto.getPublishedDate(),
                umDto.getIsbn10(), umDto.getIsbn13(), umDto.getLanguage());

        String systemMessage = ChatClientConfig.DEFAULT_SYSTEM_MESSAGE + bookInfo;

        ChatRecord chatRecord = new ChatRecord();
        ChatResponseDto chatResponseDto = null;
        try{
            ChatResponse chatResponse = client.prompt().user(umDto.getUserMessage()).system(systemMessage).call().chatResponse();
            chatRecord.setModel(model);
            chatRecord.setTimestamp(umDto.getTimestamp());
            chatRecord.setUserMessage(umDto.getUserMessage());
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
            chatResponseDto = chatMapper.toChatResponseDto(chatRecord);
            return chatResponseDto;
        }
    }

}
