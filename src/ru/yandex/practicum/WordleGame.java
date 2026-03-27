package ru.yandex.practicum;

import java.util.*;

public class WordleGame {
    private final String answer;
    private int stepsLeft;
    private final WordleDictionary dictionary;
    private boolean win = false;

    private final List<String> previousGuesses = new ArrayList<>();
    private final List<String> previousHints = new ArrayList<>();
    private final Set<String> suggestedWords = new HashSet<>();

    public WordleGame(int steps, WordleDictionary dictionary) {
        this.stepsLeft = steps;
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
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
        String hint = getHint(word);
        previousGuesses.add(word);
        previousHints.add(hint);
        stepsLeft--;

        if (word.equals(answer)) {
            win = true;
        }
        return hint;
    }

    public String getHint(String word) {
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
            return dictionary.getRandomWord();
        }

        Random rand = new Random();
        String hint = possible.get(rand.nextInt(possible.size()));
        suggestedWords.add(hint);
        return hint;
    }

    private boolean isConsistentWithAllGuesses(String candidate) {
        for (int i = 0; i < previousGuesses.size(); i++) {
            String guess = previousGuesses.get(i);
            String actualHint = previousHints.get(i);
            String candidateHint = computeHintForCandidate(guess, candidate);
            if (!candidateHint.equals(actualHint)) {
                return false;
            }
        }
        return true;
    }

    private String computeHintForCandidate(String guess, String candidate) {
        int n = guess.length();
        char[] hint = new char[n];
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : candidate.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < n; i++) {
            char letter = guess.charAt(i);
            if (letter == candidate.charAt(i)) {
                hint[i] = '+';
                freq.put(letter, freq.get(letter) - 1);
            }
        }

        for (int i = 0; i < n; i++) {
            if (hint[i] == '+') continue;
            char letter = guess.charAt(i);
            if (freq.getOrDefault(letter, 0) > 0) {
                hint[i] = '^';
                freq.put(letter, freq.get(letter) - 1);
            } else {
                hint[i] = '-';
            }
        }
        return new String(hint);
    }

    public boolean isCorrectPlayerWord(String word) {
        String normalized = dictionary.getNormalizeWord(word);
        return normalized.length() == dictionary.getWordLength() &&
                dictionary.isContainsWord(normalized);
    }
}