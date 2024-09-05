package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import roadhog360.simpleskinbackport.ducks.IArmsState;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IArmsState {

    @Unique
    private boolean simpleSkinBackport$slim = false;

    @Override
    public boolean simpleSkinBackport$isSlim() {
        return simpleSkinBackport$slim;
    }

    @Override
    public void simpleSkinBackport$setSlim(boolean slim) {
        simpleSkinBackport$slim = slim;
    }
}
