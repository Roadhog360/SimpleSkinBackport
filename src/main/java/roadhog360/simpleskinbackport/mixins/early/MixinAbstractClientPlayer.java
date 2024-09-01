package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import roadhog360.simpleskinbackport.core.Utils;
import roadhog360.simpleskinbackport.ducks.ISlimModelData;

import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer implements SkinManager.SkinAvailableCallback, ISlimModelData {

    boolean checkedBase64Data;

    public MixinAbstractClientPlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
        super(p_i45324_1_, p_i45324_2_);
    }

    @Redirect(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;locationStevePng:Lnet/minecraft/util/ResourceLocation;"))
    private ResourceLocation setNewDefaultSkinBehavior() {
        simpleSkinBackport$checkForUpdate(Utils.isDefaultSkinSlim(getUniqueID()));
        return Utils.getDefaultSkin(getUniqueID());
    }

    @WrapOperation(method = "getLocationSkin()Lnet/minecraft/util/ResourceLocation;",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;locationSkin:Lnet/minecraft/util/ResourceLocation;", ordinal = 1))
    private ResourceLocation setNewSkinModel(AbstractClientPlayer instance, Operation<ResourceLocation> original) {
        if(!checkedBase64Data && instance.getGameProfile().getProperties().containsKey("textures")) {
            for(Property property : instance.getGameProfile().getProperties().get("textures")) {
                if(property.getName().equals("textures")) {
                    simpleSkinBackport$checkForUpdate(Utils.getSlimFromBase64Data(property.getValue()));
                    checkedBase64Data = true;
                    break;
                }
            }
        }
        return original.call(instance);
    }

    @Unique
    private void simpleSkinBackport$checkForUpdate(boolean slim) {
        if(simpleSkinBackport$isSlim() != slim) {
            simpleSkinBackport$setSlim(slim);
            simpleSkinBackport$setNeedsUpdate(true);
        }
    }
}
