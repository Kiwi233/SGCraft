//------------------------------------------------------------------------------------------------
//
//   SG Craft - Dimension map
//
//------------------------------------------------------------------------------------------------
package gcewing.sg;

import com.google.common.primitives.Ints;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SGDimensionMap extends WorldSavedData {

    public static final boolean debugDimensionMap = false;

    protected final List<Integer> indexToDimension = new ArrayList<>();
    protected final Map<Integer, Integer> dimensionToIndex = new HashMap<>();

    public SGDimensionMap(String name) {
        super(name);
    }

    public static SGDimensionMap get() {
        World world = BaseUtils.getWorldForDimension(0);
        return BaseUtils.getWorldData(world, SGDimensionMap.class, "sgcraft:dimension_map");
    }

    public static Integer dimensionForIndex(int index) {
        return get().getDimensionForIndex(index);
    }

    public static Integer indexForDimension(int dimension) {
        return get().getIndexForDimension(dimension);
    }

    protected Integer getDimensionForIndex(int index) {
        Integer dimension = null;
        if (index >= 0 && index < indexToDimension.size())
            dimension = indexToDimension.get(index);
        if (debugDimensionMap)
            System.out.printf("SGDimensionMap: Found index %s -> dimension %s\n", index, dimension);
        return dimension;
    }

    protected Integer getIndexForDimension(int dimension) {
        if (!dimensionToIndex.containsKey(dimension)) {
            int index = indexToDimension.size();
            if (debugDimensionMap)
                System.out.printf("SGDimensionMap: Adding dimension %s -> index %s\n", dimension, index);
            indexToDimension.add(dimension);
            dimensionToIndex.put(dimension, index);
            markDirty();
            return index;
        } else {
            int index = dimensionToIndex.get(dimension);
            if (debugDimensionMap)
                System.out.printf("SGDimensionMap: Found dimension %s -> index %s\n", dimension, index);
            return index;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (debugDimensionMap)
            System.out.print("SGDimensionMap: Reading from nbt\n");
        int[] a = nbt.getIntArray("dimensions");
        for (int i = 0; i < a.length; i++) {
            indexToDimension.add(a[i]);
            dimensionToIndex.put(a[i], i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (debugDimensionMap)
            System.out.print("SGDimensionMap: Writing to nbt\n");
        int[] a = Ints.toArray(indexToDimension);
        nbt.setIntArray("dimensions", a);
    }

}
