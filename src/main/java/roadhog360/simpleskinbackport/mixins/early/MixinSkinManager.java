package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import roadhog360.simpleskinbackport.Tags;
import roadhog360.simpleskinbackport.client.ImageBufferDownloadPlayerSkin;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.IArmsState;

@Mixin(value = SkinManager.class, priority = 1100)
public class MixinSkinManager {
    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation changeDownloadLocation(String p_i1293_1_, Operation<ResourceLocation> original,
                                                    @Local(argsOnly = true) final MinecraftProfileTexture.Type p_152789_2_,
                                                    @Local(argsOnly = true) final SkinManager.SkinAvailableCallback p_152789_3_
                                                    ) {
        if(Utils.isPlayer(p_152789_2_, p_152789_3_)) {
            return new ResourceLocation(Tags.MOD_ID, p_i1293_1_);
        }
        return original.call(p_i1293_1_);
    }

    @WrapOperation(method = "func_152789_a", at = @At(value = "NEW", target = "()Lnet/minecraft/client/renderer/ImageBufferDownload;"))
    private ImageBufferDownload changeDownloadBufferManager(Operation<ImageBufferDownload> original,
                                                            @Local(argsOnly = true) MinecraftProfileTexture p_152789_1_,
                                                            @Local(argsOnly = true) MinecraftProfileTexture.Type p_152789_2_,
                                                            @Local(argsOnly = true) SkinManager.SkinAvailableCallback p_152789_3_) {
        if(Utils.isPlayer(p_152789_2_, p_152789_3_)) {
            return new ImageBufferDownloadPlayerSkin(p_152789_1_, p_152789_3_);
        }
        return original.call();
    }

    @Inject(method = "func_152789_a",
        at = @At(value = "TAIL", target = "Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;func_152121_a(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/util/ResourceLocation;)V"))
    private void injectCallbackSkinCheck(MinecraftProfileTexture p_152789_1_, MinecraftProfileTexture.Type p_152789_2_, SkinManager.SkinAvailableCallback p_152789_3_, CallbackInfoReturnable<ResourceLocation> cir) {
        if(Utils.isPlayer(p_152789_2_, p_152789_3_) && p_152789_3_ instanceof IArmsState playerData) {
            Utils.setSlimFromMetadata(p_152789_1_, playerData);
        }
    }

    //Temp disabled, from LAN skin fix mod
//    @Shadow
//    @Final
//    private static ExecutorService field_152794_b;
//    @Shadow
//    @Final
//    private MinecraftSessionService sessionService;
//
//    @Shadow
//    public ResourceLocation func_152789_a(MinecraftProfileTexture p_152789_1_, final MinecraftProfileTexture.Type p_152789_2_, final SkinManager.SkinAvailableCallback p_152789_3_)
//    {
//        return new ResourceLocation("jss2a98aj");
//    }
//
//    /**
//     * @author
//     * @reason
//     */
//    @Overwrite
//    public void func_152790_a(final GameProfile p_152790_1_, final SkinManager.SkinAvailableCallback p_152790_2_, final boolean p_152790_3_)
//    {
//        // SkinManager$3
//// METHOD INSTRUCTIONS CHANGED
//        field_152794_b.submit(() -> {
//            final HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture> hashmap = Maps.newHashMap();
//
//            try
//            {
//                hashmap.putAll(sessionService.getTextures(p_152790_1_, p_152790_3_));
//            }
//            catch (InsecureTextureException ignored) {}
//
//            if (hashmap.isEmpty() && p_152790_1_.getId().equals(Minecraft.getMinecraft().getSession().func_148256_e().getId()))
//            {
//                // CHANGED LINE
//                // Replaced "SkinManager.this.sessionService.fillProfileProperties(p_152790_1_, false)" with "Minecraft.getMinecraft().getSession().getProfile()" below.
//                hashmap.putAll(sessionService.getTextures(Minecraft.getMinecraft().getSession().func_148256_e(), false));
//            }
//
//            Minecraft.getMinecraft().func_152344_a(() -> {
//                if (hashmap.containsKey(MinecraftProfileTexture.Type.SKIN))
//                {
//                    func_152789_a(hashmap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, p_152790_2_);
//                }
//
//                if (hashmap.containsKey(MinecraftProfileTexture.Type.CAPE))
//                {
//                    func_152789_a(hashmap.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, p_152790_2_);
//                }
//            });
//        });
//    }
}
