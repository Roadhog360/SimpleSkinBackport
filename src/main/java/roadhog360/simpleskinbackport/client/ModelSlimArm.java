package roadhog360.simpleskinbackport.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

 /**
 * Was hoping to use this class to make the arm states toggleable instead of creating two separate objects.
 * However specifically on the right arm, the boxSlim seems to inherit the exact values of boxWide regardless of what I make its args.
 * Hopefully I can switch back to this when I find out why.
 * Leaving this here so anyone who knows can crack the mystery.
 */
@Deprecated
public class ModelSlimArm extends ModelHatLayer {


    private boolean hatLayer;
    private final boolean rightArm;

    private boolean slim;

    private float rotationPointXWide;
    private float rotationPointXSlim;
    private float rotationPointYWide;
    private float rotationPointYSlim;
    //Z Adjustments remain consistent across both arms

    private ModelBox boxWide;
    private ModelBox boxSlim;

    public ModelSlimArm(ModelBase p_i1174_1_, int p_i1174_2_, int p_i1174_3_, boolean isRightArm) {
        super(p_i1174_1_, p_i1174_2_, p_i1174_3_);
        this.rightArm = isRightArm;
    }

    @Override
    public void setRotationPoint(float p_78793_1_, float p_78793_2_, float p_78793_3_) {
        rotationPointXWide = p_78793_2_;
        rotationPointXSlim = p_78793_2_ + (rightArm ? 1F : 0);
        rotationPointYWide = p_78793_2_;
        rotationPointYSlim = p_78793_2_ + 0.5F;
        super.setRotationPoint(p_78793_1_, p_78793_2_, p_78793_3_);
    }

    @Override
    public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_) {
        super.addBox(p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_);
        boxWide = createBox(p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_);
        boxSlim = createBox(p_78790_1_ + (rightArm ? 1 : 0), p_78790_2_, p_78790_3_, p_78790_4_ - 1, p_78790_5_, p_78790_6_, p_78790_7_);
    }

    private ModelBox createBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_) {
        return new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_);
    }

    public void setSlim(boolean slim) {
        if(slim) {
            cubeList.set(0, boxSlim);
            rotationPointX = rotationPointXSlim;
            rotationPointY = rotationPointYSlim;
        } else {
            cubeList.set(0, boxWide);
            rotationPointX = rotationPointXWide;
            rotationPointY = rotationPointYWide;
        }
        this.slim = slim;
        if(childModels != null) {
            for (ModelRenderer child : childModels) {
                if (child instanceof ModelSlimArm armwear) {
                    armwear.setSlim(slim);
                }
            }
        }
    }

    public ModelSlimArm setAsHatLayer() {
        hatLayer = true;
        return this;
    }

    public boolean isSlim() {
        return slim;
    }

    @Override
    public boolean isHatLayer() {
        return hatLayer;
    }
}
