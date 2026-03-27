package ru.yandex.practicum;

import java.io.IOException;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILENAME = "words_ru.txt";
    private static final int WORD_LENGTH = 5;
    private static final int MAX_STEPS = 6;

    public static void main(String[] args) throws IOException {
        System.out.println("ДОБРО ПОЖАЛОВАТЬ В ИГРУ \"Wordle\"");
        System.out.printf("""
                ВАМ ПРЕДСТОИТ ОТГАДАТЬ %d-ЗНАЧНОЕ СЛОВО, ЗАГАДАННОЕ КОМПЬЮТЕРОМ.
                НА ОТГАДЫВАНИЕ ВАМ ДАЕТСЯ %d ПОПЫТОК.
                ЕСЛИ ВВЕДЕНО НЕКОРРЕКТНОЕ СЛОВО, КОЛИЧЕСТВО ПОПЫТОК НЕ УМЕНЬШАЕТСЯ.
                В СЛУЧАЕ, ЕСЛИ ВО ВРЕМЯ ИГРЫ ВЫ ХОТИТЕ ПОЛУЧИТЬ ПОДСКАЗКУ, НАЖМИТЕ КЛАВИШУ ENTER, НЕ ВВОДЯ СЛОВО.%n
                """, WORD_LENGTH, MAX_STEPS);

        Scanner scanner = new Scanner(System.in);

        WordleDictionary wordBank = WordleDictionaryLoader.getWordsArray(WORD_LENGTH, DICTIONARY_FILENAME);
        WordleGame game = new WordleGame(MAX_STEPS, wordBank);


        while (game.getStepsLeft() > 0 && !game.isWin()) {
            System.out.print("ВВЕДИТЕ СЛОВО: ");
            String playerInput = scanner.nextLine();

            if (playerInput.isBlank()) {
                String hintWord = game.getHintWord();
                System.out.println("ПОДСКАЗКА: " + hintWord);
                continue;
            }

            if (!game.isCorrectPlayerWord(playerInput)) {
                System.out.println("НЕКОРРЕКТНОЕ СЛОВО. Попробуйте ещё раз.");
                continue;
            }

            String hint = game.submitGuess(playerInput);

            if (game.isWin()) {
                System.out.printf("""
                        ПОЗДРАВЛЯЕМ! ВЫ ВЫИГРАЛИ!
                        СЛОВО "%s" БЫЛО ОТГАДАНО ЗА %d ПОПЫТОК.%n
                        """, game.getAnswer(), MAX_STEPS - game.getStepsLeft());
                return;
            } else {
                System.out.println(hint);
            }
        }

        System.out.printf("""
                К СОЖАЛЕНИЮ, ВЫ НЕ СМОГЛИ ОТГАДАТЬ СЛОВО.
                ЗАГАДАННОЕ СЛОВО - "%s".
                """, game.getAnswer());
    }
}