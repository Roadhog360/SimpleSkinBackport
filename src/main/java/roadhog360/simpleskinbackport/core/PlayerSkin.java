package roadhog360.simpleskinbackport.core;

import net.minecraft.util.ResourceLocation;

public class PlayerSkin {
    private final boolean slim;
    private final String domain;
    private final String skinName;
    /**
     * Lazy loaded and cached. We need to do this since class loading order with mixins is an annoying bitch
     */
    private ResourceLocation resource;

    public PlayerSkin(boolean slim, String domain, String skinName) {
        this.slim = slim;
        this.domain = domain;
        this.skinName = skinName;
    }

    public PlayerSkin(boolean slim, String skinName) {
        this(slim, null, skinName);
    }

    public ResourceLocation getResource() {
        if(resource == null) {
            resource = new ResourceLocation(domain, "textures/entity/player/" + (slim ? "slim" : "wide") + "/" + skinName + ".png");
        }
        return resource;
    }

    public boolean isSlim() {
        return slim;
    }

    public String getDomain() {
        return domain;
    }

    public String getSkinName() {
        return skinName;
    }
}
