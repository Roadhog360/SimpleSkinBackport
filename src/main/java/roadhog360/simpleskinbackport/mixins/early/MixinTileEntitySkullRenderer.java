package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySkullRenderer.class)
public abstract class MixinTileEntitySkullRenderer extends TileEntitySpecialRenderer {

    @Shadow
    private ModelSkeletonHead field_147538_h;

    @Inject(method = "func_152674_a",at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/resources/SkinManager;func_152788_a(Lcom/mojang/authlib/GameProfile;)Ljava/util/Map;", shift = At.Shift.AFTER))
    private void applyDoubleResSkin(float p_152674_1_, float p_152674_2_, float p_152674_3_,
                                    int p_152674_4_, float p_152674_5_, int p_152674_6_,
                                    GameProfile p_152674_7_, CallbackInfo ci,
                                    @Local LocalRef<ModelSkeletonHead> modelskeletonhead) {
        if(p_152674_7_.getProperties().containsKey("textures")) {
            modelskeletonhead.set(field_147538_h);
        }
    }
}
