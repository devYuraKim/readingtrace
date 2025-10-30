export type AiChat = {
  userMessage: string;
  assistantMessage: string;
  timestamp: Date;
};

export type ChatResponseDto = {
  chatRecordId: number;
  model: string;
  timestamp: Date;
  userMessage: string;
  assistantMessage: string;
  promptTokens: number;
  completionTokens: number;
  totalTokens: number;
  finishReason: string;
  error: string;
};
