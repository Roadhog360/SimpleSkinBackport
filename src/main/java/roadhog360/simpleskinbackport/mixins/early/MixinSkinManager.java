package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import roadhog360.simpleskinbackport.SimpleSkinBackport;
import roadhog360.simpleskinbackport.client.ImageBufferDownloadPlayerSkin;
import roadhog360.simpleskinbackport.ducks.INewModelData;

import java.util.Objects;

@Mixin(value = SkinManager.class, priority = 1100)
public class MixinSkinManager {
    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation changeDownloadLocation(String p_i1293_1_, Operation<ResourceLocation> original,
                                                    @Local(argsOnly = true) final MinecraftProfileTexture.Type p_152789_2_,
                                                    @Local(argsOnly = true) final SkinManager.SkinAvailableCallback p_152789_3_
                                                    ) {
        if(p_152789_2_ == MinecraftProfileTexture.Type.SKIN) {
            return new ResourceLocation(SimpleSkinBackport.MODID, p_i1293_1_);
        }
        return original.call(p_i1293_1_);
    }

    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "()Lnet/minecraft/client/renderer/ImageBufferDownload;"))
    private ImageBufferDownload changeDownloadBufferManager(Operation<ImageBufferDownload> original,
                                                            @Local(argsOnly = true) final MinecraftProfileTexture.Type p_152789_2_,
                                                            @Local(argsOnly = true) SkinManager.SkinAvailableCallback p_152789_3_) {
        if(p_152789_2_ == MinecraftProfileTexture.Type.SKIN) {
            return new ImageBufferDownloadPlayerSkin();
        }
        return original.call();
    }

    @Inject(method = "func_152789_a",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;func_152121_a(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/util/ResourceLocation;)V",
        shift = At.Shift.AFTER))
    private void injectCallbackSkinCheck(MinecraftProfileTexture p_152789_1_, MinecraftProfileTexture.Type p_152789_2_, SkinManager.SkinAvailableCallback p_152789_3_, CallbackInfoReturnable<ResourceLocation> cir) {
        if(p_152789_2_ == MinecraftProfileTexture.Type.SKIN && p_152789_3_ instanceof INewModelData playerData) {
            if(Objects.equals(p_152789_1_.getMetadata("model"), "slim")) {
                playerData.simpleSkinBackport$setSlim(true);
            }
        }
    }
}
