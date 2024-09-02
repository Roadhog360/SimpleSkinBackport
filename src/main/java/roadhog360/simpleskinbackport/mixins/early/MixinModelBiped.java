package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.client.ModelHatLayer;
import roadhog360.simpleskinbackport.client.ModelSlimArm;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.INewModelData;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase {

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
    public ModelSlimArm simpleSkinBackport$bipedLeftArmwear;
    @Unique
    public ModelSlimArm simpleSkinBackport$bipedRightArmwear;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedLeftLegwear;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedRightLegwear;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedBodyWear;

    @Unique
    private boolean simpleSkinBackport$isPlayerModel;

    @Redirect(method = "<init>(FFII)V", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target="Lnet/minecraft/client/model/ModelBiped;textureHeight:I"))
    private void checkPlayerModelAndSetDim(ModelBiped instance, int value, @Local(ordinal = 0, argsOnly = true) float size) {
        textureHeight = value; //Original assignment; We overwrote this earlier

        boolean isPlayerRenderer;
        //Do all this OUTSIDE of the if bracket, so we can find and add cases for classes that use size > 0
        try {
            Class<?> caller = Class.forName(Utils.getCallerClassName());
            isPlayerRenderer = caller.isAssignableFrom(RenderPlayer.class);
            //We need a way to check if it's a player model, without modifying the RenderPlayer.
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(size == 0) {
            //TODO: Might have to add special exceptions for big models, I think this is model size so I named it as such.
            // Mods that add giant (or small) players will need special treatment.
            if (isPlayerRenderer) {
                textureHeight = 64;
                simpleSkinBackport$isPlayerModel = true;
            }
        }
    }

    @Inject(method = "<init>(FFII)V", at = @At("TAIL"))
    private void injectNewLimbs(float size, float p_i1149_2_, int texWidth, int texHeight, CallbackInfo ci) {
        if (simpleSkinBackport$isPlayerModel) {
            //TODO There are probably 64x32 slim skins, we should try to support them
            //I think our parseUserSkin mixin might automatically convert them though? Gotta check...
            //This "should" just work
            //Also TODO Maybe make these inherit the values from the above boxes?

            bipedCloak = new ModelRenderer(this, 0, 0);
            bipedCloak.setTextureSize(64, 32);
            bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, size);

            bipedLeftArm = new ModelSlimArm(this, 32, 48, false);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size);
            bipedLeftArm.setRotationPoint(5.0F, 2F, 0.0F);

            bipedRightArm = new ModelSlimArm(this, 40, 16, true);
            bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size);
            bipedRightArm.setRotationPoint(-5.0F, 2F, 0.0F);

            simpleSkinBackport$bipedLeftArmwear = new ModelSlimArm(this, 48, 48, false).setAsHatLayer();
            simpleSkinBackport$bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size + 0.25F);
            bipedLeftArm.addChild(simpleSkinBackport$bipedLeftArmwear);

            simpleSkinBackport$bipedRightArmwear = new ModelSlimArm(this, 40, 32, true).setAsHatLayer();
            simpleSkinBackport$bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size + 0.25F);
            bipedRightArm.addChild(simpleSkinBackport$bipedRightArmwear);

            bipedLeftLeg = new ModelRenderer(this, 16, 48);
            bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size);
            bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

            simpleSkinBackport$bipedLeftLegwear = new ModelHatLayer(this, 0, 48);
            simpleSkinBackport$bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size + 0.25F);
            bipedLeftLeg.addChild(simpleSkinBackport$bipedLeftLegwear);

            simpleSkinBackport$bipedRightLegwear = new ModelHatLayer(this, 0, 32);
            simpleSkinBackport$bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, size + 0.25F);
            bipedRightLeg.addChild(simpleSkinBackport$bipedRightLegwear);

            simpleSkinBackport$bipedBodyWear = new ModelHatLayer(this, 16, 32);
            simpleSkinBackport$bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, size + 0.25F);
            bipedBody.addChild(simpleSkinBackport$bipedBodyWear);
        }
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void setupArmStates(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_, CallbackInfo ci) {
        if(simpleSkinBackport$isPlayerModel && p_78088_1_ instanceof INewModelData player) {
            if(bipedLeftArm instanceof ModelSlimArm bipedLeftArmSlim) {
                bipedLeftArmSlim.setSlim(player.simpleSkinBackport$isSlim());
            }
            if(bipedRightArm instanceof ModelSlimArm bipedRightArmSlim) {
                bipedRightArmSlim.setSlim(player.simpleSkinBackport$isSlim());
            }

            //TODO Add visibility stuff here
        }
    }
}
