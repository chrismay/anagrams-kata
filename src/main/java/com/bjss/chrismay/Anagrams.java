package com.bjss.chrismay;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingInt;

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

        // Map to hold normalizedValue=>Set<words which normalize to that value>
        Map<String, Set<String>> allWords = new ConcurrentHashMap<>(words.size());

        // Map holding _only_ the normalizedValues that have more than one matching word.
        Map<String, Set<String>> anagramsOnly = new ConcurrentHashMap<>(words.size());

        words.parallelStream().forEach(word ->
            allWords.compute(normalizeWord(word), (k, v) -> v == null ? setOf(word) : addTo(v, word,k,anagramsOnly))
        );

        return anagramsOnly;
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

    /**
     * Add a word to a set of words, and then add the resulting set to a map, based on the supplied key (overwriting any prior value)
     *
     * @return the updated set of words.
     */
    public static Set<String> addTo(Set<String> target, String word, String key, Map<String,Set<String>> anagramsOnly) {
        target.add(word);
        anagramsOnly.put(key, target);
        return target;
    }

}

