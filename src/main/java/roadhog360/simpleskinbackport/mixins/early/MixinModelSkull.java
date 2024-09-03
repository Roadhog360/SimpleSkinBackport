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
import roadhog360.simpleskinbackport.client.models.ModelHatLayer;

@Mixin(ModelSkeletonHead.class)
public abstract class MixinModelSkull extends ModelBase {

    @Shadow
    public ModelRenderer skeletonHead;
    @Unique
    ModelHatLayer simpleSkinBackport$bipedHeadwear;

    @Inject(method = "<init>(IIII)V", at = @At(value = "TAIL"))
    private void addHeadwear(int p_i1155_1_, int p_i1155_2_, int p_i1155_3_, int p_i1155_4_, CallbackInfo ci) {
        simpleSkinBackport$bipedHeadwear = new ModelHatLayer(this, 32, 0);
        simpleSkinBackport$bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1155_1_ + 0.5F);
        simpleSkinBackport$bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1155_2_, 0.0F);
        skeletonHead.addChild(simpleSkinBackport$bipedHeadwear);
    }
}
