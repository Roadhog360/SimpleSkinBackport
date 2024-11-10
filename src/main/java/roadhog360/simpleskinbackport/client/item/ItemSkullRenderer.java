package roadhog360.simpleskinbackport.client.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.opengl.GL11;

public class ItemSkullRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GameProfile profile = stack.hasTagCompound() ? getGameProfile(stack) : null;

        switch (type) {
            case ENTITY:
                renderSkull(-0.25F, -0.5F, -0.5F, stack.getItemDamage(), profile);
                break;
            case EQUIPPED:
                renderSkull(0.5F, 0.0F, 0.0F, stack.getItemDamage(), profile);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderSkull(0.5F, 0.35F, 0.25F, stack.getItemDamage(), profile);
                break;
            case INVENTORY:
                GL11.glScaled(1.5, 1.5, 1.5);
                renderSkull(0.75F, 0.30F, 0.5F, stack.getItemDamage(), profile);
                break;
            default:
                break;
        }
    }

    private void renderSkull(float x, float y, float z, int meta, GameProfile name) {
        if(TileEntitySkullRenderer.field_147536_b != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, z);
            TileEntitySkullRenderer.field_147536_b.func_152674_a(0, 0, 0, 0, 0, meta, name);
            GL11.glPopMatrix();
        }
    }

    private GameProfile getGameProfile(ItemStack stack) {
        GameProfile profile = null;

        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey("SkullOwner", Constants.NBT.TAG_COMPOUND))
                profile = NBTUtil.func_152459_a(nbt.getCompoundTag("SkullOwner")); // readGameProfileFromNBT
            else if (nbt.hasKey("SkullOwner", Constants.NBT.TAG_STRING))
                profile = new GameProfile(null, nbt.getString("SkullOwner"));
        }

        return profile;
    }
}
