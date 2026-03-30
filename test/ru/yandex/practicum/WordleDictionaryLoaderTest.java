package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.DictionaryLoadingException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {

    private File tempFile;
    private static PrintWriter logger;

    @BeforeAll
    static void beforeAll() {
        StringWriter logWriter = new StringWriter();
        logger = new PrintWriter(logWriter);
    }

    @BeforeEach
    void beforeEach() throws IOException {
        tempFile = File.createTempFile("dict", ".txt");
        tempFile.deleteOnExit();
    }

    @Test
    void shouldLoadOnlyWordsOfGivenLength() throws IOException, DictionaryLoadingException {
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write("дом\n");
            fw.write("кот\n");
            fw.write("привет\n");
            fw.write("искра\n");
            fw.write("пока\n");
        }

        WordleDictionary dict = WordleDictionaryLoader.getWordsArray(5, tempFile.getAbsolutePath(), logger);

        assertEquals(1, dict.getWords().size());
        assertTrue(dict.isContainsWord("искра"));
    }

    @Test
    void shouldThrowExceptionWhenNoWordsOfRequiredLength() throws IOException {
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write("дом\nкот\n");
        }

        assertThrows(DictionaryLoadingException.class,
                () -> WordleDictionaryLoader.getWordsArray(5, tempFile.getAbsolutePath(), logger));
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        assertThrows(DictionaryLoadingException.class,
                () -> WordleDictionaryLoader.getWordsArray(5, "blabla.txt", logger));
    }
}
