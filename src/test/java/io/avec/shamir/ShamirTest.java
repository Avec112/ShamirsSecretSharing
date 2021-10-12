package io.avec.shamir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShamirTest {

    private String expected = "My secret!";
    private List<String> shares;

    @BeforeEach
    void setUp() {
        shares = Shamir.getShares(expected, 5, 3);
    }

    @Test
    void testShamir() {
        final String actual = Shamir.getSecret(shares.get(0), shares.get(2), shares.get(4));

        assertEquals(expected, actual);
    }


    @Test
    void testShamirToFewShares() {
        final String actual = Shamir.getSecret(shares.get(0), shares.get(2));

        assertNotEquals(expected, actual);
    }

    @Test
    void testShamirWrongShareEncodedOnce() {

        byte[] wrongShare = "just wrong".getBytes(StandardCharsets.UTF_8);
        String encodeOnce = encode(wrongShare);

        assertThrows(IllegalStateException.class, () ->
                Shamir.getSecret(shares.get(0), shares.get(2), encodeOnce));
    }

    @Test
    void testShamirWrongShareEncodedTwice() {

        byte[] wrongShare = "just wrong".getBytes(StandardCharsets.UTF_8);
        String encodeOnce = encode(wrongShare);
        String encodedTwice = encode(("10+" + encodeOnce).getBytes(StandardCharsets.UTF_8));

        final String actual = Shamir.getSecret(shares.get(0), shares.get(2), encodedTwice);

        assertNotEquals(expected, actual);
    }

    @Test
    void testShamirLargeSecret() {
        expected = longSecret();
        shares = Shamir.getShares(expected, 4, 2);

        final String actual = Shamir.getSecret(shares.get(1), shares.get(3));

        assertEquals(expected, actual);
    }

    private String longSecret() {
        byte[] array = new byte[100000]; // 100 Kb
        new Random().nextBytes(array);
        return encode(array);
    }

    private String encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

}