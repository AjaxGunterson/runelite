package net.runelite.client.plugins.zulrah;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.NpcID.*;
import static net.runelite.client.plugins.zulrah.ZulrahPhase.*;
import static net.runelite.client.plugins.zulrah.ZulrahPosition.*;

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

    private ZulrahPhase phase;

    private ZulrahPosition position;

    final int ZULRAH_EMERGE_INITIAL = 5071,
                ZULRAH_EMERGE = 5073;


    int phaseCounter = 0;
    NPC zulrahNpc = null;
    List<WorldPoint> safeSpots = new ArrayList<>(),
                nextSafeSpots = new ArrayList<>();
    List<ZulrahPhase> phaseList = new ArrayList<>();

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
                zulrahNpc = null;
                break;
            case LOGGING_IN:
                zulrahNpc = null;
                break;
            case LOGGED_IN:
                break;
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event){
        if (!isZulrah(event.getNpc())){
            return;
        }
        System.out.println("Number of phases: " + phaseCounter);
        phaseCounter = 0;
        System.out.println("Zulrah has despawned!");
        for(ZulrahPhase phase : phaseList){
            System.out.print(phase.getNpcId());
            System.out.println(": " + phase.getPosition());

        }
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
    public void onAnimationChanged(AnimationChanged event){
        if (!config.tileYes()){return;}

        Actor npc = event.getActor();

        if (npc.getName() == null){return;}

        if (!(npc.getName().equals("Zulrah"))){return;}

        if (npc.getAnimation() == ZULRAH_EMERGE || npc.getAnimation() == ZULRAH_EMERGE_INITIAL){
            transformationTracker();
            System.out.println("Zulrah has transformed!");
        }

    }

    private void transformationTracker(){
        phaseCounter++;
        ZulrahPhase currentPhase = phase.MAGE_SOUTH;
        LocalPoint zulrahLocation;

        for (NPC newZulrah : client.getNpcs()){

            switch(newZulrah.getId()){
                case ZULRAH://RANGE

                    zulrahNpc = newZulrah;
                    zulrahLocation = newZulrah.getLocalLocation();

                    if (zulrahLocation.equals(CENTER.getPosition())){
                        currentPhase = phase.RANGE_CENTER;
                    } else if (zulrahLocation.equals(EAST.getPosition())){
                        currentPhase = phase.RANGE_EAST;
                    } else if (zulrahLocation.equals(SOUTH.getPosition())){
                        currentPhase = phase.RANGE_SOUTH;
                    } else if (zulrahLocation.equals(WEST.getPosition())){
                        currentPhase = phase.RANGE_WEST;
                    }
                    break;
                case ZULRAH_2043://MELEE
                    zulrahNpc = newZulrah;
                    zulrahLocation = newZulrah.getLocalLocation();
                    if (zulrahLocation.equals(CENTER.getPosition())){
                        currentPhase = phase.MELEE_CENTER;
                    } else if (zulrahLocation.equals(EAST.getPosition())){
                        currentPhase = phase.MELEE_EAST;
                    } else if (zulrahLocation.equals(SOUTH.getPosition())){
                        currentPhase = phase.MELEE_SOUTH;
                    } else if (zulrahLocation.equals(WEST.getPosition())){
                        currentPhase = phase.MELEE_WEST;
                    }
                    break;
                case ZULRAH_2044://MAGE
                    zulrahNpc = newZulrah;
                    zulrahLocation = newZulrah.getLocalLocation();
                    if (zulrahLocation.equals(CENTER.getPosition())){
                        currentPhase = phase.MAGE_CENTER;
                    } else if (zulrahLocation.equals(EAST.getPosition())){
                        currentPhase = phase.MAGE_EAST;
                    } else if (zulrahLocation.equals(SOUTH.getPosition())){
                        currentPhase = phase.MAGE_SOUTH;
                    } else if (zulrahLocation.equals(WEST.getPosition())){
                        currentPhase = phase.MAGE_WEST;
                    }
                    break;
            }
        }
        phaseList.add(currentPhase);
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
