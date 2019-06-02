package net.runelite.client.plugins.zulrah;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import java.awt.*;

import static net.runelite.api.NpcID.*;

public class ZulrahOverlay extends Overlay {
    private final Client client;
    private final ZulrahConfig config;
    private final ZulrahPlugin plugin;

    @Inject
    private ZulrahOverlay(Client client, ZulrahConfig config, ZulrahPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.tileYes())
        {
            return null;
        }

        NPC zulrah = plugin.zulrahNpc;

        if (zulrah == null){return null;}

        switch (zulrah.getId()){
            case ZULRAH:
                renderTargetOverlay(graphics, zulrah, Color.green);//highlights hull
                renderTile(graphics, zulrah.getLocalLocation(), Color.green);//highlights tiles
                break;
            case ZULRAH_2043:
                renderTargetOverlay(graphics, zulrah, Color.red);//highlights hull
                renderTile(graphics, zulrah.getLocalLocation(), Color.red);//highlights tiles
                break;
            case ZULRAH_2044:
                renderTargetOverlay(graphics, zulrah, Color.blue);//highlights hull
                renderTile(graphics, zulrah.getLocalLocation(), Color.blue);//highlights tiles
                break;
        }



        return null;
    }

    private void renderTile(final Graphics2D graphics, final LocalPoint dest, final Color color)
    {
        if (dest == null)
        {
            return;
        }

        final Polygon poly = Perspective.getCanvasTilePoly(client, dest);

        if (poly == null)
        {
            return;
        }

        OverlayUtil.renderPolygon(graphics, poly, color);
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
