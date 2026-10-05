package net.henrycmoss.bb.util;

@FunctionalInterface
public interface TimedAction<P, L> {

    void accept(P player, L level);
}
