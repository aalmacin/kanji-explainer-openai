package com.raidrin.kanjiexplainer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raidrin.kanjiexplainer.controllers.NoKanjiFoundException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KanjiDetailsService {
    @Autowired
    private OpenAiService openAiService;
    private static final String SYSTEM_TASK_MESSAGE = """
                You are are an API server that provides information about kanji in JSON format.
                
                Don't say anything else. Respond only with the JSON.
                
                The user will send you a kanji character and you will respond with information about that character.
                
                Respond in JSON format, including the following fields:
                - kanji (string): the kanji analyzed
                - onyomiReadings ({kana: string, romaji: string}[]): common kana format and romaji for each item
                - kunyumiReadings ({kana: string, romaji: string}[]): common kana format for each item
                - radicals (string[]): each radical part of the kanji character
                - englishTranslations (string[]): equivalent english translations
                - history (string): the history of the kanji character
                - commonUsages ({usage: string, kana: string, romaji: string}[]): Show the common usages of the character
                - mnemonicSuggestions (string[]): Be creative and create different mnemonic suggestions and stories based on the radicals, english sound, etc.
                
                Don't add anything else in the end after you respond with the JSON.
            """;

    public KanjiDetails getDetails(String kanjiCharacter) throws JsonProcessingException, NoKanjiFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                getKanjiDetails(kanjiCharacter),
                KanjiDetails.class
        );
    }

    private String getKanjiDetails(String kanji) throws NoKanjiFoundException {
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent(SYSTEM_TASK_MESSAGE);

        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent("Show me details for " + kanji);

        ChatCompletionRequest chatCompletionRequest =
                ChatCompletionRequest.builder()
                        .model("gpt-3.5-turbo")
                        .messages(
                                List.of(
                                        systemMessage,
                                        userMessage
                                )
                        )
                        .temperature(1.0)
                        .build();
        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);

        List<ChatCompletionChoice> chatCompletionChoices = chatCompletionResult.getChoices();
        if (chatCompletionChoices.size() == 0) {
            throw new NoKanjiFoundException("Expected at least 1 choice");
        }
        StringBuilder sb = new StringBuilder();
        chatCompletionChoices.forEach(
                choice -> {
                    sb.append(choice.getMessage().getContent());
                }
        );
        return sb.toString();
    }
}
