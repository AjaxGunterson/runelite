package net.runelite.client.plugins.zulrah;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import java.awt.*;
import java.util.List;

import static net.runelite.api.NpcID.*;

public class ZulrahTileOverlay extends Overlay {
    private final Client client;
    private final ZulrahPlugin plugin;

    @Inject
    private ZulrahTileOverlay(Client client, ZulrahPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {

        NPC zulrah = plugin.zulrahNpc;
        List<LocalPoint> safeSpots = plugin.getSafeSpots(),
                    nextSafeSpots = plugin.getNextSafeSpots();

        //if (zulrah == null || safeSpots == null){return null;}
        if (safeSpots == null){return null;}


        for (LocalPoint nextSafe : nextSafeSpots){

            //LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();

            renderTile(graphics, nextSafe, Color.yellow);

        }

        for (LocalPoint safe : safeSpots)
        {
            //LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();

            renderTile(graphics, safe, Color.green);
        }


        return null;
    }

    private void renderTile(final Graphics2D graphics, final LocalPoint safeSpot, final Color color)
    {
        if (safeSpot == null)
        {
            return;
        }

        final Polygon poly = Perspective.getCanvasTilePoly(client, safeSpot);

        if (poly == null)
        {
            return;
        }

        OverlayUtil.renderPolygon(graphics, poly, color);
    }

}

