package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleDictionary dict;
    private WordleGame game;
    private PrintWriter logger;

    @BeforeEach
    void beforeEach() {
        StringWriter logWriter = new StringWriter();
        logger = new PrintWriter(logWriter);
        dict = new WordleDictionary(5, logger);
        dict.addWord("абзац");
        game = new WordleGame(6, dict, logger);
    }

    @Test
    void shouldInitializeGameCorrectly() {
        assertEquals(6, game.getStepsLeft());
        assertFalse(game.isWin());
        assertEquals("абзац", game.getAnswer());
    }

    @Test
    void shouldThrowExceptionWhenWordNotInDictionary() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.validateWord("абвгд"));
    }

    @Test
    void shouldValidateCorrectWord() {
        assertDoesNotThrow(() -> game.validateWord("абзац"));
    }

    @Test
    void shouldReturnHintAndDecreaseStepsOnValidGuess() {
        String hint = game.submitGuess("аббат");
        assertEquals(5, hint.length());
        assertEquals(5, game.getStepsLeft());
        assertFalse(game.isWin());
    }

    @Test
    void shouldWinWhenWordMatches() {
        String hint = game.submitGuess("абзац");
        assertEquals("+++++", hint);
        assertTrue(game.isWin());
        assertEquals(5, game.getStepsLeft());
    }
}