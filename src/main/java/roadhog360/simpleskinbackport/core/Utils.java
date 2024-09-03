package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import roadhog360.simpleskinbackport.ducks.INewModelData;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    private static final ResourceLocation[] DEFAULT_SKINS = new ResourceLocation[] {
        new ResourceLocation("textures/entity/player/slim/alex.png"),
        new ResourceLocation("textures/entity/player/slim/ari.png"),
        new ResourceLocation("textures/entity/player/slim/efe.png"),
        new ResourceLocation("textures/entity/player/slim/kai.png"),
        new ResourceLocation("textures/entity/player/slim/makena.png"),
        new ResourceLocation("textures/entity/player/slim/noor.png"),
        new ResourceLocation("textures/entity/player/slim/steve.png"),
        new ResourceLocation("textures/entity/player/slim/sunny.png"),
        new ResourceLocation("textures/entity/player/slim/zuri.png"),

        new ResourceLocation("textures/entity/player/wide/alex.png"),
        new ResourceLocation("textures/entity/player/wide/ari.png"),
        new ResourceLocation("textures/entity/player/wide/efe.png"),
        new ResourceLocation("textures/entity/player/wide/kai.png"),
        new ResourceLocation("textures/entity/player/wide/makena.png"),
        new ResourceLocation("textures/entity/player/wide/noor.png"),
        new ResourceLocation("textures/entity/player/wide/steve.png"),
        new ResourceLocation("textures/entity/player/wide/sunny.png"),
        new ResourceLocation("textures/entity/player/wide/zuri.png")};

    public static final ResourceLocation NEW_STEVE = DEFAULT_SKINS[15];

    public static int getIndexFromUUID(UUID uuid) {
        if(uuid == null) { //TODO Config to not use new skins perhaps?
            return 15;
        }
        return Math.floorMod(uuid.hashCode(), DEFAULT_SKINS.length);
    }

    public static ResourceLocation getDefaultSkin(int index) {
        return DEFAULT_SKINS[index % DEFAULT_SKINS.length];
    }

    public static ResourceLocation getDefaultSkin(UUID uuid) {
        return getDefaultSkin(getIndexFromUUID(uuid));
    }

    public static boolean isDefaultSkinSlim(UUID uuid) {
        return getIndexFromUUID(uuid) < DEFAULT_SKINS.length / 2;
    }

    public static boolean getSlimFromBase64Data(String base64) {
        JsonObject props = new Gson().fromJson(new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8), JsonObject.class);
        JsonObject medatata = props.getAsJsonObject("textures").getAsJsonObject("SKIN").getAsJsonObject("metadata");
        return medatata != null && medatata.get("model").getAsString().equals("slim");
    }

    /**
     * null == could not get slim state from game profile
     * Unused but might be needed in the future
     * @param profile
     * @return
     */
    public static Boolean getSlimFromGameProfile(GameProfile profile) {
        if(profile.getProperties().containsKey("textures")) {
            for(Property property : profile.getProperties().get("textures")) {
                if(property.getName().equals("textures")) {
                    return Utils.getSlimFromBase64Data(property.getValue());
                }
            }
        }
        return null;
    }

    public static void setSlimFromMetadata(MinecraftProfileTexture texture, INewModelData data) {
        data.simpleSkinBackport$setSlim(Objects.equals(texture.getMetadata("model"), "slim"));
    }

    public static boolean isPlayer(MinecraftProfileTexture.Type type, SkinManager.SkinAvailableCallback callback) {
        return type == MinecraftProfileTexture.Type.SKIN && callback instanceof EntityPlayer;
    }

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Utils.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
    }

    /**
     * null == not cached
     */
    public static final Map<String, Boolean> HASH_CACHE = Maps.newHashMap();
}
