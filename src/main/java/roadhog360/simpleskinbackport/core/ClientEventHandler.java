package roadhog360.simpleskinbackport.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
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
    }

    private void checkAndSetArmsState(Entity entity, Render render) {
        if(render instanceof RenderPlayer renderPlayer && renderPlayer.modelBipedMain instanceof ISwitchableArmsModel model
            && entity instanceof INewModelData player) {
            model.setArmState(player.simpleSkinBackport$isSlim());
        }
    }
}
