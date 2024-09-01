package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import roadhog360.simpleskinbackport.ducks.ISlimModelData;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements ISlimModelData {

    @Unique
    private boolean simpleSkinBackport$slim;
    @Unique
    private boolean simpleSkinBackport$needsUpdate;

    @Override
    public boolean simpleSkinBackport$isSlim() {
        return simpleSkinBackport$slim;
    }

    @Override
    public void simpleSkinBackport$setSlim(boolean slim) {
        simpleSkinBackport$slim = slim;
    }

    @Override
    public boolean simpleSkinBackport$needsModelUpdate() {
        return simpleSkinBackport$needsUpdate;
    }

    @Override
    public void simpleSkinBackport$setNeedsUpdate(boolean update) {
        simpleSkinBackport$needsUpdate = update;
    }
}
