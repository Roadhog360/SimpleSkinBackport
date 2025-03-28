package roadhog360.simpleskinbackport.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.IArmsState;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;

public class ClientEventHandler {
    public static final ClientEventHandler INSTANCE = new ClientEventHandler();
    @SubscribeEvent
    public void onHandRender(RenderHandEvent event) {
        checkAndSetArmsState(FMLClientHandler.instance().getClientPlayerEntity(), RenderManager.instance.getEntityRenderObject(FMLClientHandler.instance().getClientPlayerEntity()));
    }

    private static boolean hidingHeadwear;

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre event) {
        checkAndSetArmsState(event.entity, event.renderer);

        ModelRenderer headwear;
        if(event.renderer instanceof RenderPlayer renderPlayer) {
            headwear = renderPlayer.modelBipedMain.bipedHeadwear;
        } else if (event.renderer instanceof RenderBiped renderBiped) {
            headwear = renderBiped.modelBipedMain.bipedHeadwear;
        } else {
            return;
        }

        ItemStack itemstack = event.entity.getEquipmentInSlot(4);
        boolean showHeadwear = itemstack == null || !isHead(itemstack.getItem());

        if(headwear.showModel && !showHeadwear) {
            hidingHeadwear = true;
            headwear.showModel = false;
        }
    }

    public void onPostRenderEntity(RenderLivingEvent.Post event) {
        ModelRenderer headwear;
        if(event.renderer instanceof RenderPlayer renderPlayer) {
            headwear = renderPlayer.modelBipedMain.bipedHeadwear;
        } else if (event.renderer instanceof RenderBiped renderBiped) {
            headwear = renderBiped.modelBipedMain.bipedHeadwear;
        } else {
            return;
        }

        if(hidingHeadwear) {
            headwear.showModel = true;
            hidingHeadwear = false;
        }
    }

    private void checkAndSetArmsState(Entity entity, Render render) {
        ModelBiped modelBiped = null;
        if(render instanceof RenderPlayer renderPlayer) {
            modelBiped = renderPlayer.modelBipedMain;
        } else if (render instanceof RenderBiped renderBiped) {
            modelBiped = renderBiped.modelBipedMain;
        }
        if(modelBiped instanceof INewBipedModel model) {
            if(entity instanceof IArmsState player) {
                model.ssb$setSlim(player.ssb$isSlim());
            } else if(Utils.rendererCopiesPlayerSkin(render)) {
                model.ssb$setSlim(Utils.isClientPlayerSlim());
            }
        }
    }

    private boolean isHead(Item item) {
        if(item instanceof ItemSkull) {
            return true;
        }
        return item.getClass().getName().equals("chylex.hee.item.ItemEndermanHead");//These don't extend ItemSkull
    }
}
