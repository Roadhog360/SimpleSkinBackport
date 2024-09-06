package roadhog360.simpleskinbackport.configuration.configs;

import roadhog360.simpleskinbackport.configuration.ConfigBase;
import roadhog360.simpleskinbackport.core.DefaultPlayerSkin;

import java.io.File;

public class ConfigModCompat extends ConfigBase {

    public static DefaultPlayerSkin.Set TFgiantSkinSet;

    static final String catMisc = "misc";

    public ConfigModCompat(File file) {
        super(file);

        getCategory(catMisc).setComment("Settings for mod-specific patches.");

        configCats.add(getCategory(catMisc));
    }


    //TODO: Move Iron Chest checks here
    @Override
    protected void syncConfigOptions() {
        TFgiantSkinSet = DefaultPlayerSkin.Set.valueOfOrNull(
            getString("TwilightForest:giantSkinSet", catMisc, DefaultPlayerSkin.NONE,
                    """
                    What default skin set should we use for Twilight Forest giants? NONE means they copy the client player's skin and arms
                    The other values set a skin randomly assigned based on the Giant's UUID. See the main config for more info on these.""", ConfigBase.getSkinReplacementModes(true))
        );
    }
}
