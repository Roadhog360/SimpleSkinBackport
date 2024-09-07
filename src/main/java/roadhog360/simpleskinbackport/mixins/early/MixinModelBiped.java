package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.configuration.configs.ConfigMain;
import roadhog360.simpleskinbackport.core.ArmPair;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase implements INewBipedModel {

    @Shadow
    public ModelRenderer bipedHead;
    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedBody;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public ModelRenderer bipedEars;
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
    private boolean simpleSkinBackport$isPlayerModel;


    @WrapOperation(method = "<init>(FFII)V", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target="Lnet/minecraft/client/model/ModelBiped;textureHeight:I"))
    private void checkPlayerModelAndSetDim(ModelBiped instance, int value, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) float size) {
        original.call(instance, simpleSkinBackport$isPlayerModel() ? 64 : value);

        if(!simpleSkinBackport$isPlayerModel()) {
            boolean isPlayerRenderer;
            boolean isGaia;
            //Do all this OUTSIDE of the if bracket, so we can find and add cases for classes that use size > 0
            try {
                Class<?> caller = Class.forName(Utils.getCallerClassName());
                isGaia = caller.getName().equals("vazkii.botania.client.render.entity.RenderDoppleganger");
                isPlayerRenderer = caller.isAssignableFrom(RenderPlayer.class) || isGaia
                    || caller.getName().equals("net.smart.render.ModelPlayer") || caller.getName().contains("RenderTFGiant");
                //We need a way to check if it's a player model, without modifying the RenderPlayer.
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (size == 0 || size == 0.0625F || isGaia) {
                //TODO: Might have to add special exceptions for big models, I think this is model size so I named it as such.
                // Mods that add giant (or small) players will need special treatment.
                if (isPlayerRenderer) {
                    original.call(instance, 64);
                    simpleSkinBackport$setPlayerModel(true);
                }
            }
        }
    }

    @Inject(method = "<init>(FFII)V", at = @At("TAIL"))
    private void injectNewLimbs(float size, float p_i1149_2_, int texWidth, int texHeight, CallbackInfo ci) {
        if (simpleSkinBackport$isPlayerModel()) {
            Utils.remakeBoxes(bipedCloak.setTextureSize(64, 32));

            Utils.setAllBoxesTransparent(bipedHeadwear);

            bipedLeftArm.mirror = false;
            Utils.changeTextureOffset(bipedLeftArm, 32, 48);
            //Right arm is fine as it is and doesn't need any transformation.

            simpleSkinBackport$bipedLeftArmwear = Utils.cloneModel(this, bipedLeftArm, 48, 48,true, Utils.BoxTransformType.HAT);
            simpleSkinBackport$bipedRightArmwear = Utils.cloneModel(this, bipedRightArm, 40, 32,true, Utils.BoxTransformType.HAT);

            bipedLeftLeg.mirror = false;
            Utils.changeTextureOffset(bipedLeftLeg, 16, 48);
            //Right leg is fine as it is and doesn't need any transformation.

            simpleSkinBackport$bipedLeftLegwear = Utils.cloneModel(this, bipedLeftLeg, 0, 48,true, Utils.BoxTransformType.HAT);
            simpleSkinBackport$bipedRightLegwear = Utils.cloneModel(this, bipedRightLeg, 0, 32,true, Utils.BoxTransformType.HAT);

            simpleSkinBackport$bipedBodyWear = Utils.cloneModel(this, bipedBody, 16, 32,true, Utils.BoxTransformType.HAT);

            simpleSkinBackport$createBoxes();
        }
    }

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
     * Creates the ModelBox instances used for slim arms. Creates dummy models currently, this could probably be more efficient...
     */
    @Unique
    private void simpleSkinBackport$createBoxes() {
        //Experimental code where I re-make everything and initialize the boxes when the slim/wide arm request is made
        //Because that can only mean this is a player model. Had a few issues with it though...
        //1 is for some reason the head's hat layer would always be merged with the head itself. Might be precision lost on reconstructing the ModelBoxes
        //Secondly was it didn't fix the right arm issue with Smart Moving like I wanted it to...
//        Utils.changeTextureSize(this, 64, 64);
//
//        Utils.remakeBoxes(bipedCloak.setTextureSize(64, 32));
//
//        Utils.setAllBoxesTransparent(bipedHeadwear);
//
//        bipedLeftArm.mirror = false;
//        Utils.changeTextureOffset(bipedLeftArm, 32, 48);
//        //Right arm is fine as it is and doesn't need any transformation.
//        Utils.remakeBoxes(bipedRightArm);
//
//        simpleSkinBackport$bipedLeftArmwear = Utils.cloneModel(this, bipedLeftArm, 48, 48,true, Utils.BoxTransformType.HAT);
//        simpleSkinBackport$bipedRightArmwear = Utils.cloneModel(this, bipedRightArm, 40, 32,true, Utils.BoxTransformType.HAT);
//
//        bipedLeftLeg.mirror = false;
//        Utils.changeTextureOffset(bipedLeftLeg, 16, 48);
//        //Right leg is fine as it is and doesn't need any transformation.
//        Utils.remakeBoxes(bipedRightLeg);
//
//        simpleSkinBackport$bipedLeftLegwear = Utils.cloneModel(this, bipedLeftLeg, 0, 48,true, Utils.BoxTransformType.HAT);
//        simpleSkinBackport$bipedRightLegwear = Utils.cloneModel(this, bipedRightLeg, 0, 32,true, Utils.BoxTransformType.HAT);
//
//        simpleSkinBackport$bipedBodyWear = Utils.cloneModel(this, bipedBody, 16, 32,true, Utils.BoxTransformType.HAT);

        ModelRenderer tempLeftArmSlim = Utils.cloneModel(this, bipedLeftArm, false, Utils.BoxTransformType.SLIM_ARM);
        ModelRenderer tempRightArmSlim = Utils.cloneModel(this, bipedRightArm, false, Utils.BoxTransformType.SLIM_RIGHT_ARM);
        ModelRenderer tempLeftArmwearSlim = Utils.setAllBoxesTransparent(Utils.cloneModel(
            this, bipedLeftArm, 48, 48, false, Utils.BoxTransformType.SLIM_ARM_HAT));
        ModelRenderer tempRightArmwearSlim = Utils.setAllBoxesTransparent(Utils.cloneModel(
            this, bipedRightArm, 40, 32, false, Utils.BoxTransformType.SLIM_RIGHT_ARM_HAT));

        simpleSkinBackport$wideArms = ArmPair.of(bipedLeftArm, bipedRightArm);
        simpleSkinBackport$slimArms = ArmPair.of(tempLeftArmSlim, tempRightArmSlim);

        simpleSkinBackport$wideArmwear = ArmPair.of(simpleSkinBackport$bipedLeftArmwear, simpleSkinBackport$bipedRightArmwear);
        simpleSkinBackport$slimArmwear = ArmPair.of(tempLeftArmwearSlim, tempRightArmwearSlim);

        simpleSkinBackport$armsRotationPointY = bipedLeftArm.rotationPointY;
    }

    @Override
    public void simpleSkinBackport$setSlim(boolean slim) {
//        if(!simpleSkinBackport$isPlayerModel) {
//            simpleSkinBackport$createBoxes();
//            simpleSkinBackport$isPlayerModel = true;
//        }

        if(simpleSkinBackport$isPlayerModel) {
            if(ConfigMain.oldSlimArms) {
                if (slim) {
                    bipedLeftArm.rotationPointY = simpleSkinBackport$armsRotationPointY + 0.5F;
                    bipedRightArm.rotationPointY = simpleSkinBackport$armsRotationPointY + 0.5F;
                } else {
                    bipedLeftArm.rotationPointY = simpleSkinBackport$armsRotationPointY;
                    bipedRightArm.rotationPointY = simpleSkinBackport$armsRotationPointY;
                }
            }
            ArmPair arms = slim ? simpleSkinBackport$slimArms : simpleSkinBackport$wideArms;
            ArmPair armwear = slim ? simpleSkinBackport$slimArmwear : simpleSkinBackport$wideArmwear;
            bipedLeftArm.displayList = arms.getLeftDisplayList();
            bipedRightArm.displayList = arms.getRightDisplayList();
            simpleSkinBackport$bipedLeftArmwear.displayList = armwear.getLeftDisplayList();
            simpleSkinBackport$bipedRightArmwear.displayList = armwear.getRightDisplayList();
            bipedLeftArm.cubeList = arms.getLeft();
            bipedRightArm.cubeList = arms.getRight();
            simpleSkinBackport$bipedLeftArmwear.cubeList = armwear.getLeft();
            simpleSkinBackport$bipedRightArmwear.cubeList = armwear.getRight();
        }
    }

    @Override
    public boolean simpleSkinBackport$isPlayerModel() {
        return simpleSkinBackport$isPlayerModel;
    }

    @Override
    public void simpleSkinBackport$setPlayerModel(boolean player) {
        simpleSkinBackport$isPlayerModel = player;
    }
}
