package net.runelite.client.plugins.zulrah;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NpcID;

@Getter
@RequiredArgsConstructor
public enum ZulrahPhase {

    RANGE_CENTER(NpcID.ZULRAH, ZulrahPosition.CENTER, "Range: Center"),
    RANGE_EAST(NpcID.ZULRAH, ZulrahPosition.EAST, "Range: East"),
    RANGE_SOUTH(NpcID.ZULRAH, ZulrahPosition.SOUTH, "Range: South"),
    RANGE_WEST(NpcID.ZULRAH, ZulrahPosition.WEST, "Range: West"),

    MELEE_CENTER(NpcID.ZULRAH_2043, ZulrahPosition.CENTER, "Melee: Center"),
    MELEE_EAST(NpcID.ZULRAH_2043, ZulrahPosition.EAST, "Melee: East"),
    MELEE_SOUTH(NpcID.ZULRAH_2043, ZulrahPosition.SOUTH, "Melee: South"),
    MELEE_WEST(NpcID.ZULRAH_2043, ZulrahPosition.WEST, "Melee: West"),

    MAGE_CENTER(NpcID.ZULRAH_2044, ZulrahPosition.CENTER, "Mage: Center"),
    MAGE_EAST(NpcID.ZULRAH_2044, ZulrahPosition.EAST, "Mage: East"),
    MAGE_SOUTH(NpcID.ZULRAH_2044, ZulrahPosition.SOUTH, "Mage: South"),
    MAGE_WEST(NpcID.ZULRAH_2044, ZulrahPosition.WEST, "Mage: West"),

    JAD_RANGE_CENTER(NpcID.ZULRAH, ZulrahPosition.CENTER, "Jad, Range-first: Center"),
    JAD_RANGE_EAST(NpcID.ZULRAH, ZulrahPosition.EAST, "Jad, Range-first: East"),
    JAD_RANGE_SOUTH(NpcID.ZULRAH, ZulrahPosition.SOUTH, "Jad, Range-first: South"),
    JAD_RANGE_WEST(NpcID.ZULRAH, ZulrahPosition.WEST, "Jad, Range-first: West"),

    JAD_MAGE_CENTER(NpcID.ZULRAH, ZulrahPosition.CENTER, "Jad, Mage-first: Center"),
    JAD_MAGE_EAST(NpcID.ZULRAH, ZulrahPosition.EAST, "Jad, Mage-first: East"),
    JAD_MAGE_SOUTH(NpcID.ZULRAH, ZulrahPosition.SOUTH, "Jad, Mage-first: South"),
    JAD_MAGE_WEST(NpcID.ZULRAH, ZulrahPosition.WEST, "Jad, Mage-first: West");

    private final int npcId;
    private final ZulrahPosition position;
    private final String phaseName;

}
