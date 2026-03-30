package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILENAME = "words_ru.txt";
    private static final String LOG_FILENAME = "game.log";
    private static final int WORD_LENGTH = 5;
    private static final int MAX_STEPS = 6;

    public static void main(String[] args) throws IOException {
        try (PrintWriter logger = new PrintWriter(new FileWriter(LOG_FILENAME, StandardCharsets.UTF_8))) {
            logger.println("=== НАЧАЛО ИГРЫ ===");
            runGame(logger);
            logger.println("=== КОНЕЦ ИГРЫ ===");
        } catch (IOException e) {
            System.err.println("Не удалось создать лог-файл. Игра завершена.");
        }
    }



    private static void runGame(PrintWriter logger) {
        try {
            WordleDictionary wordBank = WordleDictionaryLoader.getWordsArray(WORD_LENGTH, DICTIONARY_FILENAME, logger);
            logger.println("Словарь загружен, слов: " + wordBank.getWords().size());

            WordleGame game = new WordleGame(MAX_STEPS, wordBank, logger);
            Scanner scanner = new Scanner(System.in);

            System.out.println("ДОБРО ПОЖАЛОВАТЬ В ИГРУ \"Wordle\"");
            System.out.printf("""
                    ВАМ ПРЕДСТОИТ ОТГАДАТЬ %d-ЗНАЧНОЕ СЛОВО, ЗАГАДАННОЕ КОМПЬЮТЕРОМ.
                    НА ОТГАДЫВАНИЕ ВАМ ДАЕТСЯ %d ПОПЫТОК.
                    ЕСЛИ ВВЕДЕНО НЕКОРРЕКТНОЕ СЛОВО, КОЛИЧЕСТВО ПОПЫТОК НЕ УМЕНЬШАЕТСЯ.
                    В СЛУЧАЕ, ЕСЛИ ВО ВРЕМЯ ИГРЫ ВЫ ХОТИТЕ ПОЛУЧИТЬ ПОДСКАЗКУ, НАЖМИТЕ КЛАВИШУ ENTER, НЕ ВВОДЯ СЛОВО.%n
                    """, WORD_LENGTH, MAX_STEPS);

            while (game.getStepsLeft() > 0 && !game.isWin()) {
                System.out.print("ВВЕДИТЕ СЛОВО: ");
                String playerInput = scanner.nextLine();

                if (playerInput.isBlank()) {
                    String hintWord = game.getHintWord();
                    System.out.println("ПОДСКАЗКА: " + hintWord);
                    continue;
                }

                try {
                    game.validateWord(playerInput);
                } catch (WordNotFoundInDictionary e) {
                    System.out.println("НЕКОРРЕКТНОЕ СЛОВО: " + e.getMessage() + ". Попробуйте ещё раз.");
                    logger.println("Ошибка ввода: " + e.getMessage() + " (слово: " + playerInput + ")");
                    continue;
                }

                String hint = game.submitGuess(wordBank.getNormalizeWord(playerInput));

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
        } catch (DictionaryLoadingException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
            if (logger != null) {
                logger.println("Ошибка загрузки словаря: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка. Игра завершена.");
            if (logger != null) {
                logger.println("Критическая ошибка: " + e.getMessage());
            }
        }
    }
}