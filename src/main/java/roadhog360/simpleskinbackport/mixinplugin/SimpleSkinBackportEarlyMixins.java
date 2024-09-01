package roadhog360.simpleskinbackport.mixinplugin;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.management.PlayerProfileCache;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleSkinBackportEarlyMixins implements IFMLLoadingPlugin, IEarlyMixinLoader {

    public static final MixinEnvironment.Side side = MixinEnvironment.getCurrentEnvironment().getSide();

    @Override
    public String getMixinConfig() {
        return "mixins.ssb.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        List<String> mixins = new ArrayList<>();
        mixins.add("MixinEntityPlayer");
        if(side == MixinEnvironment.Side.CLIENT) {
            mixins.add("MixinAbstractClientPlayer");
            mixins.add("MixinRenderPlayer");
            mixins.add("MixinImageBufferDownload");
        }
        return mixins;
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
