package net.runelite.client.plugins.cowcounter;

import net.runelite.client.game.AsyncBufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Counter;
import java.awt.image.BufferedImage;

class CowCounterCounter extends Counter
{
    CowCounterCounter(BufferedImage img, Plugin plugin, int amount)
    {
        super(img, plugin, amount);
    }
}