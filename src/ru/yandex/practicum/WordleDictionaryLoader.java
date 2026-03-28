package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private static final Charset encoding = StandardCharsets.UTF_8;

    public static WordleDictionary getWordsArray(int wordLength, String fileName, PrintWriter logger) throws DictionaryLoadingException {
        WordleDictionary dict = new WordleDictionary(wordLength, logger);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName, encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() == wordLength) {
                    dict.addWord(line);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadingException("Ошибка чтения файла словаря: " + fileName, e);
        }

        if (dict.getWords().isEmpty()) {
            throw new DictionaryLoadingException("Словарь не содержит слов длины " + wordLength, null);
        }

        return dict;
    }
}
