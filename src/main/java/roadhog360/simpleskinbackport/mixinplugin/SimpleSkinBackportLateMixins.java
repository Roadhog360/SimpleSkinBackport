package roadhog360.simpleskinbackport.mixinplugin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import roadhog360.simpleskinbackport.SimpleSkinBackport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class SimpleSkinBackportLateMixins implements ILateMixinLoader {

    public static final MixinEnvironment.Side SIDE = MixinEnvironment.getCurrentEnvironment().getSide();

    @Override
    public String getMixinConfig() {
        return "mixins." + SimpleSkinBackport.MODID + ".late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if(loadedMods.contains("TwilightForest")) {
            if(SIDE == MixinEnvironment.Side.CLIENT) {
                mixins.add("MixinRenderTFGiant");
            }
        }
        return mixins;
    }
}
