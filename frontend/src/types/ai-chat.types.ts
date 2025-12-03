export type AiChat = {
  userMessage: string;
  assistantMessage: string;
  timestamp: Date;
};

export type ChatRecordDto = {
  chatRecordId: number;
  userId: number;
  bookId: number;
  chatModel: string;
  timestamp: Date;
  userMessage: string;
  assistantMessage: string;
  promptTokens: number;
  completionTokens: number;
  totalTokens: number;
  finishReason: string;
  isSuccess: boolean;
  error: string;
  isBookmarked: boolean;
  isDeleted: boolean;
};
