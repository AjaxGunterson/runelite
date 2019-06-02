package net.runelite.client.plugins.zulrah;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Zulrah")
public interface ZulrahConfig extends Config {

    @ConfigItem(
            keyName = "tileYes",
            name = "Test Zulrah",
            description = "",
            position = 1
    )
    default boolean tileYes() {return true;}
}
