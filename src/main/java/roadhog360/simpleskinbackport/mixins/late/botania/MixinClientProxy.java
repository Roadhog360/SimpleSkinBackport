package roadhog360.simpleskinbackport.mixins.late.botania;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(vazkii.botania.client.core.proxy.ClientProxy.class)
public class MixinClientProxy extends  vazkii.botania.common.core.proxy.CommonProxy {
    @WrapOperation(method = "initRenderers", remap = false, at = @At(value = "INVOKE", target = "Lcpw/mods/fml/client/registry/ClientRegistry;bindTileEntitySpecialRenderer(Ljava/lang/Class;Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;)V"))
    private void disarmSkullRenderReplacer(Class<? extends TileEntity> tileEntityClass, TileEntitySpecialRenderer specialRenderer, Operation<Void> original) {
        if(tileEntityClass != TileEntitySkull.class) {
            //We provide our own hat layer directly on the skull, so Botania doesn't need to do this.
            original.call(tileEntityClass, specialRenderer);
        }
    }
}
