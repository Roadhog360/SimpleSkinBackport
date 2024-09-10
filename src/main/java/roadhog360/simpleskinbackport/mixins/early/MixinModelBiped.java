package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import roadhog360.simpleskinbackport.configuration.configs.ConfigMain;
import roadhog360.simpleskinbackport.core.ArmPair;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.core.compat.SmartRenderCompat;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase implements INewBipedModel {

    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedBody;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public ModelRenderer bipedCloak;

    //new stuff
    @Unique
    public ModelRenderer simpleSkinBackport$bipedLeftArmwear;
    @Unique
    public ModelRenderer simpleSkinBackport$bipedRightArmwear;

    @Unique
    public ModelRenderer simpleSkinBackport$bipedLeftLegwear;
    @Unique
    public ModelRenderer simpleSkinBackport$bipedRightLegwear;
    @Unique
    public ModelRenderer simpleSkinBackport$bipedBodyWear;

    @Unique
    private ArmPair simpleSkinBackport$wideArms;
    @Unique
    private ArmPair simpleSkinBackport$slimArms;

    @Unique
    private ArmPair simpleSkinBackport$wideArmwear;
    @Unique
    private ArmPair simpleSkinBackport$slimArmwear;

    @Unique
    private float simpleSkinBackport$armsRotationPointY;

    /**
     * Resizes a model's boxes from a 32x64 texture to a 64x64 texture.
     */
    public void simpleSkinBackport$set64x() {
        if(textureHeight == 32) {
            textureHeight = 64;

            Utils.changeTextureSize(this, textureWidth, 64);

            simpleSkinBackport$setupBoxes();
            simpleSkinBackport$createArmBoxes();
        }
    }

    @Unique
    private void simpleSkinBackport$setupBoxes() {
        Utils.setAllBoxesTransparent(bipedHeadwear);

        if(textureHeight == 64) {
            Utils.remakeBoxes(bipedCloak.setTextureSize(textureWidth, 32));

            bipedLeftArm.mirror = false;
            Utils.changeTextureOffset(bipedLeftArm, 32, 48);
            //Right arm is fine as it is and doesn't need any transformation.

            simpleSkinBackport$bipedLeftArmwear = Utils.cloneModel(this, bipedLeftArm, 48, 48, true, Utils.BoxTransformType.HAT);
            simpleSkinBackport$bipedRightArmwear = Utils.cloneModel(this, bipedRightArm, 40, 32, true, Utils.BoxTransformType.HAT);

            bipedLeftLeg.mirror = false;
            Utils.changeTextureOffset(bipedLeftLeg, 16, 48);
            //Right leg is fine as it is and doesn't need any transformation.

            simpleSkinBackport$bipedLeftLegwear = Utils.cloneModel(this, bipedLeftLeg, 0, 48, true, Utils.BoxTransformType.HAT);
            simpleSkinBackport$bipedRightLegwear = Utils.cloneModel(this, bipedRightLeg, 0, 32, true, Utils.BoxTransformType.HAT);

            simpleSkinBackport$bipedBodyWear = Utils.cloneModel(this, bipedBody, 16, 32, true, Utils.BoxTransformType.HAT);
        }

        simpleSkinBackport$armsRotationPointY = bipedRightArm.rotationPointY;
    }

    /**
     * Creates the ModelBox instances used for slim arms.
     */
    @Unique
    private void simpleSkinBackport$createArmBoxes() {
        ModelRenderer tempLeftArmSlim = Utils.cloneModel(this, bipedLeftArm, false, Utils.BoxTransformType.SLIM_ARM);
        ModelRenderer tempRightArmSlim = Utils.cloneModel(this, bipedRightArm, false, Utils.BoxTransformType.SLIM_RIGHT_ARM);

        simpleSkinBackport$wideArms = ArmPair.of(bipedLeftArm, bipedRightArm);
        simpleSkinBackport$slimArms = ArmPair.of(tempLeftArmSlim, tempRightArmSlim);

        simpleSkinBackport$armsRotationPointY = bipedLeftArm.rotationPointY;

        if(textureHeight == 64) {
            ModelRenderer tempLeftArmwearSlim = Utils.setAllBoxesTransparent(Utils.cloneModel(
                this, bipedLeftArm, 48, 48, false, Utils.BoxTransformType.SLIM_ARM_HAT));
            ModelRenderer tempRightArmwearSlim = Utils.setAllBoxesTransparent(Utils.cloneModel(
                this, bipedRightArm, 40, 32, false, Utils.BoxTransformType.SLIM_RIGHT_ARM_HAT));
            simpleSkinBackport$wideArmwear = ArmPair.of(simpleSkinBackport$bipedLeftArmwear, simpleSkinBackport$bipedRightArmwear);
            simpleSkinBackport$slimArmwear = ArmPair.of(tempLeftArmwearSlim, tempRightArmwearSlim);
        }
    }

    @Override
    public void simpleSkinBackport$setSlim(boolean slim) {
        if(simpleSkinBackport$slimArms == null || simpleSkinBackport$wideArms == null) {
            simpleSkinBackport$createArmBoxes();
        }
        ArmPair arms = slim ? simpleSkinBackport$slimArms : simpleSkinBackport$wideArms;
        if (ConfigMain.oldSlimArms) {
            if (slim) {
                bipedLeftArm.rotationPointY = simpleSkinBackport$armsRotationPointY + 0.5F;
                bipedRightArm.rotationPointY = simpleSkinBackport$armsRotationPointY + 0.5F;
            } else {
                bipedLeftArm.rotationPointY = simpleSkinBackport$armsRotationPointY;
                bipedRightArm.rotationPointY = simpleSkinBackport$armsRotationPointY;
            }
        }
        bipedLeftArm.displayList = arms.getLeftDisplayList();
        bipedRightArm.displayList = arms.getRightDisplayList();
        bipedLeftArm.cubeList = arms.getLeft();
        bipedRightArm.cubeList = arms.getRight();
        SmartRenderCompat.updateSmartRenderFields(bipedLeftArm, bipedRightArm);
        if(textureHeight == 64 && simpleSkinBackport$slimArmwear != null && simpleSkinBackport$wideArmwear != null) {
            ArmPair armwear = slim ? simpleSkinBackport$slimArmwear : simpleSkinBackport$wideArmwear;
            simpleSkinBackport$bipedLeftArmwear.displayList = armwear.getLeftDisplayList();
            simpleSkinBackport$bipedRightArmwear.displayList = armwear.getRightDisplayList();
            simpleSkinBackport$bipedLeftArmwear.cubeList = armwear.getLeft();
            simpleSkinBackport$bipedRightArmwear.cubeList = armwear.getRight();
        }
    }
}
