package net.runelite.client.plugins.zulrah;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NpcID;
import net.runelite.api.Skill;

@Getter
@RequiredArgsConstructor
public enum ZulrahPhase {


    RANGE(NpcID.ZULRAH, Skill.RANGED),
    MELEE(NpcID.ZULRAH_2043, Skill.ATTACK),
    MAGE(NpcID.ZULRAH_2044, Skill.MAGIC);

    private final int npcId;
    private final Skill type;

}
