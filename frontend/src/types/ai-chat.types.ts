export type AiChat = {
  userInput: string;
  assistantOutput: string;
  timestamp: Date;
};

export type ChatResponseDto = {
  timestamp: Date;
  textContent: string;
  promptTokens: number;
  completionTokens: number;
  totalTokens: number;
  messageType: string;
  finishReason: string;
};
