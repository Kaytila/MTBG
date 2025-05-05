package net.ck.mtbg.util.utils;

import net.ck.mtbg.map.MapTile;

public class BetterUILense
{
    /**
     * Singleton
     */
    private static final BetterUILense UILense = new BetterUILense();
    public MapTile[][] maptiles;

    /**
     * Singleton access - now I can use Lense in a lot of things :D
     */
    public static BetterUILense getCurrent()
    {
        return UILense;
    }
}
