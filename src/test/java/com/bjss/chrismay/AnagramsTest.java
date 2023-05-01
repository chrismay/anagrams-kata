package com.bjss.chrismay;

import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AnagramsTest {

    @Test
    public void canListAnagrams() {
        var words = Set.of("tried", "tired", "ohms", "mohs", "test");
        var anags = Anagrams.anagramize(words);

        // expect to get back two sets of anagrams; tried/tired and ohms/mohs. "test"
        // has no anagrams so is not included in the result
        assertEquals(2, anags.size());

        assertEquals(Set.of("tried", "tired"), anags.get("deirt"));
        assertEquals(Set.of("ohms", "mohs"), anags.get("hmos"));

    }

    @Test
    public void canListWhenNoAnagams(){
        var anags = Anagrams.anagramize(Set.of("one", "two"));
        assertEquals(0, anags.size());
    }


    @Test
    public void normalizeWord() {
        var word = "hello";
        assertEquals("ehllo", Anagrams.normalizeWord(word));
    }

    @Test
    public void anagramsAreKeyedByNormalisedForm() {
        var words = Set.of("tried", "tired");
        var anags = Anagrams.anagramize(words);
        var expectedNormalisedForm = "deirt";

        assertEquals(words, anags.get(expectedNormalisedForm));
    }

    @Test
    public void quoteMarksAreSignificant() {

        // Quote marks count in the formation of an anagram, so these two are not
        // anagrams of each other
        var words = Set.of("ohms", "ohm's");
        var anags = Anagrams.anagramize(words);

        assertEquals(0, anags.size());
    }

    @Test
    public void accentsAreSignificant() {

        // Accents count in the formation of an anagram, so these two are not anagrams
        // of each other
        var words = Set.of("peat", "paté");
        var anags = Anagrams.anagramize(words);

        assertEquals(0, anags.size());

    }

}
