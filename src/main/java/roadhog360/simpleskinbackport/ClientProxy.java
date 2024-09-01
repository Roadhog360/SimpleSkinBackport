package roadhog360.simpleskinbackport;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;

import java.io.File;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

//        TextureManager texManager = Minecraft.getMinecraft().renderEngine;
//        File skinFolder = new File(Minecraft.getMinecraft().fileAssets, "skins");
//        MinecraftSessionService sessionService = Minecraft.getMinecraft().func_152347_ac(); // getSessionService
//        Minecraft.getMinecraft().field_152350_aA/*skinManager*/ = new NewSkinManager(Minecraft.getMinecraft().func_152342_ad()/*getSkinManager*/, texManager, skinFolder, sessionService);
    }
}
