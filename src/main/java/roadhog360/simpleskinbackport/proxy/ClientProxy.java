package roadhog360.simpleskinbackport.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Items;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import roadhog360.simpleskinbackport.client.ClientEventHandler;
import roadhog360.simpleskinbackport.client.item.ItemSkullRenderer;
import roadhog360.simpleskinbackport.core.compat.SmartRenderCompat;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.


    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        SmartRenderCompat.tryInitSmartRenderFields();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance().bus().register(ClientEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.INSTANCE);

        MinecraftForgeClient.registerItemRenderer(Items.skull, new ItemSkullRenderer());
    }
}
