/*
 * 基于Towdium的JustEnoughCharacters(https://github.com/Towdium/JustEnoughCharacters/blob/1.12.0/src/main/java/me/towdium/jecharacters/transform/TransformerRegistry.java)
 * 原文件协议为MIT
 */

package net.vfyjxf.nechar.transform;

import net.vfyjxf.nechar.transform.transformers.TransformerNEI;
import net.vfyjxf.nechar.transform.transformers.TransformerRegExp;
import net.vfyjxf.nechar.transform.transformers.TransformerString;

import java.util.ArrayList;

public class TransformerRegistry {
    public static ArrayList<Transformer.Configurable> configurables = new ArrayList<>();
    public static ArrayList<Transformer> transformers = new ArrayList<>();

    static {
        configurables.add(new TransformerString());
        configurables.add(new TransformerRegExp());
        transformers.add(new TransformerNEI());
        transformers.addAll(configurables);
    }

    public static ArrayList<Transformer> getTransformer(String name) {
        ArrayList<Transformer> ret = new ArrayList<>();
        for (Transformer t : transformers) {
            if (t.accepts(name))
                ret.add(t);
        }
        return ret;
    }
}
