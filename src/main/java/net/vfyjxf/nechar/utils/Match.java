/*
 * 基于Towdium的JustEnoughCharacters(https://github.com/Towdium/JustEnoughCharacters/blob/1.12.0/src/main/java/me/towdium/jecharacters/transform/transformers/TransformerRegExp.java)
 * 原文件协议为MIT
 */

package net.vfyjxf.nechar.utils;

import static me.towdium.pinin.searchers.Searcher.Logic.CONTAIN;
import static net.moecraft.nechar.NotEnoughCharacters.CONTEXT;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.towdium.pinin.searchers.TreeSearcher;
import mezz.jei.search.GeneralizedSuffixTree;
import net.vfyjxf.nechar.NechConfig;
import net.vfyjxf.nechar.core.NechCorePlugin;

public class Match {

    static final Pattern p = Pattern.compile("a");

    public static boolean isChinese(CharSequence s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (isChinese(s.charAt(i))) return true;
        }
        return false;
    }

    public static boolean isChinese(char i) {
        return (0x3000 <= i && i < 0xA000) || (0xE900 <= i && i < 0xEA00);
    }

    public static boolean contains(String s, CharSequence cs) {
        //            NotEnoughCharacters.logger.info("contains(" + s + ',' + cs + ")->" + b);
        return CONTEXT.contains(s, cs.toString());
    }


    public static Matcher matcher(Pattern test, CharSequence name) {
        boolean result;
        if ((test.flags() & Pattern.CASE_INSENSITIVE) != 0 || (test.flags() & Pattern.UNICODE_CASE) != 0) {
            result = matches(name.toString().toLowerCase(), test.toString().toLowerCase());
        } else {
            result = matches(name.toString(), test.toString());
        }
        return result ? p.matcher("a") : p.matcher("");
    }

    public static boolean matches(String s1, String s2) {
        boolean start = s2.startsWith(".*");
        boolean end = s2.endsWith(".*");
        if (start && end && s2.length() < 4) end = false;
        if (start || end) s2 = s2.substring(start ? 2 : 0, s2.length() - (end ? 2 : 0));
        return contains(s1, s2);
    }

    static Set<TreeSearcher<?>> searchers = Collections.newSetFromMap(new WeakHashMap<>());

    public static <T> TreeSearcher<T> searcher() {
        TreeSearcher<T> ret = new TreeSearcher<>(CONTAIN, CONTEXT);
        searchers.add(ret);
        return ret;
    }


    public static class FakeTree<T> extends GeneralizedSuffixTree<T> {
        TreeSearcher<T> tree = searcher();

        @Override
        public void getSearchResults(String word, Set<T> results) {
            if (NechConfig.enableVerbose) {
                NechCorePlugin.logger.info("FakeTree:search(" + word + ')');
            }
            results.addAll(tree.search(word));
        }

        @Override
        public void put(String key, T value) {
            if (NechConfig.enableVerbose) {
                NechCorePlugin.logger.info("FakeTree:put(" + key + ',' + value + ')');
            }
            tree.put(key, value);
        }

        @Override
        public void getAllElements(Set<T> results) {
            results.addAll(tree.search(""));
        }
    }
}
