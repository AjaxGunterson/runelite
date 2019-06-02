package net.runelite.client.plugins.zulrah;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.NpcID.*;

@PluginDescriptor(
        name = "Zulrah",
        description = "This is a test.",
        tags = {"zulrah", "boss"},
        enabledByDefault = false
)

public class ZulrahPlugin extends Plugin {

    @Inject
    private ZulrahConfig config;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private ZulrahOverlay zulrahHighlightOverlay;

    @Inject
    private ZulrahTileOverlay zulrahTileOverlay;

    NPC zulrahNpc = null;
    List<WorldPoint> safeSpots = new ArrayList<>(),
                nextSafeSpots = new ArrayList<>();

    @Provides
    ZulrahConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ZulrahConfig.class);
    }

    @Override
    protected void startUp(){
        overlayManager.add(zulrahHighlightOverlay);
        overlayManager.add(zulrahTileOverlay);

    }

    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case HOPPING:
            case LOGGING_IN:
            case LOGGED_IN:
                break;
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event){
        if (!isZulrah(event.getNpc())){
            return;
        }
        System.out.println("Zulrah has despawned!");
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event){
        if (!config.tileYes()){return;}

        if (!isZulrah(event.getNpc())){
            return;
        }

        System.out.println("Zulrah has spawned!");

        zulrahNpc = event.getNpc();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event){
        if (event.getKey().equals("tileYes")){
            if (event.getNewValue().equals("true")){
                overlayManager.add(zulrahHighlightOverlay);
                overlayManager.add(zulrahTileOverlay);
            } else {
                overlayManager.remove(zulrahHighlightOverlay);
                overlayManager.remove(zulrahTileOverlay);
            }
        }
    }

    private boolean isZulrah(NPC test){
        switch(test.getId()){
            case ZULRAH://green
            case ZULRAH_2043://red
            case ZULRAH_2044://blue
                return true;
        }

        return false;
    }

    public List<WorldPoint> getSafeSpots(){
        return safeSpots;
    }

    public List<WorldPoint> getNextSafeSpots(){
        return nextSafeSpots;
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(zulrahHighlightOverlay);
        overlayManager.remove(zulrahTileOverlay);
    }

}
