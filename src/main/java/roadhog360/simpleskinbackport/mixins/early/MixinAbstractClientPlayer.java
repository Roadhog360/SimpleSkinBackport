package roadhog360.simpleskinbackport.mixins.early;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.IArmsState;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer implements SkinManager.SkinAvailableCallback, IArmsState {

    public MixinAbstractClientPlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
        super(p_i45324_1_, p_i45324_2_);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void setDefaultSkinState(World p_i45074_1_, GameProfile p_i45074_2_, CallbackInfo ci) {
        simpleSkinBackport$setSlim(Utils.isDefaultSkinSlim(getPersistentID()));
    }

    @Redirect(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;locationStevePng:Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation setNewDefaultSkinBehavior() {
        return Utils.getDefaultSkin(getPersistentID());
    }

//    UUID uuid = UUID.randomUUID();
//    /**
//     * DEBUG CODE: Force loads a default skin instead of the player's
//     * @param instance
//     * @param original
//     * @return
//     */
//    @WrapOperation(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;",
//        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;locationSkin:Lnet/minecraft/util/ResourceLocation;", ordinal = 1))
//    private ResourceLocation setNewSkinModel(AbstractClientPlayer instance, Operation<ResourceLocation> original) {
//        boolean slim = Utils.isDefaultSkinSlim(uuid);
//        ResourceLocation skin = Utils.getDefaultSkin(uuid);
//        simpleSkinBackport$setSlim(slim);
//        return skin;
//    }
}
