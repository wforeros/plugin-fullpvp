package me.theoldestwilly.fullpvp.utilities.chat;


import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextUtils {
    public TextUtils() {
    }

    public static String formatColor(String input) {
        return StringEscapeUtils.unescapeJava(ChatColor.translateAlternateColorCodes('&', input));
    }

    public static List<String> formatColor(List<String> input) {
        List<String> result = new ArrayList();
        Iterator var2 = input.iterator();

        while(var2.hasNext()) {
            String string = (String)var2.next();
            result.add(formatColor(string));
        }

        return result;
    }
}
