package net.runelite.client.plugins.zulrah;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static net.runelite.client.plugins.zulrah.ZulrahPhase.*;

@RequiredArgsConstructor
public enum ZulrahPatterns {

    PATTERN_1(Lists.newArrayList(RANGE_CENTER, MELEE_CENTER, MAGE_CENTER, RANGE_SOUTH, MELEE_CENTER, MAGE_WEST, RANGE_SOUTH, MAGE_SOUTH, JAD_RANGE_WEST, MELEE_CENTER)),
    PATTERN_2(Lists.newArrayList(RANGE_CENTER, MELEE_CENTER, MAGE_CENTER, RANGE_WEST, MAGE_SOUTH, MELEE_CENTER, RANGE_EAST, MAGE_SOUTH, JAD_RANGE_WEST, MELEE_CENTER)),
    PATTERN_3(Lists.newArrayList(RANGE_CENTER, RANGE_EAST, MELEE_CENTER, MAGE_WEST, RANGE_SOUTH, MAGE_EAST, RANGE_CENTER, RANGE_WEST, MAGE_CENTER, JAD_MAGE_EAST, MAGE_CENTER)),
    PATTERN_4(Lists.newArrayList(RANGE_CENTER, MAGE_EAST, RANGE_SOUTH, MAGE_WEST, MELEE_CENTER, RANGE_EAST, RANGE_SOUTH, MAGE_WEST, RANGE_CENTER, MAGE_CENTER, JAD_MAGE_CENTER, MAGE_CENTER));

    @Getter
    private final List<ZulrahPhase> pattern;
}
