package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import roadhog360.simpleskinbackport.client.ImageBufferDownloadPlayerSkin;

@Mixin(value = SkinManager.class, priority = 1100)
public class MixinSkinManager {
    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation changeDownloadLocation(String p_i1293_1_, Operation<ResourceLocation> original, @Local SkinManager.SkinAvailableCallback p_152789_3_) {
        if(p_152789_3_ instanceof AbstractClientPlayer) {
            String[] string = p_i1293_1_.split("/");
            if(string.length == 2) {
                return original.call(string[0] + "/64/" + string[1]);
            }
        }
        return original.call(p_i1293_1_);
    }

    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "()Lnet/minecraft/client/renderer/ImageBufferDownload;"))
    private ImageBufferDownload changeDownloadBufferManager(Operation<ImageBufferDownload> original, @Local SkinManager.SkinAvailableCallback p_152789_3_) {
        if(p_152789_3_ instanceof AbstractClientPlayer) {
            return new ImageBufferDownloadPlayerSkin();
        }
        return original.call();
    }
}
