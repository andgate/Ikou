package com.andgate.ikou.utility;

public class XorRandomGen
{
    private long x;

    public XorRandomGen(long seed) {
        this.x = seed;
    }

    public long next()
    {
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        return x;
    }

    public long next(long radius) {
        return next() % radius;
    }

    public long next(long min, long max)
    {
        return Math.abs(next(max-min)) + min;
    }

    public int nextInt(int radius) {
        if(radius == 0) return 0;
        return (int)next() % (radius+1);
    }

    public int nextInt(int min, int max)
    {
        return Math.abs(nextInt(max-min)) + min;
    }

    public boolean nextBool()
    {
        return nextInt(0,1) != 0;
    }
}
