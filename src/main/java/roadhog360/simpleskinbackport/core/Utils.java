package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import roadhog360.simpleskinbackport.ducks.IArmsState;
import roadhog360.simpleskinbackport.ducks.ITransparentBox;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
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

    public static final ResourceLocation STEVE = DEFAULT_SKINS[15];
    public static final ResourceLocation ALEX = DEFAULT_SKINS[0];

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

    public static boolean isDefaultSkinSlim(int index) {
        return index % DEFAULT_SKINS.length < DEFAULT_SKINS.length / 2;
    }

    public static boolean isDefaultSkinSlim(UUID uuid) {
        return isDefaultSkinSlim(getIndexFromUUID(uuid));
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

    public static void setSlimFromMetadata(MinecraftProfileTexture texture, IArmsState data) {
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

    public static ModelRenderer setAllChildBoxesTransparent(ModelRenderer renderer) {
        renderer.childModels.forEach(Utils::setAllBoxesTransparent);
        return renderer;
    }

    public static ModelRenderer setAllBoxesTransparent(ModelRenderer renderer) {
        renderer.cubeList.forEach(Utils::setBoxTransparent);
        return renderer;
    }

    public static ModelBox setBoxTransparent(ModelBox box) {
        if(box instanceof ITransparentBox) {
            ((ITransparentBox) box).simpleSkinBackport$setTransparent(true);
        }
        return box;
    }

    public static ModelRenderer cloneModel(ModelBase base, ModelRenderer from) {
        return cloneModel(base, from, from.textureOffsetX, from.textureOffsetY, false, BoxTransformType.NONE);
    }

    public static ModelRenderer cloneModel(ModelBase base, ModelRenderer from, boolean addAsChild) {
        return cloneModel(base, from, from.textureOffsetX, from.textureOffsetY, addAsChild, BoxTransformType.NONE);
    }

    public static ModelRenderer cloneModel(ModelBase base, ModelRenderer from, boolean addAsChild, BoxTransformType transform) {
        return cloneModel(base, from, from.textureOffsetX, from.textureOffsetY, addAsChild, transform);
    }

    public static ModelRenderer cloneModel(ModelBase base, ModelRenderer from, int textureOffsetX, int textureOffsetZ) {
        return cloneModel(base, from, textureOffsetX, textureOffsetZ, false, BoxTransformType.NONE);
    }

    public static ModelRenderer cloneModel(ModelBase base, ModelRenderer from, int textureOffsetX, int textureOffsetZ, boolean addAsChild, BoxTransformType transform) {
        ModelRenderer to = new ModelRenderer(base, textureOffsetX, textureOffsetZ);
        to.mirror = from.mirror;
        for(ModelBox box : from.cubeList) {
            to.cubeList.add(cloneBox(box, to, transform));

        }
        if(!transform.isHatLayer()) {
            to.setRotationPoint(from.rotationPointX, from.rotationPointY, from.rotationPointZ);
        }
        if(addAsChild) {
            from.addChild(to);
        }
        return to;
    }

    public static ModelBox cloneBox(ModelBox box, ModelRenderer to, BoxTransformType transform) {
        float size = 0;
        float boxMinX = box.posX1;
        float boxMinY = box.posY1;
        float boxMinZ = box.posZ1;
        int boxMaxX = MathHelper.floor_float(box.posX2 - box.posX1);
        int boxMaxY = MathHelper.floor_float(box.posY2 - box.posY1);
        int boxMaxZ = MathHelper.floor_float(box.posZ2 - box.posZ1);
        if(transform.isHatLayer()) {
            size += 0.25F;
        }
        if(transform.isSlim()) {
            if(transform.isRightArm()) {
                boxMinX += 1;
            }
            boxMaxX -= 1;
        }
        ModelBox newBox = makeBox(to, boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ, size);
        if(newBox instanceof ITransparentBox newBoxTransparent && box instanceof ITransparentBox boxTransparent) {
            newBoxTransparent.simpleSkinBackport$setTransparent(boxTransparent.simpleSkinBackport$isTransparent());
        }
        return newBox;
    }
    public static void remakeBoxes(ModelRenderer renderer) {
        remakeBoxes(renderer, BoxTransformType.NONE);
    }

    public static void remakeBoxes(ModelRenderer renderer, BoxTransformType transform) {
        List<ModelBox> list = Lists.newArrayList();
        for(ModelBox box : renderer.cubeList) {
            list.add(cloneBox(box, renderer, transform));
        }
        renderer.cubeList.clear();
        renderer.cubeList.addAll(list);
    }

    public static void changeTextureOffset(ModelRenderer renderer, int offsetX, int offsetY) {
        renderer.setTextureOffset(offsetX, offsetY);
        remakeBoxes(renderer, BoxTransformType.NONE);
    }

    public static ModelBox makeBox(ModelRenderer renderer, float boxMinX, float boxMinY, float boxMinZ, int boxMaxX, int boxMaxY, int boxMaxZ, float size) {
        return new ModelBox(renderer, renderer.textureOffsetX, renderer.textureOffsetY, boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ, size);
    }

    private static ThreadLocal<ModelRenderer> DUMMY_MODEL = ThreadLocal.withInitial(() -> new ModelRenderer(new ModelBase(){}, 64, 64));

    public static int createDisplaylistFor(ModelRenderer renderer) {
        DUMMY_MODEL.get().cubeList = renderer.cubeList;
        DUMMY_MODEL.get().compileDisplayList(0.0625F);
        return DUMMY_MODEL.get().displayList;
    }

    public static void changeTextureSize(ModelBase base, int width, int height) {
        base.textureWidth = width;
        base.textureHeight = height;
        base.boxList.forEach(box -> box.setTextureSize(width, height));
        base.boxList.forEach(Utils::remakeBoxes);
    }

    /**
     * I got sick of constantly updating the util funcs whenever I needed to pass in a new setting lol
     */
    public enum BoxTransformType {
         NONE(false, false, false),
         HAT(true, false, false),
         SLIM_ARM(false, true, false),
         SLIM_RIGHT_ARM(false, true, true),
         SLIM_ARM_HAT(true, true, false),
         SLIM_RIGHT_ARM_HAT(true, true, true);

        private final boolean hatLayer;
        private final boolean slim;
        private final boolean rightArm;

        private BoxTransformType(boolean hatLayer, boolean slim, boolean rightArm) {
            this.hatLayer = hatLayer;
            this.slim = slim;
            this.rightArm = rightArm;
        }

        public boolean isSlim() {
            return slim;
        }

        public boolean isRightArm() {
            return rightArm;
        }

        public boolean isHatLayer() {
            return hatLayer;
        }
    }
}
