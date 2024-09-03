package roadhog360.simpleskinbackport.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import roadhog360.simpleskinbackport.ducks.INewModelData;
import roadhog360.simpleskinbackport.ducks.ISwitchableArmsModel;

public class ClientEventHandler {
    public static final ClientEventHandler INSTANCE = new ClientEventHandler();
    @SubscribeEvent
    public void onHandRender(RenderHandEvent event) {
        checkAndSetArmsState(FMLClientHandler.instance().getClientPlayerEntity(), RenderManager.instance.getEntityRenderObject(FMLClientHandler.instance().getClientPlayerEntity()));
    }

    @SubscribeEvent
    public void onRenderEntity(RenderLivingEvent.Pre event) {
        checkAndSetArmsState(event.entity, event.renderer);
        if(event.renderer instanceof RenderPlayer renderPlayer && event.entity instanceof EntityPlayer living) {
            ItemStack itemstack = living.getEquipmentInSlot(4);
            renderPlayer.modelBipedMain.bipedHeadwear.showModel = itemstack == null || !isHead(itemstack.getItem());
        }
    }

    private void checkAndSetArmsState(Entity entity, Render render) {
        if(render instanceof RenderPlayer renderPlayer && renderPlayer.modelBipedMain instanceof ISwitchableArmsModel model
            && entity instanceof INewModelData player) {
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
