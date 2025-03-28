package roadhog360.simpleskinbackport.mixins.early;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import roadhog360.simpleskinbackport.ducks.IArmsState;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IArmsState {

    @Unique
    private boolean ssb$slim = false;

    @Override
    public boolean ssb$isSlim() {
        return ssb$slim;
    }

    @Override
    public void ssb$setSlim(boolean slim) {
        ssb$slim = slim;
    }
}
