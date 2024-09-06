package roadhog360.simpleskinbackport.configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.MixinEnvironment;
import roadhog360.simpleskinbackport.SimpleSkinBackport;
import roadhog360.simpleskinbackport.configuration.configs.ConfigMain;
import roadhog360.simpleskinbackport.configuration.configs.ConfigModCompat;
import roadhog360.simpleskinbackport.core.DefaultPlayerSkin;
import roadhog360.simpleskinbackport.mixinplugin.SimpleSkinBackportEarlyMixins;

import java.io.File;
import java.util.*;

public abstract class ConfigBase extends Configuration {
    protected final List<ConfigCategory> configCats = new ArrayList<>();
    private static final Set<ConfigBase> CONFIGS = new HashSet<>();

    public static final String configDir = "config" + File.separator + SimpleSkinBackport.MODID + File.separator;

    public static final ConfigBase MAIN = new ConfigMain(createConfigFile("main"));
    public static final ConfigBase MOD_COMPAT = new ConfigModCompat(createConfigFile("modcompat"));

    public ConfigBase(File file) {
        super(file);
        CONFIGS.add(this);
    }

    private static File createConfigFile(String name) {
        return new File(Launch.minecraftHome, configDir + name + ".cfg");
    }

    public static void initializeConfigs() {
        for (ConfigBase config : CONFIGS) {
            config.syncConfig();
        }
    }

    private void syncConfig() {
        syncConfigOptions();

        for (ConfigCategory cat : configCats) {
            if (SimpleSkinBackportEarlyMixins.SIDE == MixinEnvironment.Side.SERVER) {
                if (cat.getName().toLowerCase().contains("client")) {
                    for (Property prop : cat.getOrderedValues()) {
                        cat.remove(prop.getName());
                    }
                }
            }

            if (cat.isEmpty() && !cat.getName().toLowerCase().contains("experiment")) {
                removeCategory(cat);
            }
        }

        if (hasChanged()) {
            save();
        }
    }

    protected abstract void syncConfigOptions();

    /**
     * Used in case we need to wait till later to initialize some config values.
     */
    protected void initValues() {
    }

    public static void postInit() {
        for (ConfigBase config : CONFIGS) {
            config.initValues();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (SimpleSkinBackport.MODID.equals(eventArgs.modID))
            syncConfig();
    }

    protected static String[] getSkinReplacementModes(boolean isNoneAllowed) {
        String[] array = new String[0];
        if(isNoneAllowed) {
            ArrayUtils.add(array, "NONE");
        }
        Arrays.stream(DefaultPlayerSkin.Set.values()).forEachOrdered(mode -> ArrayUtils.add(array, mode.name()));
        return array;
    }

    protected static String getSkinReplacementDescriptions() {
        StringBuilder sb = new StringBuilder();
        DefaultPlayerSkin.Set[] values = DefaultPlayerSkin.Set.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            DefaultPlayerSkin.Set skinSet = values[i];
            sb.append(skinSet.name()).append(": ").append(skinSet.getDescription());
            if(i < valuesLength - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
