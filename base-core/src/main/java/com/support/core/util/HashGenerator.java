package com.support.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class HashGenerator {
    private static final Logger LOGGER = LogManager.getLogger(HashGenerator.class);
    private static final String HASH_ALGORITHM = "MD5";
    public static String hash(String value) {
        MessageDigest md = tryInitializeDigest();
        update(md, value);
        return getResult(md);
    }
    public static String hash(List<String> values) {
        Collections.sort(values);
        MessageDigest md = tryInitializeDigest();
        values.forEach(value -> update(md, value));
        return getResult(md);
    }

    private static MessageDigest tryInitializeDigest() {
        try {
            return initializeDigest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Cant generate hash: " + e.getMessage());
            throw new RuntimeException("Cant generate hash", e);
        }
    }
    private static MessageDigest initializeDigest() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.reset();
        return md;
    }
    private static void update(MessageDigest md, String value)
    {
        byte[] bytes = value.getBytes();
        md.update(bytes);
    }
    private static String getResult(MessageDigest md)
    {
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(16);
    }
}
