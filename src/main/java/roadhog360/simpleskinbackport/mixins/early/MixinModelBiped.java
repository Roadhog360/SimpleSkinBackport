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
    public ModelRenderer ssb$bipedLeftArmwear;
    @Unique
    public ModelRenderer ssb$bipedRightArmwear;

    @Unique
    public ModelRenderer ssb$bipedLeftLegwear;
    @Unique
    public ModelRenderer ssb$bipedRightLegwear;
    @Unique
    public ModelRenderer ssb$bipedBodywear;

    @Unique
    private ArmPair ssb$wideArms;
    @Unique
    private ArmPair ssb$slimArms;

    @Unique
    private ArmPair ssb$wideArmwear;
    @Unique
    private ArmPair ssb$slimArmwear;

    @Unique
    private float ssb$armsRotationPointY;

    /**
     * Resizes a model's boxes from a 32x64 texture to a 64x64 texture.
     */
    public void ssb$set64x() {
        if(textureHeight == 32) {
            textureHeight = 64;

            Utils.changeTextureSize(this, textureWidth, 64);

            ssb$setupBoxes();
            ssb$createArmBoxes();
        }
    }

    @Unique
    private void ssb$setupBoxes() {
        Utils.setAllBoxesTransparent(bipedHeadwear);

        if(textureHeight == 64) {
            Utils.remakeBoxes(bipedCloak.setTextureSize(textureWidth, 32));

            bipedLeftArm.mirror = false;
            Utils.changeTextureOffset(bipedLeftArm, 32, 48);
            //Right arm is fine as it is and doesn't need any transformation.

            ssb$bipedLeftArmwear = Utils.setAllBoxesTransparent(Utils.cloneModel(this, bipedLeftArm, 48, 48, true, Utils.BoxTransformType.HAT));
            ssb$bipedRightArmwear = Utils.setAllBoxesTransparent(Utils.cloneModel(this, bipedRightArm, 40, 32, true, Utils.BoxTransformType.HAT));

            bipedLeftLeg.mirror = false;
            Utils.changeTextureOffset(bipedLeftLeg, 16, 48);
            //Right leg is fine as it is and doesn't need any transformation.

            ssb$bipedLeftLegwear = Utils.setAllBoxesTransparent(Utils.cloneModel(this, bipedLeftLeg, 0, 48, true, Utils.BoxTransformType.HAT));
            ssb$bipedRightLegwear = Utils.setAllBoxesTransparent(Utils.cloneModel(this, bipedRightLeg, 0, 32, true, Utils.BoxTransformType.HAT));

            ssb$bipedBodywear = Utils.setAllBoxesTransparent(Utils.cloneModel(this, bipedBody, 16, 32, true, Utils.BoxTransformType.HAT));
        }

        ssb$armsRotationPointY = bipedRightArm.rotationPointY;
    }

    /**
     * Creates the ModelBox instances used for slim arms, as well as armwear for slim and wide arms.
     */
    @Unique
    private void ssb$createArmBoxes() {
        ssb$wideArms = ArmPair.of(bipedLeftArm, bipedRightArm);
        ssb$slimArms = ArmPair.of(
            Utils.cloneModel(this, bipedLeftArm, false, Utils.BoxTransformType.SLIM_LEFT_ARM),
            Utils.cloneModel(this, bipedRightArm, false, Utils.BoxTransformType.SLIM_RIGHT_ARM)
        );

        ssb$armsRotationPointY = bipedLeftArm.rotationPointY;

        if(textureHeight == 64) {
            ssb$wideArmwear = ArmPair.of(ssb$bipedLeftArmwear, ssb$bipedRightArmwear);
            ssb$slimArmwear = ArmPair.of(
                Utils.cloneModel(this, ssb$bipedLeftArmwear, 48, 48, false, Utils.BoxTransformType.SLIM_LEFT_ARM),
                Utils.cloneModel(this, ssb$bipedRightArmwear, 40, 32, false, Utils.BoxTransformType.SLIM_RIGHT_ARM)
            );
        }
    }

    @Override
    public void ssb$setSlim(boolean slim) {
        if(ssb$slimArms == null || ssb$wideArms == null) {
            ssb$createArmBoxes();
        }
        ArmPair arms = slim ? ssb$slimArms : ssb$wideArms;
        if (ConfigMain.oldSlimArms) {
            if (slim) {
                bipedLeftArm.rotationPointY = ssb$armsRotationPointY + 0.5F;
                bipedRightArm.rotationPointY = ssb$armsRotationPointY + 0.5F;
            } else {
                bipedLeftArm.rotationPointY = ssb$armsRotationPointY;
                bipedRightArm.rotationPointY = ssb$armsRotationPointY;
            }
        }
        bipedLeftArm.displayList = arms.getLeftDisplayList();
        bipedRightArm.displayList = arms.getRightDisplayList();
        bipedLeftArm.cubeList = arms.getLeft();
        bipedRightArm.cubeList = arms.getRight();
        SmartRenderCompat.updateSmartRenderFields(bipedLeftArm, bipedRightArm);
        if(textureHeight == 64 && ssb$slimArmwear != null && ssb$wideArmwear != null) {
            ArmPair armwear = slim ? ssb$slimArmwear : ssb$wideArmwear;
            ssb$bipedLeftArmwear.displayList = armwear.getLeftDisplayList();
            ssb$bipedRightArmwear.displayList = armwear.getRightDisplayList();
            ssb$bipedLeftArmwear.cubeList = armwear.getLeft();
            ssb$bipedRightArmwear.cubeList = armwear.getRight();
        }
    }
}
