package net.runelite.client.plugins.zulrah;

import lombok.Getter;
import net.runelite.api.coords.LocalPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ZulrahSafespots {

    PATTERN_1_SAFESPOTS(new LocalPoint(7232,8000), new LocalPoint(7232,8000)),
    PATTERN_2_SAFESPOTS(new LocalPoint(7232,8000), new LocalPoint(7232,8000)),
    PATTERN_3_SAFESPOTS(new LocalPoint(7232,8000), new LocalPoint(7232,8000)),
    PATTERN_4_SAFESPOTS(new LocalPoint(7232,8000), new LocalPoint(7232,8000));

    @Getter
    private static final List<LocalPoint> spots = new ArrayList<>();

    static
    {
        for (ZulrahSafespots safeSpot : values())
        {
            spots.addAll(Arrays.asList(safeSpot.safeSpots));
        }
    }

    private final LocalPoint[] safeSpots;

    ZulrahSafespots(LocalPoint... spots)
    {
        this.safeSpots = spots;
    }
}
