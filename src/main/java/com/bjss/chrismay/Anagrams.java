package com.bjss.chrismay;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

public class Anagrams {
    public static void main(String[] args) throws IOException {

        var words = Files.readAllLines(Path.of("wordlist.txt"), StandardCharsets.ISO_8859_1);

        long start = System.currentTimeMillis();
        var anagrams = anagramize(words);
        long duration = System.currentTimeMillis() - start;

        System.out.println("Computed anagrams in " + duration + " ms");

        System.out.println("Size of anagrams list: " + anagrams.size());

        var most = anagrams.values().stream().max(comparingInt(Set::size)).orElse(emptySet());
        System.out.println("Largest group of anagrams: (" + most.size() + ") " + most);

        var longestKey = anagrams.keySet().stream().max(comparingInt(String::length)).orElse("");
        System.out.println("Longest anagram words: " + anagrams.get(longestKey));

    }

    public static Map<String, Set<String>> anagramize(Collection<String> words) {
        Map<String, Set<String>> allWords = new ConcurrentHashMap<>(words.size());


        words.parallelStream().forEach(word -> {
            var normalized = normalizeWord(word);

            allWords.compute(normalized, (k, v) -> v == null ? setOf(word) : addTo(v, word));
        });


        return allWords.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static String normalizeWord(String word) {
        var letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public static Set<String> setOf(String word) {
        Set<String> s =  new HashSet<>();
        s.add(word);
        return s;
    }

    public static Set<String> addTo(Set<String> target, String word) {
        target.add(word);
        return target;
    }

}

