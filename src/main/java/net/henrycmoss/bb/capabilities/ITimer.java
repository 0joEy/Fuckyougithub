package net.henrycmoss.bb.capabilities;

public interface ITimer {
    int getTicks();
    void setTicks(int ticks);

    void decrement();
    void done();
}
