package roadhog360.simpleskinbackport.mixinplugin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@LateMixin
public class SimpleSkinBackportLateMixins implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.ssb.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        return mixins;
    }
}
