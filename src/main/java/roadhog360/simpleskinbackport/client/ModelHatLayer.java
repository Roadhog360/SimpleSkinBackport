package roadhog360.simpleskinbackport.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelHatLayer extends ModelRenderer {

    public ModelHatLayer(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_) {
        super(p_i1174_1_, p_i1174_2_, p_i1174_3_);
    }

    @Override
    public void render(float p_78785_1_) {
        if(isHatLayer()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        super.render(p_78785_1_);
        if(isHatLayer()) {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    protected boolean isHatLayer() {
        return true;
    }
}
