package net.ck.mtbg.map;

import lombok.Getter;

/**
 * * <a href="https://www.baeldung.com/java-convert-enums#:~:text=When%20converting%20between%20enums%20with,to%20declare%20the%20enum%20constant">
 * * https://www.baeldung.com/java-convert-enums#:~:text=When%20converting%20between%20enums%20with,to%20declare%20the%20enum%20constant
 * * </a>.
 *
 * @author Claus
 */
public enum TileTypes
{
    DESERT(MapShortHand.ds),
    GRASS(MapShortHand.gs),
    HILL(MapShortHand.hl),
    MOUNTAIN(MapShortHand.mt),
    OCEAN(MapShortHand.oc),
    RIVERE(MapShortHand.re),
    RIVERN(MapShortHand.rn),
    RIVER(MapShortHand.rr),
    RIVERS(MapShortHand.rs),
    RIVERW(MapShortHand.rw),
    SWAMP(MapShortHand.sw),
    LADDERUP(MapShortHand.lu),
    LADDERDOWN(MapShortHand.ld),
    STAIRSUP(MapShortHand.su),
    STAIRSDOWN(MapShortHand.sd),
    CASTLEENTRANCE(MapShortHand.ce),
    TOWNENTRANCE(MapShortHand.te),
    VILLAGEENTRANCE(MapShortHand.ve),
    CASTLEWEST(MapShortHand.cw),
    CASTLEEAST(MapShortHand.ce),
    STONEWALL(MapShortHand.sw),
    STONEWINDOW(MapShortHand.ws),
    WOODWALL(MapShortHand.ww),
    WOODWINDOW(MapShortHand.wi),
    GATEOPEN(MapShortHand.go),
    GATECLOSED(MapShortHand.gc),
    WOODDOOROPEN(MapShortHand.wd),
    WOODDOORCLOSED(MapShortHand.wc),
    STONEDOOROPEN(MapShortHand.sd),
    STONEDOORCLOSED(MapShortHand.sc),
    FOUNTAIN(MapShortHand.fn),
    WELL(MapShortHand.we),
    DIRTROAD(MapShortHand.dr),
    PAVEDROAD(MapShortHand.pr),
    WOODFLOOR(MapShortHand.wf),
    STONEFLOOR(MapShortHand.sf),
    MARBLEFLOOR(MapShortHand.mf),
    DIRTFLOOR(MapShortHand.df),
    CAVEENTRANCE(MapShortHand.ce),
    LIGHTFOREST(MapShortHand.lf),
    SHALLOWOCEAN(MapShortHand.so),
    REEF(MapShortHand.rf),
    BUSHES(MapShortHand.bs),
    BUSH(MapShortHand.bs),
    LAVA(MapShortHand.lv),
    STEEPMOUNTAIN(MapShortHand.sm),
    DENSEFOREST(MapShortHand.df),
    SIGNPOST(MapShortHand.sp);

    @Getter
    private final MapShortHand mapShortHand;

    TileTypes(MapShortHand mapShortHand)
    {
        this.mapShortHand = mapShortHand;
    }
}

