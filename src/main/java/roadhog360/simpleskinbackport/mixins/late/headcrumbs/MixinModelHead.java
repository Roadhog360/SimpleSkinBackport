package roadhog360.simpleskinbackport.mixins.late.headcrumbs;

import ganymedes01.headcrumbs.renderers.ModelHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ModelHead.class)
public class MixinModelHead {

    @ModifyConstant(method = "<init>()V", constant = @Constant(intValue = 32))
    private static int changeTextureRes(int constant) {
        return 64;
    }
}
