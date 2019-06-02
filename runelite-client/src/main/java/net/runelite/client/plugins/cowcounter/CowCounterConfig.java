package net.runelite.client.plugins.cowcounter;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("cowcounter")
public interface CowCounterConfig extends Config {

    @ConfigItem(
            keyName = "cowTracker",
            name = "Number of cows on screen",
            description = "Keeps track of how many cows are rendered.",
            position = 1
    )
    default boolean cowTracker() { return false; }

    @ConfigItem(
            keyName = "mooTracker",
            name = "Number of \"moo\"s",
            description = "The amount of times a cow \"moos\" will be kept track of.",
            position = 2
    )
    default boolean mooTracker() { return true; }

    @ConfigItem(
            keyName = "cowsHighlighted",
            name = "Cows are highlighted",
            description = "Cows will be highlighted.",
            position = 3
    )
    default boolean cowsHighlighted() {return true;}

    @ConfigItem(
            keyName = "cowHighlightColor",
            name = "Cow highlight color",
            description = "The color that cows will be highlighted.",
            position = 4
    )
    default Color cowHighlightColor() {return Color.yellow;}
}
