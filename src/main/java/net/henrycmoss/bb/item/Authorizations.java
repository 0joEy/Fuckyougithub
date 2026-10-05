package net.henrycmoss.bb.item;

public enum Authorizations {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4),
    LEVEL_5(5);


    private int id;

    private Authorizations(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
