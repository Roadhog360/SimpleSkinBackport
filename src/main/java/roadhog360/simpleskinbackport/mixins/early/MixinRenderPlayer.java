package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.client.ModelSlimArm;
import roadhog360.simpleskinbackport.ducks.INewModelData;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {
    @Shadow
    public ModelBiped modelBipedMain;

    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "renderFirstPersonArm", at = @At(value = "HEAD"))
    private void setFirstPersonArmState(EntityPlayer p_82441_1_, CallbackInfo ci) {
        if(p_82441_1_ instanceof INewModelData player && modelBipedMain.bipedRightArm instanceof ModelSlimArm slimRightArm) {
            slimRightArm.setSlim(player.simpleSkinBackport$isSlim());
        }
    }
}
