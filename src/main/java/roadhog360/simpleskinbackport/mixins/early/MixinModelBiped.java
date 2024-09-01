package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.core.Utils;

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
    public Utils.CreationState simpleSkinBackport$stateCreatedIn = Utils.CreationState.NONE;

    @Inject(method = "<init>(FFII)V", at = @At("TAIL"))
    private void injectNewLimbs(float z, float p_i1149_2_, int texWidth, int texHeight, CallbackInfo ci) {
        if(Utils.getCurrentCreationState() != Utils.CreationState.NONE && texHeight == 64) {
            //TODO There are probably 64x32 slim skins, we should try to support them
            //I think our parseUserSkin mixin might automatically convert them though? Gotta check...
            //This "should" just work

            simpleSkinBackport$stateCreatedIn = Utils.getCurrentCreationState();

            bipedCloak = new ModelRenderer(this, 0, 0);
            bipedCloak.setTextureSize(64, 32);
            bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, z);

            if (Utils.getCurrentCreationState() != Utils.CreationState.SLIM_PLAYER) {
                bipedLeftArm = new ModelRenderer(this, 32, 48);
                bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, z);
                bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

                bipedRightArm = new ModelRenderer(this, 40, 16);
                bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, z);
                bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

                simpleSkinBackport$bipedLeftArmwear = new ModelRenderer(this, 48, 48);
                simpleSkinBackport$bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, z + 0.25F);
                bipedLeftArm.addChild(simpleSkinBackport$bipedLeftArmwear);

                simpleSkinBackport$bipedRightArmwear = new ModelRenderer(this, 40, 32);
                simpleSkinBackport$bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, z + 0.25F);
                bipedRightArm.addChild(simpleSkinBackport$bipedRightArmwear);
            } else {
                bipedLeftArm = new ModelRenderer(this, 32, 48);
                bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, z);
                bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

                simpleSkinBackport$bipedLeftArmwear = new ModelRenderer(this, 48, 48);
                simpleSkinBackport$bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, z + 0.25F);
                bipedLeftArm.addChild(simpleSkinBackport$bipedLeftArmwear);

                simpleSkinBackport$bipedRightArmwear = new ModelRenderer(this, 40, 32);
                simpleSkinBackport$bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, z + 0.25F);
                bipedRightArm.addChild(simpleSkinBackport$bipedRightArmwear);
            }

            bipedLeftLeg = new ModelRenderer(this, 16, 48);
            bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z);
            bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

            simpleSkinBackport$bipedLeftLegwear = new ModelRenderer(this, 0, 48);
            simpleSkinBackport$bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z + 0.25F);
            bipedLeftLeg.addChild(simpleSkinBackport$bipedLeftLegwear);

            simpleSkinBackport$bipedRightLegwear = new ModelRenderer(this, 0, 32);
            simpleSkinBackport$bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z + 0.25F);
            bipedRightLeg.addChild(simpleSkinBackport$bipedRightLegwear);

            simpleSkinBackport$bipedBodyWear = new ModelRenderer(this, 16, 32);
            simpleSkinBackport$bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, z + 0.25F);
            bipedBody.addChild(simpleSkinBackport$bipedBodyWear);
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void setRightArmRotationPoint(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_, CallbackInfo ci) {
        if (simpleSkinBackport$stateCreatedIn == Utils.CreationState.SLIM_PLAYER) {
            bipedRightArm.rotationPointX += 1.0F;
        }
    }
}
