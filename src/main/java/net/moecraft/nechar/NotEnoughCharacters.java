package net.moecraft.nechar;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.towdium.pinin.DictLoader;
import me.towdium.pinin.PinIn;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.vfyjxf.nechar.NechCommand;
import net.vfyjxf.nechar.NechConfig;
import net.vfyjxf.nechar.core.NechCorePlugin;

import java.io.File;
import java.util.function.BiConsumer;


@Mod(modid = NotEnoughCharacters.ID, useMetadata = true)
public class NotEnoughCharacters {
    public static final String ID = "nechar";
    public static final String VERSION = NotEnoughCharacters.class.getPackage().getImplementationVersion();

    public static final PinIn CONTEXT = new PinIn(new CustomDictLoader()).config().accelerate(true).commit();

    private static class CustomDictLoader extends DictLoader.Default {
        @Override
        public void load(BiConsumer<Character, String[]> feed) {
            super.load(feed);
            feed.accept('\ue900', new String[]{"lu2", "jinlu"}); // 钅卢
            feed.accept('\ue901', new String[]{"du4", "jindu"}); //钅杜
            feed.accept('\ue902', new String[]{"xi3", "jinxi"}); //钅喜
            feed.accept('\ue903', new String[]{"bo1", "jinbo"}); //钅波
            feed.accept('\ue904', new String[]{"hei1", "jinhei"}); //钅黑
            feed.accept('\u9fcf', new String[]{"mai4", "jinmai"});//钅麦
            feed.accept('\ue906', new String[]{"da2", "jinda"});//钅达
            feed.accept('\ue907', new String[]{"lun2", "jinlun"});//钅仑
            feed.accept('\u9fd4', new String[]{"ge1", "jinge"});//钅哥
            feed.accept('\u9fed', new String[]{"ni3", "jiner"});//钅尔
            feed.accept('\ue90a', new String[]{"fu1", "jinfu"});//钅夫
            feed.accept('\ue90c', new String[]{"li4", "jinli"});//钅立
            feed.accept('\u9fec', new String[]{"tian2", "shitian"});// 石田
            feed.accept('\u9feb', new String[]{"ao4", "qiao", "aoqi"});//气奥
        }
    }

    public static void onConfigChange() {
        CONTEXT.config()
                .fZh2Z(NechConfig.EnableFZh2Z)
                .fSh2S(NechConfig.EnableFSh2S)
                .fCh2C(NechConfig.EnableFCh2C)
                .fAng2An(NechConfig.EnableFAng2An)
                .fIng2In(NechConfig.EnableFIng2In)
                .fEng2En(NechConfig.EnableFEng2En)
                .fU2V(NechConfig.EnableFU2V)
                .keyboard(NechConfig.KeyboardType.get())
                .commit();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NechCorePlugin.logger.info("Not Enough Characters - v" + VERSION);
        NechConfig.loadConfig(new File(Minecraft.getMinecraft().mcDataDir, "config/NotEnoughCharacters.cfg"));
        onConfigChange();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new NechCommand());
    }
}
