package net.runelite.client.plugins.cowcounter;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.api.NpcID.*;

@PluginDescriptor(
        name = "Cow Counter",
        description = "There is no cow level.",
        tags = {"cow", "tracker"},
        enabledByDefault = false
)

public class CowCounterPlugin extends Plugin {

    @Inject
    private CowCounterConfig config;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private InfoBoxManager infoBoxManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private CowCounterOverlay overlay;

    int numOfMoos = 0;
    int numOfCows = 0;
    CowCounterCounter cowCounter,
                        mooCounter;
    List<NPC> cowList = new ArrayList<>();

    @Provides
    CowCounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CowCounterConfig.class);
    }

    @Override
    protected void startUp(){
            if (config.mooTracker()){
                addMooCounter();
            }
            if(config.cowTracker()){
                addCowCounter();
            }
            if (config.cowsHighlighted()){
                overlayManager.add(overlay);
            }
    }

    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case HOPPING:
                cowList.clear();
            case LOGGING_IN:
                cowList.clear();
            case LOGGED_IN:
                break;
        }
    }

    private void addCowCounter(){
        int itemSpriteId = ItemID.COW_MASK;
        BufferedImage cowPic = itemManager.getImage(itemSpriteId);

        cowCounter = new CowCounterCounter(cowPic, this, numOfCows);
        cowCounter.setTooltip(String.format("Cows On Screen"));

        infoBoxManager.addInfoBox(cowCounter);
    }

    private void addMooCounter(){
        BufferedImage cowPic = itemManager.getImage(ItemID.COW_TOP);
        mooCounter = new CowCounterCounter(cowPic, this, numOfMoos);
        mooCounter.setTooltip(String.format("Times Cows Have Moo'd"));
        infoBoxManager.addInfoBox(mooCounter);
    }

    private void removeCowCounter(){
        infoBoxManager.removeInfoBox(cowCounter);
        numOfCows = 0;
    }

    private void removeMooCounter(){
        infoBoxManager.removeInfoBox(mooCounter);
        numOfMoos = 0;
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event){
        if (config.cowTracker()){
            numOfCows = cowTracker();
            cowCounter.setCount(numOfCows);
            //Put code to unhighlight npcs here
            NPC npc = event.getNpc();
            cowList.remove(npc);
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event){
        if (config.cowTracker()){
            numOfCows = cowTracker();
            cowCounter.setCount(numOfCows);
        }
        if (config.cowsHighlighted()){
            NPC npc = event.getNpc();
            if (isCow(npc)){
                cowList.add(npc);
            }

        }
    }

    @Subscribe
    public void onOverheadTextChanged(OverheadTextChanged event) {

        if (config.mooTracker()) {//if checked
            List<NPC> listy;
            NPC dummy;
            listy = client.getNpcs();
            for (int i = 0; i < listy.size(); i++) {
                dummy = listy.get(i);
                if (isCow(dummy)) {
                    if (dummy.getOverheadText() != null) {
                        numOfMoos++;
                    }
                }
            }
            //removeMooCounter();
            //addMooCounter();
            mooCounter.setCount(numOfMoos);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event){
        if (event.getKey().equals("cowTracker")){
            if (event.getNewValue().equals("true")){
                addCowCounter();
            } else {
                removeCowCounter();
            }

        } else if (event.getKey().equals("mooTracker")){
            if (event.getNewValue().equals("true")){
                addMooCounter();
            } else {
                removeMooCounter();
            }

        } else if (event.getKey().equals("cowsHighlighted")){
            if (event.getNewValue().equals("true")){
                //enable cow highlighting
                overlayManager.add(overlay);
            } else {
                //disable cow highlighting
                overlayManager.remove(overlay);
                //cowList.clear();
            }

        }
    }

    public List<NPC> getCowList(){
        return cowList;
    }

    private int cowTracker(){
        List<NPC> listy = client.getNpcs();
        NPC dummy;
        int placeholder = 0;
        for (int i = 0; i < listy.size(); i++){
            dummy = listy.get(i);
            if (isCow(dummy)){
                placeholder++;
            }
        }
        return placeholder;
    }

    private boolean isCow(NPC test){
        int npcID = test.getId();

        switch(npcID){
            case COW:
            case COW_2806:
            case COW_2808:
            case COW_2810:
            case COW_5842:
            case COW_6401:
            case 2691: //dairy cow
            case COW_CALF:
            case COW_CALF_2809:
            case COW_CALF_2816:
                return true;
            default:
                return false;

        }
    }

    @Override
    protected void shutDown() throws Exception
    {
        if (config.mooTracker()){
            removeMooCounter();
        }
        if(config.cowTracker()){
            removeCowCounter();
        }
        //unhighlight all NPCs
        overlayManager.remove(overlay);
        cowList.clear();
    }

}
