package net.runelite.client.plugins.zulrah;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NpcID;

@Getter
@RequiredArgsConstructor
public enum ZulrahPhase {

    RANGE_CENTER(NpcID.ZULRAH, ZulrahPosition.CENTER),
    RANGE_EAST(NpcID.ZULRAH, ZulrahPosition.EAST),
    RANGE_SOUTH(NpcID.ZULRAH, ZulrahPosition.SOUTH),
    RANGE_WEST(NpcID.ZULRAH, ZulrahPosition.WEST),

    MELEE_CENTER(NpcID.ZULRAH_2043, ZulrahPosition.CENTER),
    MELEE_EAST(NpcID.ZULRAH_2043, ZulrahPosition.EAST),
    MELEE_SOUTH(NpcID.ZULRAH_2043, ZulrahPosition.SOUTH),
    MELEE_WEST(NpcID.ZULRAH_2043, ZulrahPosition.WEST),

    MAGE_CENTER(NpcID.ZULRAH_2044, ZulrahPosition.CENTER),
    MAGE_EAST(NpcID.ZULRAH_2044, ZulrahPosition.EAST),
    MAGE_SOUTH(NpcID.ZULRAH_2044, ZulrahPosition.SOUTH),
    MAGE_WEST(NpcID.ZULRAH_2044, ZulrahPosition.WEST);

    private final int npcId;
    private final ZulrahPosition position;

}
