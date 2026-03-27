package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private static final Charset encoding = StandardCharsets.UTF_8;

    public static WordleDictionary getWordsArray(int wordLength, String fileName) throws IOException {
        WordleDictionary dict = new WordleDictionary(wordLength);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName, encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() == wordLength) {
                    dict.addWord(line);
                }
            }
        }
        return dict;
    }
}
