package com.bjss.chrismay;

import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class AnagramsTest {

    @Test
    public void canListAnagrams(){
        var words = Set.of("tried", "tired");
        var anags = Anagrams.anagramize(words);

        assertEquals(1, anags.size());
        assertEquals(words, anags.values().toArray()[0]);
    }

    @Test
    public void normalizeWord(){
        var word = "hello";
        assertEquals("ehllo", Anagrams.normalizeWord(word));
    }
}
