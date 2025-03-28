package roadhog360.simpleskinbackport.mixinplugin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import cpw.mods.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.MixinEnvironment;
import roadhog360.simpleskinbackport.Tags;
import roadhog360.simpleskinbackport.configuration.configs.ConfigModCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class SimpleSkinBackportLateMixins implements ILateMixinLoader {

    public static final MixinEnvironment.Side SIDE = MixinEnvironment.getCurrentEnvironment().getSide();

    @Override
    public String getMixinConfig() {
        return "mixins." + Tags.MOD_ID + ".late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if(SIDE == MixinEnvironment.Side.CLIENT) {
            if(loadedMods.contains("TwilightForest")) {
                if(ConfigModCompat.TFgiantSkinSet != null) {
                    mixins.add("twilightforest.MixinRenderTFGiant");
                }
            }
            if(loadedMods.contains("Botania")) {
                mixins.add("botania.MixinClientProxy");
                mixins.add("botania.MixinRenderTileSkullOverride");
            }
        }
        return mixins;
    }
}
