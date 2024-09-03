package roadhog360.simpleskinbackport.mixins.early;

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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.client.models.ModelHatLayer;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.ISwitchableArmsModel;

@Mixin(ModelBiped.class)
public abstract class MixinModelBiped extends ModelBase implements ISwitchableArmsModel {

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
    public ModelRenderer simpleSkinBackport$bipedLeftArmWide;
    @Unique
    public ModelRenderer simpleSkinBackport$bipedRightArmWide;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedLeftArmwearWide;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedRightArmwearWide;

    @Unique
    public ModelRenderer simpleSkinBackport$bipedLeftArmSlim;
    @Unique
    public ModelRenderer simpleSkinBackport$bipedRightArmSlim;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedLeftArmwearSlim;
    @Unique
    public ModelHatLayer simpleSkinBackport$bipedRightArmwearSlim;

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
            isPlayerRenderer = caller.isAssignableFrom(RenderPlayer.class) || caller.getName().equals("net.smart.render.ModelPlayer");
            //We need a way to check if it's a player model, without modifying the RenderPlayer.
            System.out.println();
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

            simpleSkinBackport$bipedLeftArmWide = new ModelRenderer(this, 32, 48);
            simpleSkinBackport$bipedLeftArmWide.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size);
            simpleSkinBackport$bipedLeftArmWide.setRotationPoint(5.0F, 2F, 0.0F);

            simpleSkinBackport$bipedRightArmWide = new ModelRenderer(this, 40, 16);
            simpleSkinBackport$bipedRightArmWide.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size);
            simpleSkinBackport$bipedRightArmWide.setRotationPoint(-5.0F, 2F, 0.0F);

            simpleSkinBackport$bipedLeftArmwearWide = new ModelHatLayer(this, 48, 48);
            simpleSkinBackport$bipedLeftArmwearWide.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, size + 0.25F);
            simpleSkinBackport$bipedLeftArmWide.addChild(simpleSkinBackport$bipedLeftArmwearWide);

            simpleSkinBackport$bipedRightArmwearWide = new ModelHatLayer(this, 40, 32);
            simpleSkinBackport$bipedRightArmwearWide.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, size + 0.25F);
            simpleSkinBackport$bipedRightArmWide.addChild(simpleSkinBackport$bipedRightArmwearWide);

            simpleSkinBackport$bipedLeftArmSlim = new ModelRenderer(this, 32, 48);
            simpleSkinBackport$bipedLeftArmSlim.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, size);
            simpleSkinBackport$bipedLeftArmSlim.setRotationPoint(5.0F, 2.5F, 0.0F);

            simpleSkinBackport$bipedRightArmSlim = new ModelRenderer(this, 40, 16);
            simpleSkinBackport$bipedRightArmSlim.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, size);
            simpleSkinBackport$bipedRightArmSlim.setRotationPoint(-4.0F, 2.5F, 0.0F);

            simpleSkinBackport$bipedLeftArmwearSlim = new ModelHatLayer(this, 48, 48);
            simpleSkinBackport$bipedLeftArmwearSlim.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, size + 0.25F);
            simpleSkinBackport$bipedLeftArmSlim.addChild(simpleSkinBackport$bipedLeftArmwearSlim);

            simpleSkinBackport$bipedRightArmwearSlim = new ModelHatLayer(this, 40, 32);
            simpleSkinBackport$bipedRightArmwearSlim.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, size + 0.25F);
            simpleSkinBackport$bipedRightArmSlim.addChild(simpleSkinBackport$bipedRightArmwearSlim);

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

    @Override
    public void simpleSkinBackport$setSlim(boolean slim) {
        if(simpleSkinBackport$isPlayerModel) {
            if (slim) {
                bipedLeftArm = simpleSkinBackport$bipedLeftArmSlim;
                bipedRightArm = simpleSkinBackport$bipedRightArmSlim;
            } else {
                bipedLeftArm = simpleSkinBackport$bipedLeftArmWide;
                bipedRightArm = simpleSkinBackport$bipedRightArmWide;
            }
        }
    }
}
