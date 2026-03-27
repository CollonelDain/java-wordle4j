package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
*/
public class WordleDictionary {

    private final List<String> words;
    private final int wordLength;

    public WordleDictionary(int wordLength) {
        this.words = new ArrayList<>();
        this.wordLength = wordLength;
    }

    public void addWord(String word) {
        String normalized = getNormalizeWord(word);
        if (isCorrect(normalized)) {
            words.add(word);
        }
    }

    private boolean isCorrect(String word) {
        return word.length() == this.wordLength && !words.contains(word);
    }

    public String getNormalizeWord(String word) {
        if (word == null) return "";
        return word.trim().toLowerCase().replace("ё", "е");
    }

    //Replace exception
    public String getRandomWord() {
        Random random = new Random();
        if (words.isEmpty()) {
            throw new NullPointerException();
        }
        return words.get(random.nextInt(words.size()));
    }

    public int getWordLength() {
        return wordLength;
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public boolean isContainsWord(String word) {
        return words.contains(getNormalizeWord(word));
    }
}
