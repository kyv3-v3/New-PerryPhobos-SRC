/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.util;

public class TimerUtil {
    private long time = -1L;

    public boolean passedS(double s) {
        return this.passedMs((long)s * 1000L);
    }

    public boolean passedDms(double dms) {
        return this.passedMs((long)dms * 10L);
    }

    public boolean passedDs(double ds) {
        return this.passedMs((long)ds * 100L);
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public void setMs(long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }

    public boolean passedNS(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public TimerUtil reset() {
        this.time = System.nanoTime();
        return this;
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }

    public boolean sleep(long l) {
        if (System.nanoTime() / 1000000L - l >= l) {
            this.reset();
            return true;
        }
        return false;
    }
}

