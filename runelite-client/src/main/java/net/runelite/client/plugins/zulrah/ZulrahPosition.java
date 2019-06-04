package net.runelite.client.plugins.zulrah;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.LocalPoint;

@Getter
@RequiredArgsConstructor
public enum ZulrahPosition {

        CENTER(new LocalPoint(6720,7616)),
        EAST(new LocalPoint(8000,7360)),
        SOUTH(new LocalPoint(6720,6208)),
        WEST(new LocalPoint(5440,7360));


        private final LocalPoint position;

}
