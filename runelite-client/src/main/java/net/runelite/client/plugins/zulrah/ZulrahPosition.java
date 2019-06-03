package net.runelite.client.plugins.zulrah;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;


@Getter
@RequiredArgsConstructor
public enum ZulrahPosition {

        CENTER(new WorldPoint(11492,6243,0)),
        EAST(new WorldPoint(10158,7201,0)),
        SOUTH(new WorldPoint(8996,7576,0)),
        WEST(new WorldPoint(8986,7585,0));

        private final WorldPoint position;

}
