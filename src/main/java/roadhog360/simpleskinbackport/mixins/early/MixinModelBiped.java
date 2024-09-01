package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.SimpleSkinBackport;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase {

    @Shadow public ModelRenderer bipedHead;
    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedBody;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public ModelRenderer bipedEars;
    @Shadow public ModelRenderer bipedCloak;

    //new stuff
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    public boolean smallArms = true;

    @Inject(method = "<init>(FFII)V", at = @At("TAIL"))
    private void injectNewLimbs(float z, float p_i1149_2_, int texWidth, int texHeight, CallbackInfo ci) {

        if (smallArms) { //thin arms, set to true for now
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, z);
            bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

            bipedRightArm = new ModelRenderer(this, 40, 16);
            bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, z);
            bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

            bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, z + 0.25F);
            bipedLeftArm.addChild(bipedLeftArmwear);

            bipedRightArmwear = new ModelRenderer(this, 40, 32);
            bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, z + 0.25F);
            bipedRightArm.addChild(bipedRightArmwear);
        } else {
            bipedLeftArm = new ModelRenderer(this, 32, 48);
            bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, z);
            bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

            bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, z + 0.25F);
            bipedLeftArm.addChild(bipedLeftArmwear);

            bipedRightArmwear = new ModelRenderer(this, 40, 32);
            bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, z + 0.25F);
            bipedRightArm.addChild(bipedRightArmwear);
        }

        bipedLeftLeg = new ModelRenderer(this, 16, 48);
        bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z);
        bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

        bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z + 0.25F);
        bipedLeftLeg.addChild(bipedLeftLegwear);

        bipedRightLegwear = new ModelRenderer(this, 0, 32);
        bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, z + 0.25F);
        bipedRightLeg.addChild(bipedRightLegwear);

        bipedBodyWear = new ModelRenderer(this, 16, 32);
        bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, z + 0.25F);
        bipedBody.addChild(bipedBodyWear);
    }
}
