package me.theoldestwilly.fullpvp.utilities;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class JavaUtils {
    public static final Pattern ALPHANUMERIC_REMOVER = Pattern.compile("[^A-Za-z0-9]");
    private static final CharMatcher CHAR_MATCHER_ASCII;
    private static final Pattern UUID_PATTERN;

    public JavaUtils() {
    }

    public static String showDoubleWith2Decimals (Double number) {
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(number);
    }

    public static boolean isUUID(String string) {
        return UUID_PATTERN.matcher(string).find();
    }

    public static boolean isAlphanumeric(String string) {
        return CHAR_MATCHER_ASCII.matchesAllOf(string);
    }

    public static boolean containsIgnoreCase(Iterable<? extends String> elements, String string) {
        Iterator var2 = elements.iterator();

        String element;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            element = (String)var2.next();
        } while(!StringUtils.containsIgnoreCase(element, string));

        return true;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return ((Comparable)o1.getValue()).compareTo(o2.getValue());
            }
        });
        Map<K, V> result = new LinkedHashMap();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            Map.Entry<K, V> entry = (Map.Entry)var3.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static String formalFormat(String message) {
        message = message.toLowerCase();
        String[] strings = message.split(" ");
        String prettyString = "";
        int counter = 1;
        String[] var5 = strings;
        int var6 = strings.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            char[] characters = s.toCharArray();

            for(int i = 0; i < characters.length; ++i) {
                if (i == 0) {
                    prettyString = prettyString + Character.toUpperCase(characters[0]);
                } else {
                    prettyString = prettyString + characters[i];
                }
            }

            if (counter != strings.length) {
                prettyString = prettyString + " ";
            }

            ++counter;
        }

        return prettyString;
    }

    public static String format(Number number) {
        return format(number, 5);
    }

    public static String format(Number number, int decimalPlaces) {
        return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
    }

    public static String format(Number number, int decimalPlaces, RoundingMode roundingMode) {
        Preconditions.checkNotNull(number, "The number cannot be null");
        return (new BigDecimal(number.toString())).setScale(decimalPlaces, roundingMode).stripTrailingZeros().toPlainString();
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd) {
        return andJoin(collection, delimiterBeforeAnd, ", ");
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection != null && !collection.isEmpty()) {
            List<String> contents = new ArrayList(collection);
            String last = (String)contents.remove(contents.size() - 1);
            StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join(contents));
            if (delimiterBeforeAnd) {
                builder.append(delimiter);
            }

            return builder.append(" and ").append(last).toString();
        } else {
            return "";
        }
    }

    public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static Float tryParseFloat(String string) {
        try {
            return Float.parseFloat(string);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static Long tryParseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static UUID tryParseUUID(String input) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static String setFormat(Long value) {
        return DurationFormatUtils.formatDuration(value, (value >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") + "mm:ss");
    }

    static {
        CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
        UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
    }
}
