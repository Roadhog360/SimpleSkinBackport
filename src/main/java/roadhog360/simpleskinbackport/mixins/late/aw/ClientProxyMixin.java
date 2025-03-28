package roadhog360.simpleskinbackport.mixins.late.aw;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import riskyken.armourersWorkshop.proxies.ClientProxy;

@Mixin(value = ClientProxy.class, remap = false)
public class ClientProxyMixin {

    @Inject(method = "useTexturePainting()Z", at = @At("HEAD"), cancellable = true)
    private static void useTexturePainting(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

}
