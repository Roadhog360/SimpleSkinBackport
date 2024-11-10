package roadhog360.simpleskinbackport.mixins.early;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roadhog360.simpleskinbackport.Tags;

import java.util.List;
import java.util.UUID;

@Mixin(ItemSkull.class)
public class MixinItemSkull extends Item {

    private IIcon roadhogHeadIcon;

    @Inject(method = "getSubItems", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void injectMySkull(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_, CallbackInfo ci, @Local int i) {
        if(i == 3) {
            ItemStack skull = new ItemStack(p_150895_1_, 1, 3);
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagCompound skullOwner = new NBTTagCompound();
            NBTUtil.func_152460_a(skullOwner, new GameProfile(UUID.fromString("390d0a39-5541-4e8e-8f07-115bb41684c4"), "Roadhog360"));
            nbt.setTag("SkullOwner", skullOwner);
            skull.setTagCompound(nbt);
            p_150895_3_.add(skull);
        }
    }

    @Inject(method = "registerIcons", at = @At(value = "TAIL"))
    private void injectIconRegisterForHead(IIconRegister register, CallbackInfo ci) {
        roadhogHeadIcon = register.registerIcon(Tags.MOD_ID + ":skull_roadhog360");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.getIconIndex(stack);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        if(stack.getItemDamage() == 3 && stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            String name = "";
            if (nbt.hasKey("SkullOwner", Constants.NBT.TAG_COMPOUND)) {
                name = nbt.getCompoundTag("SkullOwner").getString("Name");
            } else if (nbt.hasKey("SkullOwner", Constants.NBT.TAG_STRING)) {
                name = nbt.getString("SkullOwner");
            }
            if(name.equals("Roadhog360")) {
                return roadhogHeadIcon;
            }
        }
        return super.getIconIndex(stack);
    }
}
