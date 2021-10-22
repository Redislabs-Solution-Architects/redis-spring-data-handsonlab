package com.redis.lars.exercise5.bootstrap;

import java.security.SecureRandom;
import org.springframework.util.StringUtils;

public class Utilities {

    /**
     * Fake IBAN generator. Generates a Dutch IBAN with 17 digits of which the
     * numerical ones are generated randomly. A Real Dutch IBAN has 18 digits and
     * the first two (between country and bank code) are a checksum of the remaining
     * ten. We do not generate a checksum either.
     * 
     * @param userName used as a seed to make sure the generated IBAN is always the
     *                 same for a given username
     * @return the generated fake IBAN
     */
    public static final String generateFakeIbanFrom(String userName) {

        if (StringUtils.hasLength(userName)) {
            throw new IllegalArgumentException("Parameter userName should have a value");
        }

        try {
            // Set the seed of the pseudo random generator to the username,
            // so we always get the same sequence for the same username
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(userName.getBytes("UTF-8"));

            StringBuilder builder = new StringBuilder();
            builder.append("NL");
            appendDigits(builder, random, 2);
            builder.append("REBK");
            appendDigits(builder, random, 9);

            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static final void appendDigits(StringBuilder builder, SecureRandom random, int numberOfDigits) {
        for (int i = 0; i < numberOfDigits; i++) {
            builder.append(random.nextInt(10));
        }

    }

    public static final String generatePortfolioIdFrom(String userName)   {
        if (StringUtils.hasLength(userName)) {
            throw new IllegalArgumentException("Parameter userName should have a value");
        }

        try {
            // Set the seed of the pseudo random generator to the username,
            // so we always get the same sequence for the same username
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(userName.getBytes("UTF-8"));

            StringBuilder builder = new StringBuilder();
            builder.append("NL");
            appendDigits(builder, random, 2);
            builder.append("RBINV");
            appendDigits(builder, random, 5);

            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }

}
