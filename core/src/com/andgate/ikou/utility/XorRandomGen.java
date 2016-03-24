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
}
