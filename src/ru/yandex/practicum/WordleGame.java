package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    private final String answer;
    private int stepsLeft;
    private final WordleDictionary dictionary;
    private boolean win = false;
    private final PrintWriter logger;

    private final List<String> previousGuesses = new ArrayList<>();
    private final List<String> previousHints = new ArrayList<>();
    private final Set<String> suggestedWords = new HashSet<>();

    public WordleGame(int steps, WordleDictionary dictionary, PrintWriter logger) {
        this.stepsLeft = steps;
        this.dictionary = dictionary;
        this.logger = logger;
        this.answer = dictionary.getRandomWord();
        logger.println("Загадано слово: " + answer);
    }

    public String getAnswer() {
        return answer;
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public boolean isWin() {
        return win;
    }

    public String submitGuess(String word) {
        String hint = getHint(word, answer);
        previousGuesses.add(word);
        previousHints.add(hint);
        stepsLeft--;

        logger.println("Ход: " + word + " #" + hint + ", осталось попыток: " + stepsLeft);

        if (word.equals(answer)) {
            win = true;
            logger.println("Игрок победил!");
        }
        return hint;
    }

    public String getHint(String word, String answer) {
        int n = word.length();
        char[] hint = new char[n];
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : answer.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < n; i++) {
            char letter = word.charAt(i);
            if (letter == answer.charAt(i)) {
                hint[i] = '+';
                freq.put(letter, freq.get(letter) - 1);
            }
        }

        for (int i = 0; i < n; i++) {
            if (hint[i] == '+') continue;
            char letter = word.charAt(i);
            if (freq.getOrDefault(letter, 0) > 0) {
                hint[i] = '^';
                freq.put(letter, freq.get(letter) - 1);
            } else {
                hint[i] = '-';
            }
        }

        return new String(hint);
    }

    public String getHintWord() {
        List<String> possible = new ArrayList<>();

        if (previousGuesses.isEmpty()) {
            for (String w : dictionary.getWords()) {
                if (!suggestedWords.contains(w)) {
                    possible.add(w);
                }
            }
        } else {
            for (String candidate : dictionary.getWords()) {
                if (isConsistentWithAllGuesses(candidate)) {
                    possible.add(candidate);
                }
            }
            possible.removeAll(previousGuesses);
            possible.removeAll(suggestedWords);
        }

        if (possible.isEmpty()) {
            suggestedWords.clear();
            if (previousGuesses.isEmpty()) {
                possible.addAll(dictionary.getWords());
            } else {
                for (String candidate : dictionary.getWords()) {
                    if (isConsistentWithAllGuesses(candidate)) {
                        possible.add(candidate);
                    }
                }
            }
        }

        if (possible.isEmpty()) {
            logger.println("Не найдено подходящих слов для подсказки, возвращаем случайное");
            return dictionary.getRandomWord();
        }

        Random rand = new Random();
        String hint = possible.get(rand.nextInt(possible.size()));
        suggestedWords.add(hint);
        logger.println("Подсказка: " + hint);
        return hint;
    }

    private boolean isConsistentWithAllGuesses(String candidate) {
        for (int i = 0; i < previousGuesses.size(); i++) {
            String guess = previousGuesses.get(i);
            String actualHint = previousHints.get(i);
            String candidateHint = getHint(guess, candidate);
            if (!candidateHint.equals(actualHint)) {
                return false;
            }
        }
        return true;
    }

    public void validateWord(String word) throws WordNotFoundInDictionary {
        String normalized = dictionary.getNormalizeWord(word);
        if (normalized.length() != dictionary.getWordLength()) {
            throw new WordNotFoundInDictionary("Слово должно содержать " + dictionary.getWordLength() + " букв");
        }
        if (!dictionary.isContainsWord(normalized)) {
            throw new WordNotFoundInDictionary("Слово отсутствует в словаре");
        }
    }
}