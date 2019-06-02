package net.runelite.client.plugins.cowcounter;

import java.awt.*;
import java.util.List;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;


public class CowCounterOverlay extends Overlay {
    private final Client client;
    private final CowCounterConfig config;
    private final CowCounterPlugin plugin;

    @Inject
    private CowCounterOverlay(Client client, CowCounterConfig config, CowCounterPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.cowsHighlighted())
        {
            return null;
        }

        List<NPC> targets = plugin.getCowList();
        for (NPC target : targets)
        {
            renderTargetOverlay(graphics, target, config.cowHighlightColor());
        }

        return null;
    }

    private void renderTargetOverlay(Graphics2D graphics, NPC actor, Color color)
    {
        Polygon objectClickbox = actor.getConvexHull();
        if (objectClickbox != null)
        {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(2));
            graphics.draw(objectClickbox);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            graphics.fill(objectClickbox);
        }
    }

}