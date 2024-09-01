package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.awt.image.BufferedImage;

@Mixin(ImageBufferDownload.class)
public abstract class MixinImageBufferDownload implements IImageBuffer {

    @Redirect(method = "parseUserSkin",
        at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/renderer/ImageBufferDownload;imageHeight:I"))
    private void changeImageHeight(ImageBufferDownload instance, int value) {
        imageHeight = 64;
    }

    @Inject(method = "parseUserSkin",
        at = @At(value = "INVOKE", target = "Ljava/awt/Graphics;drawImage(Ljava/awt/Image;IILjava/awt/image/ImageObserver;)Z",
            shift = At.Shift.AFTER))
    private void injectOldSkinCompat(BufferedImage p_78432_1_, CallbackInfoReturnable<BufferedImage> cir,
                                     @Local Graphics graphics, @Local(name = "bufferedimage1") BufferedImage bufferedimage1) {
        if (p_78432_1_.getHeight() == 32) {
            graphics.drawImage(bufferedimage1, 24, 48, 20, 52, 4, 16, 8, 20, null);
            graphics.drawImage(bufferedimage1, 28, 48, 24, 52, 8, 16, 12, 20, null);
            graphics.drawImage(bufferedimage1, 20, 52, 16, 64, 8, 20, 12, 32, null);
            graphics.drawImage(bufferedimage1, 24, 52, 20, 64, 4, 20, 8, 32, null);
            graphics.drawImage(bufferedimage1, 28, 52, 24, 64, 0, 20, 4, 32, null);
            graphics.drawImage(bufferedimage1, 32, 52, 28, 64, 12, 20, 16, 32, null);
            graphics.drawImage(bufferedimage1, 40, 48, 36, 52, 44, 16, 48, 20, null);
            graphics.drawImage(bufferedimage1, 44, 48, 40, 52, 48, 16, 52, 20, null);
            graphics.drawImage(bufferedimage1, 36, 52, 32, 64, 48, 20, 52, 32, null);
            graphics.drawImage(bufferedimage1, 40, 52, 36, 64, 44, 20, 48, 32, null);
            graphics.drawImage(bufferedimage1, 44, 52, 40, 64, 40, 20, 44, 32, null);
            graphics.drawImage(bufferedimage1, 48, 52, 44, 64, 52, 20, 56, 32, null);
        }
    }

    @Shadow
    private void setAreaTransparent(int minX, int minY, int maxX, int maxY) {}

    @Shadow
    private void setAreaOpaque(int minX, int minY, int maxX, int maxY) {}

    @Shadow
    private int imageHeight;

    @Inject(method = "parseUserSkin", at = @At(value = "RETURN"))
    private void injectnewTransparentAreas(BufferedImage p_78432_1_, CallbackInfoReturnable<BufferedImage> cir) {
        setAreaTransparent(0, 32, 16, 48);
        setAreaTransparent(16, 32, 40, 48);
        setAreaTransparent(40, 32, 56, 48);
        setAreaTransparent(0, 48, 16, 64);
        setAreaOpaque(16, 48, 48, 64);
        setAreaTransparent(48, 48, 64, 64);
    }
}
