package roadhog360.simpleskinbackport.configuration.configs;

import roadhog360.simpleskinbackport.configuration.ConfigBase;
import roadhog360.simpleskinbackport.core.DefaultPlayerSkin;

import java.io.File;

public class ConfigMain extends ConfigBase {

    public static DefaultPlayerSkin.Set defaultSkinSet = DefaultPlayerSkin.Set.ALL_DEFAULTS;
    public static boolean oldSlimArms = false;

    static final String catSkins = "skins";
    static final String catHeads = "heads";

    public ConfigMain(File file) {
        super(file);

        getCategory(catSkins).setComment("Settings for mod-specific patches.");

        configCats.add(getCategory(catSkins));
        configCats.add(getCategory(catHeads));
    }

    @Override
    protected void syncConfigOptions() {
        defaultSkinSet = DefaultPlayerSkin.Set.valueOf(
            getString("defaultSkinSet", catSkins, DefaultPlayerSkin.Set.ALL_DEFAULTS.name(),
                    """
                    What default skin set should we use for players? This only affects players whose skins are not loaded, or do not have a custom skin.
                    Note that players may also set their skin to one of the new defaults in their Minecraft launcher, and that will take precedent over this option.
                    Each set contains the following skins:
                    """ + ConfigBase.getSkinReplacementDescriptions(), ConfigBase.getSkinReplacementModes(false))
        );
        oldSlimArms = getBoolean("oldSlimArms", catSkins, false,
           """
            Before 1.15, slim-armed skins were a half-pixel lower than the player's torso. Set this to true to re-enable that behavior.
            """);
    }
}
