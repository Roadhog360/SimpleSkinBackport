package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.client.ModelPlayerNew;
import roadhog360.simpleskinbackport.ducks.ISlimModelData;

import java.util.*;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {

    @Shadow public ModelBiped modelBipedMain;

    @Unique
    private static final ModelPlayerNew simpleSkinBackport$WIDE = new ModelPlayerNew(0.0F, false);
    @Unique
    private static final ModelPlayerNew simpleSkinBackport$SLIM = new ModelPlayerNew(0.0F, true);

    @Unique
    private static final ThreadLocal<Set<EntityPlayer>> simpleSkinBackport$SLIM_MODELS =
        ThreadLocal.withInitial(() -> Collections.newSetFromMap(new WeakHashMap<>()));

    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void useNewRenderer(CallbackInfo ci) {
        mainModel = modelBipedMain = simpleSkinBackport$WIDE;
    }

    @Inject(method = "renderFirstPersonArm", at = @At(value = "HEAD"))
    private void checkIfShouldUpdateModelState(EntityPlayer p_82441_1_, CallbackInfo ci) {
        simpleSkinBackport$updateModelState(p_82441_1_);
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At(value = "HEAD"))
    private void checkIfShouldUpdateModelState(AbstractClientPlayer p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_, CallbackInfo ci) {
        simpleSkinBackport$updateModelState(p_76986_1_);
    }

    @Unique
    private void simpleSkinBackport$updateModelState(EntityPlayer player) {
        boolean updateSkinState = false;
        if(player instanceof ISlimModelData playerExtData && playerExtData.simpleSkinBackport$needsModelUpdate()) {
            if(playerExtData.simpleSkinBackport$isSlim()) {
                simpleSkinBackport$SLIM_MODELS.get().add(player);
            } else {
                simpleSkinBackport$SLIM_MODELS.get().remove(player);
            }
            playerExtData.simpleSkinBackport$setNeedsUpdate(false);
        }
        mainModel = modelBipedMain = simpleSkinBackport$isSlim(player) ? simpleSkinBackport$SLIM : simpleSkinBackport$WIDE;
    }

    @Unique
    private boolean simpleSkinBackport$isSlim(EntityPlayer player) {
        return simpleSkinBackport$SLIM_MODELS.get().contains(player);
    }
}
