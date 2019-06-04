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
import net.runelite.api.GameState;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.NpcID.*;
import static net.runelite.client.plugins.zulrah.ZulrahPatterns.*;
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
    ZulrahPatterns pattern = null; //undecided pattern

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
            case LOADING:
                break;
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event){
        if (!isZulrah(event.getNpc())){
            return;
        }
        System.out.println("Number of phases: " + phaseCounter);
        System.out.println("Zulrah has despawned!");
        System.out.println("Phase: " + phase);
        for(ZulrahPhase phase : phaseList){
            System.out.print(phase.getPhaseName());

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

        if (npc.getAnimation() == ZULRAH_EMERGE_INITIAL){
            phaseCounter = 0;
            phaseList.clear();
            pattern = null;
        }

        if (npc.getAnimation() == ZULRAH_EMERGE || npc.getAnimation() == ZULRAH_EMERGE_INITIAL){
            transformationTracker();
            System.out.println("Zulrah has transformed!");
        }

    }

    private void transformationTracker(){
        phaseCounter++;
        ZulrahPhase currentPhase = phase.MAGE_SOUTH;
        LocalPoint zulrahLocation;
        System.out.println("Phase Count: " + phaseCounter);

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
        switch(phaseCounter){
            case 9:
                if (pattern.equals(PATTERN_1) || pattern.equals(PATTERN_2)){currentPhase = JAD_RANGE_WEST;}
                break;
            case 10:
                if (pattern.equals(PATTERN_3)){currentPhase = JAD_MAGE_EAST;}
                break;
            case 11:
                if (pattern.equals(PATTERN_4)){currentPhase = JAD_MAGE_EAST;}
                break;
            default:
                break;
        }
        phaseList.add(currentPhase);
        patternDetermination(phaseCounter, phaseList);
    }

    private void patternDetermination(int phaseNumber, List<ZulrahPhase>currentPhases){
        //code to determine phase and possible phases
            switch (phaseNumber){
                case 1:
                    //Could be any pattern at this stage
                    //all 3 next moves are possible
                    break;
                case 2:
                    //patterns 3 and 4 are solved here
                    if (currentPhases.get(phaseNumber - 1).equals(RANGE_EAST)){
                        pattern = PATTERN_3;
                        System.out.println("Pattern Decided: 3");
                    } else if (currentPhases.get(phaseNumber - 1).equals(MAGE_EAST)){
                        pattern = PATTERN_4;
                        System.out.println("Pattern Decided: 4");
                    }
                    break;
                case 3:
                    //if phase 2 was red this should be blue
                    break;
                case 4:
                    //if phase is RANGE_SOUTH then it's pattern 1
                    //if phase is RANGE_WEST then it's pattern 2
                    //All cases should be solved by here
                    if (currentPhases.get(phaseNumber - 1).equals(RANGE_SOUTH)){
                        pattern = PATTERN_1;
                        System.out.println("Pattern Decided: 1");
                    } else if (currentPhases.get(phaseNumber - 1).equals(RANGE_WEST)){
                        pattern = PATTERN_2;
                        System.out.println("Pattern Decided: 2");
                    }
                    break;
                default:
                    break;
        }

        if (phaseNumber > 10){
        /*check if current phase is RANGE_CENTER, this will determine
        * if the pattern has been reset to the beginning*/
            if (currentPhases.get(phaseNumber - 1).equals(RANGE_CENTER)){
                phaseCounter = 1;
                pattern = null;
            }
        }

        return;
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
        phaseCounter = 0;
        phaseList.clear();
        safeSpots.clear();
        nextSafeSpots.clear();
        pattern = null;
    }

}
