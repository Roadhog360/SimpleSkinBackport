package roadhog360.simpleskinbackport.mixinplugin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleSkinBackportLateMixins implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.ssb.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        List<String> mixins = new ArrayList<>();
        if(loadedMods.contains("headcrumbs")) {
            mixins.add("headcrumbs.MixinModelHead");
        }
        return mixins;
    }
}
