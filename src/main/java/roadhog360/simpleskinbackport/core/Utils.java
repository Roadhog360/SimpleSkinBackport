package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import roadhog360.simpleskinbackport.configuration.configs.ConfigModCompat;
import roadhog360.simpleskinbackport.core.compat.SmartRenderCompat;
import roadhog360.simpleskinbackport.ducks.IArmsState;
import roadhog360.simpleskinbackport.ducks.IBoxSizeGetter;
import roadhog360.simpleskinbackport.ducks.ITransparentBox;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class Utils {

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
        data.ssb$setSlim(Objects.equals(texture.getMetadata("model"), "slim"));
    }

    public static boolean isPlayer(MinecraftProfileTexture.Type type, SkinManager.SkinAvailableCallback callback) {
        return type == MinecraftProfileTexture.Type.SKIN && callback instanceof EntityPlayer;
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
            ((ITransparentBox) box).ssb$setTransparent(true);
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
        float boxMinX = box.posX1;
        float boxMinY = box.posY1;
        float boxMinZ = box.posZ1;
        int boxMaxX = MathHelper.floor_float(box.posX2 - box.posX1);
        int boxMaxY = MathHelper.floor_float(box.posY2 - box.posY1);
        int boxMaxZ = MathHelper.floor_float(box.posZ2 - box.posZ1);
        float size = 0;
        if(box instanceof IBoxSizeGetter boxWithSize) {
            size = boxWithSize.ssb$getSize();
        }
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
            newBoxTransparent.ssb$setTransparent(boxTransparent.ssb$isTransparent());
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

    public static int createDisplaylistFor(ModelRenderer renderer) {
        renderer.displayList = 0; //OptiFine for some reason checks if the display list is 0 and things get fucky if it isn't
        renderer.compileDisplayList(0.0625F);
        SmartRenderCompat.updateSmartRenderFields(renderer);
        return renderer.displayList;
    }

    public static void changeTextureSize(ModelBase base, int width, int height) {
        base.textureWidth = width;
        base.textureHeight = height;
        base.boxList.forEach(box -> box.setTextureSize(width, height));
        base.boxList.forEach(Utils::remakeBoxes);
    }

    public static boolean isClientPlayerSlim() {
        if(FMLClientHandler.instance().getClientPlayerEntity() instanceof IArmsState player) {
            return player.ssb$isSlim();
        }
        return false;
    }

    public static boolean rendererCopiesPlayerSkin(Render renderer) {
        return renderer.getClass().getName().equals("vazkii.botania.client.render.entity.RenderDoppleganger")
            || (ConfigModCompat.TFgiantSkinSet == null && renderer.getClass().getName().equals("twilightforest.client.renderer.entity.RenderTFGiant"));
    }

    /**
     * I got sick of constantly updating the util funcs whenever I needed to pass in a new setting lol
     */
    public enum BoxTransformType {
         NONE(false, false, false),
         HAT(true, false, false),
         SLIM_LEFT_ARM(false, true, false),
         SLIM_RIGHT_ARM(false, true, true),
        //Currently unused, may be useful in the future?
         SLIM_LEFT_ARM_HAT(true, true, false),
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
