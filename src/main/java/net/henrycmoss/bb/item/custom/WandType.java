package net.henrycmoss.bb.item.custom;

public enum WandType {

    EARTH(0),
    LIGHTNING(1),
    FIRE(2),
    WIND(3),
    ;

    private final int id;

    WandType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static WandType getType(int id) {
        for(WandType type : WandType.values()) {
            if(type.getId() == id) return type;
        }
        return WandType.EARTH;
    }
}
