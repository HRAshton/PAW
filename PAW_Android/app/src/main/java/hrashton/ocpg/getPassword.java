package hrashton.ocpg;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 * Created by hrashton on 24.07.15.
 */
public class getPassword {
    final static ArrayList<Character> digits = new ArrayList<Character>(10);
    final static ArrayList<Character> lowers = new ArrayList<Character>(26);
    final static ArrayList<Character> uppers = new ArrayList<Character>(26);
    final static ArrayList<Character> specs  = new ArrayList<Character>(1);
    final static Pattern pattern = Pattern.compile("(.*?\\.)*(.+)\\..+");


    public static void get(final String service, final String key, StringBuffer password) {
        for (char c : "0123456789".toCharArray())
            digits.add(c);
        for (char c : "abcdefghigklmnopqrstuvwxyz".toCharArray())
            lowers.add(c);
        for (char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())
            uppers.add(c);
        specs.add('_');

        final String alphabets = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

        final Matcher mr = pattern.matcher(service);
        final String serviceName = mr.replaceAll("$2").toLowerCase();
        final String hashsum = MD5.md5(serviceName + key);
        final byte length = (byte)(Short.valueOf(hashsum.substring(29, 31), 16) % 2 + 11);

        ArrayList<ArrayList<Character>> few_chars = new ArrayList<ArrayList<Character>>(2);

        for (char i = 0; i < 2 * length; i += 2) {
            short num = Short.valueOf(hashsum.substring(i, i + 2), 16);
            password.append(alphabets.charAt(num % 63));
        }

        if (countOf(password, digits) < 2)
            few_chars.add(digits);
        if (countOf(password, lowers) < 2)
            few_chars.add(lowers);
        if (countOf(password, uppers) < 2)
            few_chars.add(uppers);
        if (countOf(password, specs) < 2)
            few_chars.add(specs);

        int i = Integer.valueOf(hashsum.substring(25, 27), 16) % 4;
        for (ArrayList<Character> alphabet : few_chars) {
            while (countOf(password, alphabet) < 2) {
                if (countOf(password, whatAAlphabet(password.charAt(i % length))) > 2) {
                    char new_char = alphabet.get(Short.valueOf(hashsum.substring(24, 26), 16) % alphabet.size());
                    password.setCharAt(i % length, new_char);
                }
                i += 7;
            }
        }
    }

    private static Character countOf(final StringBuffer haystack, final ArrayList<Character> needle) {
        char count = 0;
        for (char symbol : haystack.toString().toCharArray()) {
            if (needle.contains(symbol))
                count++;
        }
        return count;
    }

    private static ArrayList<Character> whatAAlphabet(final Character needle) {
        if (digits.contains(needle)) {
            return digits;
        } else {
            if (lowers.contains(needle)) {
                return lowers;
            } else {
                if (uppers.contains(needle)) {
                    return uppers;
                } else {
                    return specs;
                }
            }
        }
    }
}
