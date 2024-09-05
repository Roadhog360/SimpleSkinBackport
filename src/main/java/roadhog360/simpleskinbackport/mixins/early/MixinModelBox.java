package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.ducks.ITransparentBox;

@Mixin(ModelBox.class)
public class MixinModelBox implements ITransparentBox, Cloneable {

    @Unique
    private boolean simpleSkinBackport$isTransparent;

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
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void finishTransparency(Tessellator p_78245_1_, float p_78245_2_, CallbackInfo ci) {
        GL11.glDisable(GL11.GL_BLEND);
    }
}
