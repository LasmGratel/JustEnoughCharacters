package net.vfyjxf.nechar.transform.transformers;

import cpw.mods.fml.common.Loader;
import java.util.Iterator;
import net.vfyjxf.nechar.NechConfig;
import net.vfyjxf.nechar.core.NechCorePlugin;
import net.vfyjxf.nechar.transform.Transformer;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformerNEI extends Transformer.Default {

  @Override
  public boolean accepts(String name) {
    boolean loading = name.equals("mezz.jei.search.PrefixInfo");
    if (loading) {
      boolean isNei = Loader.instance().getModList()
          .stream()
          .anyMatch(mod -> mod.getModId().equals("NotEnoughItems"));
      if (!isNei) return false;
    }
    return NechConfig.enableNEI && loading;
  }

  @Override
  public void transform(ClassNode n) {
    NechCorePlugin.logger.info("Transforming class " + n.name + " for NEI integration.");
    Transformer.findMethod(n, "<clinit>").ifPresent(methodNode ->
        transformInvokeLambda(methodNode,
            "mezz/jei/search/GeneralizedSuffixTree",
            "<init>",
            "()V",
            "net/vfyjxf/nechar/utils/Match$FakeTree",
            "<init>",
            "()V"
        ));
    if (NechConfig.enableForceQuote) Transformer.findMethod(n, "parseSearchToken").ifPresent(methodNode -> {
      InsnList list = methodNode.instructions;

      for (int i = 0; i < list.size(); i++) {
        AbstractInsnNode node = list.get(i);
        if (node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
          MethodInsnNode methodInsn = (MethodInsnNode) node;
          if ("java/util/regex/Pattern".equals(methodInsn.owner) && "matcher".equals(methodInsn.name) && "(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;".equals(methodInsn.desc)) {
            list.insert(node.getPrevious(), new MethodInsnNode(Opcodes.INVOKESTATIC,
                "net/vfyjxf/nechar/utils/Match", "wrap",
                "(Ljava/lang/String;)Ljava/lang/String;", false));
          }
        }
      }
    });
  }

  private static boolean transformInvokeLambda(
      MethodNode method, String owner, String name, String desc,
      String newOwner, String newName, String newDesc
  ) {
    boolean ret = false;
    Iterator<AbstractInsnNode> i = method.instructions.iterator();
    while (i.hasNext()) {
      AbstractInsnNode node = i.next();
      int op = node.getOpcode();
      if (node instanceof InvokeDynamicInsnNode && op == Opcodes.INVOKEDYNAMIC) {
        InvokeDynamicInsnNode insnNode = ((InvokeDynamicInsnNode) node);
        if (insnNode.bsmArgs[1] instanceof Handle) {
          Handle h = ((Handle) insnNode.bsmArgs[1]);
          if (!(!h.getOwner().equals(owner) || !h.getName().equals(name) || !h.getDesc().equals(desc))) {
            insnNode.bsmArgs[1] = new Handle(h.getTag(), newOwner, newName, newDesc);
            ret = true;
          }
        }
      }
    }
    return ret;
  }

}
