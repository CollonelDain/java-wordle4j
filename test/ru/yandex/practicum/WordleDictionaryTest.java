package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {
    private WordleDictionary dict;

    @BeforeEach
    void beforeEach() {
        StringWriter logWriter = new StringWriter();
        PrintWriter logger = new PrintWriter(logWriter);
        dict = new WordleDictionary(5, logger);
    }

    @Test
    void shouldNormalizeAndAddWord() {
        dict.addWord("  ИСКРА  ");
        dict.addWord("АктёР");
        dict.addWord("йодид");
        List<String> words = dict.getWords();
        assertEquals(3, words.size());
        assertTrue(words.contains("искра"));
        assertTrue(words.contains("актер"));
        assertTrue(words.contains("йодид"));
    }

    @Test
    void shouldNotAddDuplicateWord() {
        dict.addWord("аббат");
        dict.addWord("аббат");
        assertEquals(1, dict.getWords().size());
    }

    @Test
    void shouldCheckWordExistenceAfterNormalization() {
        dict.addWord("аббат");
        assertTrue(dict.isContainsWord("АББАТ"));
        assertFalse(dict.isContainsWord("абвер"));
    }

    @Test
    void shouldReturnCopyOfWordsList() {
        dict.addWord("слово");
        List<String> words = dict.getWords();
        words.clear();
        assertEquals(1, dict.getWords().size());
    }
}
