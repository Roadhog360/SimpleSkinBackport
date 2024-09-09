package roadhog360.simpleskinbackport.mixins.late.twilightforest;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import roadhog360.simpleskinbackport.configuration.configs.ConfigModCompat;
import roadhog360.simpleskinbackport.core.PlayerSkin;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;
import twilightforest.client.renderer.entity.RenderTFGiant;

@Mixin(RenderTFGiant.class)
public abstract class MixinRenderTFGiant extends RenderBiped {
    public MixinRenderTFGiant(ModelBiped p_i1257_1_, float p_i1257_2_) {
        super(p_i1257_1_, p_i1257_2_);
    }

    /**
     * Mixin is not enabled when TFgiantSkinSet is null since we don't need to change any code otherwise.
     */
    @Inject(method = "getEntityTexture", at = @At(value = "HEAD"), cancellable = true)
    private void overrideSkinAndSetResourceLocation(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) {
        PlayerSkin skin = ConfigModCompat.TFgiantSkinSet.getDefaultSkin(par1Entity.getPersistentID());
        if(this.modelBipedMain instanceof INewBipedModel model) {
            model.simpleSkinBackport$setSlim(skin.isSlim());
            cir.setReturnValue(skin.getResource());
        }
    }
}
