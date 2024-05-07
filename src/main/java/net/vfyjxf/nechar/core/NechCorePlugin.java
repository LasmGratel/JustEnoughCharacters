package net.vfyjxf.nechar.core;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.vfyjxf.nechar.transform.TransformerRegistry;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NechCorePlugin implements IFMLLoadingPlugin, IFMLCallHook {

    public static final Logger logger = LogManager.getLogger("NotEnoughCharacters");
    public static boolean INITIALIZED = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"net.vfyjxf.nechar.core.NechClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return "net.vfyjxf.nechar.core.NechCorePlugin";
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public Void call() throws Exception {
        TransformerRegistry.getTransformer("some.class");
        NechCorePlugin.INITIALIZED = true;
        return null;
    }
}
