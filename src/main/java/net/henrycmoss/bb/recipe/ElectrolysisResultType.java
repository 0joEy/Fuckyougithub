package net.henrycmoss.bb.recipe;

import net.henrycmoss.bb.block.entity.ElectrolyticCellBlockEntity;
import net.henrycmoss.bb.util.BbTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ElectrolysisResultType {

    SOLID(0, "solids"),
    LIQUID(1, "liquids"),
    GAS(2, "gasses");

    private final int id;
    private final String name;

    private final Map<ElectrolysisResultType, Integer[]> slots = new HashMap<>();

    ElectrolysisResultType(int id, String name) {
        this.id = id;
        this.name = name;
        int[] i1 = {3, 4};
        int[] i2 = {5, 6};
        if(name.equals("gasses") || name.equals("liquids")) slots.put(fromId(id), (Integer[]) Arrays.stream(i1).boxed().toArray());
        else slots.put(fromId(id), (Integer[]) Arrays.stream(i1).boxed().toArray());
    }

    public int getId() {
        return id;
    }

    public ElectrolysisResultType fromId(int id) {
        for(ElectrolysisResultType type : ElectrolysisResultType.values()) {
            if(type.getId() == id) return type;
        }
        return null;
    }
}
