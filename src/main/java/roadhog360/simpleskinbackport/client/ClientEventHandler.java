package roadhog360.simpleskinbackport.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import roadhog360.simpleskinbackport.ducks.IArmsState;
import roadhog360.simpleskinbackport.ducks.INewBipedModel;

public class ClientEventHandler {
    public static final ClientEventHandler INSTANCE = new ClientEventHandler();
    @SubscribeEvent
    public void onHandRender(RenderHandEvent event) {
        checkAndSetArmsState(FMLClientHandler.instance().getClientPlayerEntity(), RenderManager.instance.getEntityRenderObject(FMLClientHandler.instance().getClientPlayerEntity()));
    }

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre event) {
        checkAndSetArmsState(event.entity, event.renderer);
        if(event.entity instanceof EntityLiving living) {
            ItemStack itemstack = living.getEquipmentInSlot(4);
            boolean showHeadwear = itemstack == null || !isHead(itemstack.getItem());
            if(event.renderer instanceof RenderPlayer renderPlayer) {
                renderPlayer.modelBipedMain.bipedHeadwear.showModel = showHeadwear;
            } else if (event.renderer instanceof RenderBiped renderBiped) {
                renderBiped.modelBipedMain.bipedHeadwear.showModel = showHeadwear;
            }
        }
    }

    private void checkAndSetArmsState(Entity entity, Render render) {
        ModelBiped modelBiped;
        if(render instanceof RenderPlayer renderPlayer) {
            modelBiped = renderPlayer.modelBipedMain;
        } else if (render instanceof RenderBiped renderBiped) {
            modelBiped = renderBiped.modelBipedMain;
        } else {
            return;
        }
        if(modelBiped instanceof INewBipedModel model && entity instanceof IArmsState player) {
            model.simpleSkinBackport$setSlim(player.simpleSkinBackport$isSlim());
        }
    }

    private boolean isHead(Item item) {
        if(item instanceof ItemSkull) {
            return true;
        }
        return item.getClass().getName().equals("chylex.hee.item.ItemEndermanHead");//These don't extend ItemSkull
    }
}
