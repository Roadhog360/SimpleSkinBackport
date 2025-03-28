package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.core.Utils;

@Mixin(ModelSkeletonHead.class)
public abstract class MixinModelSkull extends ModelBase {

    @Shadow
    public ModelRenderer skeletonHead;
    @Unique
    public ModelRenderer ssb$bipedHeadwear;

    @Inject(method = "<init>(IIII)V", at = @At(value = "TAIL"))
    private void addHeadwear(int p_i1155_1_, int p_i1155_2_, int p_i1155_3_, int p_i1155_4_, CallbackInfo ci) {
        ssb$bipedHeadwear = Utils.cloneModel(this, skeletonHead, 32, 0, true, Utils.BoxTransformType.HAT);
    }
}
