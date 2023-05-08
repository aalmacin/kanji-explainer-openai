package com.raidrin.kanjiexplainer.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanjiDetails {
    private String kanji;
    private List<KanaRomajiPair> onyomiReadings;
    private List<KanaRomajiPair> kunyomiReadings;
    private List<String> radicals;
    private List<String> englishTranslations;
    private String history;
    private List<UsageKanaRomajiPair> commonUsages;
    private List<String> mnemonicSuggestions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KanaRomajiPair {
        private String kana;
        private String romaji;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsageKanaRomajiPair {
        private String usage;
        private String kana;
        private String romaji;
    }
}

