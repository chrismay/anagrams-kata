package com.bjss.chrismay;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

public class Anagrams {
    public static void main(String[] args) throws IOException {

        var words = Files.readAllLines(Path.of("wordlist.txt"), StandardCharsets.ISO_8859_1);

        var anagrams = anagramize(words);

        System.out.println("Size of anagrams list: " + anagrams.size());

        var most = anagrams.values().stream().max(comparingInt(Set::size)).orElse(emptySet());
        System.out.println("Largest group of anagrams: (" + most.size() + ") " + most);

        var longestKey = anagrams.keySet().stream().max(comparingInt(String::length)).orElse("");
        System.out.println("Longest anagram words: " + anagrams.get(longestKey));

    }

    public static Map<String, Set<String>> anagramize(Collection<String> words) {
        Map<String, Set<String>> allWords = new HashMap<>(words.size());

        for (String word : words) {
            var normalized = normalizeWord(word);

            allWords.compute(normalized, (k, v) -> v == null ? setOf(word) : addTo(v, word));

        }
        return allWords.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String normalizeWord(String word) {
        return Arrays.stream(word.split("")).sorted().collect(Collectors.joining());
    }

    public static Set<String> setOf(String word) {
        return new HashSet<>(List.of(word));
    }

    public static Set<String> addTo(Set<String> target, String word) {
        target.add(word);
        return target;
    }

}

