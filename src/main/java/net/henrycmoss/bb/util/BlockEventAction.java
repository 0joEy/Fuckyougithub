package net.henrycmoss.bb.util;

@FunctionalInterface
public interface BlockEventAction<E, B, L> {

    void accept(E player, B position, L level);
}
