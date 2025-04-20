package net.henrycmoss.bb.recipe;

import java.util.Arrays;

public enum ElectrolysisResultType {

    SOLID(0),
    LIQUID(1),
    GAS(2);

    private final int id;

    ElectrolysisResultType(int id) {
        this.id = id;
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
