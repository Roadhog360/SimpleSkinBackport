package roadhog360.simpleskinbackport.mixins.late.botania;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import roadhog360.simpleskinbackport.core.DefaultPlayerSkin;
import vazkii.botania.client.model.ModelSkullOverride;
import vazkii.botania.client.render.tile.RenderTileSkullOverride;

@Mixin(RenderTileSkullOverride.class)
public class MixinRenderTileSkullOverride extends TileEntitySkullRenderer {

    private ModelSkeletonHead skull = new ModelSkeletonHead(0, 0, 64, 64);
    //The new one isn't needed

    /**
     * 64x64 model needs new Steve textures
     */
    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;locationStevePng:Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation fixDefaultTexture() {
        return DefaultPlayerSkin.STEVE.getResource();
    }

    /**
     * Botania's model override isn't necessary; it's easier to provide the vanilla one to set the 64x64 textures.
     * We need the 64x64 texture head since Gaia Guardians copy the player's texture, which uses 64x64 textures.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lvazkii/botania/client/model/ModelSkullOverride;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void changeModel(ModelSkullOverride instance, Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        skull.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }
}
