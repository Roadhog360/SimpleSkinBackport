package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Lists;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ArmPair extends Pair<List<ModelBox>, List<ModelBox>> {

    private final List<ModelBox> left;
    private final Integer leftDisplayList;
    private final List<ModelBox> right;
    private final Integer rightDisplayList;

    private ArmPair(List<ModelBox> left, Integer leftDisplayList, List<ModelBox> right, Integer rightDisplayList) {
        this.left = left;
        this.leftDisplayList = leftDisplayList;
        this.right = right;
        this.rightDisplayList = rightDisplayList;
    }

    public static ArmPair of(ModelRenderer left, ModelRenderer right) {
        return new ArmPair(Lists.newArrayList(left.cubeList), Utils.createDisplaylistFor(left),
            Lists.newArrayList(right.cubeList), Utils.createDisplaylistFor(right));
    }

    @Override
    public List<ModelBox> getLeft() {
        return left;
    }

    public Integer getLeftDisplayList() {
        return leftDisplayList;
    }

    @Override
    public List<ModelBox> getRight() {
        return right;
    }

    public Integer getRightDisplayList() {
        return rightDisplayList;
    }

    @Override
    public List<ModelBox> setValue(List<ModelBox> value) {
        throw new UnsupportedOperationException();
    }
}
