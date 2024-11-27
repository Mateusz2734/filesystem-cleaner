package pl.edu.agh.to2.cleaner.similarity;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.cleaner.similarity.DamerauLevenshtein;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DamerauLevenshteinTest {

    @Test
    void damerauLevenshteinDistance_identicalStrings() {
        assertEquals(0, DamerauLevenshtein.distance("aaaaa", "aaaaa"));
    }

    @Test
    void damerauLevenshteinDistance_emptyStrings() {
        assertEquals(0, DamerauLevenshtein.distance("", ""));
    }

    @Test
    void damerauLevenshteinDistance_oneEmptyString() {
        assertEquals(5, DamerauLevenshtein.distance("", "aaaaa"));
    }

    @Test
    void damerauLevenshteinDistance_transposition() {
        assertEquals(1, DamerauLevenshtein.distance("ababab", "ababba"));
    }

    @Test
    void damerauLevenshteinDistance_capitalization() {
        assertEquals(1, DamerauLevenshtein.distance("aaaaa", "Aaaaa"));
    }

    @Test
    void damerauLevenshteinDistance_substitution() {
        assertEquals(1, DamerauLevenshtein.distance("aaaaa", "aaaab"));
    }

    @Test
    void damerauLevenshteinDistance_insertion() {
        assertEquals(1, DamerauLevenshtein.distance("aaaa", "aaaaa"));
    }

    @Test
    void damerauLevenshteinDistance_deletion() {
        assertEquals(1, DamerauLevenshtein.distance("aaaaa", "aaaa"));
    }

    @Test
    void normalizedDamerauLevenshtein_identicalStrings() {
        assertEquals(1.0, DamerauLevenshtein.similarity("aaaaa", "aaaaa"));
    }

    @Test
    void normalizedDamerauLevenshtein_emptyStrings() {
        assertEquals(1.0, DamerauLevenshtein.similarity("", ""));
    }

    @Test
    void normalizedDamerauLevenshtein_oneEmptyString() {
        assertEquals(0.0, DamerauLevenshtein.similarity("", "aaaaa"));
    }

    @Test
    void normalizedDamerauLevenshtein_differentStrings() {
        assertEquals(0.8, DamerauLevenshtein.similarity("aaaaa", "aaaab"));
    }
}