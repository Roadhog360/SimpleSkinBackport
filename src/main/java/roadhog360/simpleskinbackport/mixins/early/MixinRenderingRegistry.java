package roadhog360.simpleskinbackport.mixins.early;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;

import java.util.Map;

@Mixin(RenderingRegistry.class)
public class MixinRenderingRegistry {
    /**
     * We need to do this because the RenderManager#entityRenderMap is like the ONE THING that is out of reach of FMLLoadCompleteEvent...
     * If we do it there, then the map only has vanilla entities, nooo!!!!! And RenderManager only has modded entities by that point too... fuuuuuuuuu
     * I could do it on first client tick but that feels dirty. Might cause a lag spike if there's a lot of entity renderers (but probably not even on large packs admittedly)
     * This just feels cleaner, I want this to be handled on the load screen.
     */
    @Inject(method = "loadEntityRenderers", at = @At(value = "TAIL"), remap = false)
    private void setup64xModels(Map<Class<? extends Entity>, Render> rendererMap, CallbackInfo ci) {
        RenderManager.instance.entityRenderMap.forEach((entityClass, renderer) -> {
            ModelBiped modelBiped = null;
            if (renderer instanceof RenderPlayer renderPlayer) {
                modelBiped = renderPlayer.modelBipedMain;
            }
            if (renderer instanceof RenderBiped renderBiped) {
                modelBiped = renderBiped.modelBipedMain;
            }
            if (modelBiped instanceof INewBipedModel newBipedModel) {
                if (Utils.rendererCopiesPlayerSkin(renderer) || entityClass.isAssignableFrom(EntityPlayer.class)) {
                    newBipedModel.simpleSkinBackport$set64x();
                }
            }
        });
    }
}
