package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.GameLogicException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
*/
public class WordleDictionary {

    private final List<String> words;
    private final int wordLength;
    private final PrintWriter logger;

    public WordleDictionary(int wordLength, PrintWriter logger) {
        this.words = new ArrayList<>();
        this.wordLength = wordLength;
        this.logger = logger;
    }

    public void addWord(String word) {
        String normalized = getNormalizeWord(word);
        if (isCorrect(normalized)) {
            words.add(normalized);
        }
    }

    private boolean isCorrect(String word) {
        return word.length() == getWordLength() && !words.contains(word);
    }

    public String getNormalizeWord(String word) {
        if (word == null) return "";
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            logger.println("Попытка получить случайное слово из пустого словаря");
            throw new GameLogicException("Словарь пуст");
        }
        Random random = new Random();
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
