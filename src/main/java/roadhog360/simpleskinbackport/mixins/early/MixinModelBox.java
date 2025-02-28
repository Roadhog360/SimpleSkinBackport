package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.ducks.IBoxSizeGetter;
import roadhog360.simpleskinbackport.ducks.ITransparentBox;

@Mixin(ModelBox.class)
public class MixinModelBox implements ITransparentBox, IBoxSizeGetter {

    @Unique
    private boolean simpleSkinBackport$isTransparent;
    @Unique
    private float simpleSkinBackport$boxSize;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void getBoxSize(ModelRenderer p_i1171_1_, int p_i1171_2_, int p_i1171_3_, float p_i1171_4_, float p_i1171_5_, float p_i1171_6_, int p_i1171_7_, int p_i1171_8_, int p_i1171_9_, float p_i1171_10_, CallbackInfo ci) {
        simpleSkinBackport$boxSize = p_i1171_10_;
    }

    @Override
    public float simpleSkinBackport$getSize() {
        return simpleSkinBackport$boxSize;
    }

    @Override
    public boolean simpleSkinBackport$isTransparent() {
        return simpleSkinBackport$isTransparent;
    }

    @Override
    public void simpleSkinBackport$setTransparent(boolean transparent) {
        simpleSkinBackport$isTransparent = transparent;
    }

    @Inject(method = "render", at = @At(value = "HEAD"))
    private void doTransparency(Tessellator p_78245_1_, float p_78245_2_, CallbackInfo ci) {
        if(simpleSkinBackport$isTransparent()) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void finishTransparency(Tessellator p_78245_1_, float p_78245_2_, CallbackInfo ci) {
        if(simpleSkinBackport$isTransparent()) {
            GL11.glPopAttrib();
        }
    }
}
