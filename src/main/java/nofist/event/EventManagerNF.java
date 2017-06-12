package nofist.event;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;

import nofist.NoFist;
import nofist.api.NoFistAPI;
import nofist.api.config.NFConfig;


@SuppressWarnings({"WeakerAccess", "unused"})
public class EventManagerNF {

    private ArrayList<Block> punchBlocks;
    private ArrayList<String> validOres;
    private ArrayList<String> validBlocks;

    // Event handlers.
    private final HarvestDropsEventNF HARVEST_DROPS_EVENT_HANDLER = new HarvestDropsEventNF();

    private NFConfig nfConfig;

    public EventManagerNF() {

        nfConfig = NoFistAPI.config();

        this.punchBlocks = new ArrayList<Block>();

        this.validOres = new ArrayList<String>(Arrays.asList(new String[]{
            "logWood", "plankWood", "slabWood", "stairWood", "chest", "chestWood", "chestTrapped"
        }));

        this.validBlocks = new ArrayList<String>(Arrays.asList(new String[]{
            "minecraft:fence", "minecraft:fence_gate", "minecraft:double_wooden_slab"
        }));

        Block block;
        ArrayList<ItemStack> ores;

        for (int i = 0; i < this.validOres.size(); i++) {

            FMLLog.info(this.validOres.get(i));
            ores = new ArrayList<ItemStack>(OreDictionary.getOres(this.validOres.get(i)));

            for (int j = 0; j < ores.size(); j++) {

                block = Block.getBlockFromItem(ores.get(j).getItem());
                FMLLog.info(block.getLocalizedName());

                if (!this.punchBlocks.contains(block)) {
                    this.punchBlocks.add(block);
                }
            }
        }

        for (int i = 0; i < this.validBlocks.size(); i++) {

            block = GameData.getBlockRegistry().getObject(new ResourceLocation(this.validBlocks.get(i)));
            FMLLog.info(block.getLocalizedName());

            if (!this.punchBlocks.contains(block)) {
                this.punchBlocks.add(block);
            }
        }
    }

    public class HarvestDropsEventNF {

        @SubscribeEvent
        public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event)
        {
            Block block = event.getState().getBlock();

            if (isValidBlock(block) && (event.getHarvester() != null) && event.getHarvester() instanceof EntityPlayer && !(event.getHarvester() instanceof FakePlayer)) {

                EntityPlayer player = (EntityPlayer)event.getHarvester();

                Iterable<ItemStack> tools = player.getHeldEquipment();

                for (ItemStack tool : tools) {

                    int harvestLevel = (tool == null) ? -1 : tool.getItem().getHarvestLevel(tool, "axe");

                    FMLLog.info("harvestLevel = %d", harvestLevel);

                    if (harvestLevel < 0) {

                        event.getDrops().clear();

                        //player.setHealth(player.getHealth() - 1f);

                        player.attackEntityFrom(NoFist.punchDamage, 1f);
                    }

                    // We're not concerned about the player's 'off' hand, so we're breaking here no matter what.
                    break;
                }
            }
        }

        private boolean isValidBlock(Block block)
        {
            for (int i = 0; i < punchBlocks.size(); i++) {
                if (punchBlocks.get(i) == block) {
                    return true;
                }
            }

            return false;
        }
    }

    public void registerEventHandlers() {

        logEventMessage("Registering NoFist's event handlers...");

        MinecraftForge.EVENT_BUS.register(HARVEST_DROPS_EVENT_HANDLER);

        logEventMessage("NoFist's event handlers have been registered successfully.");
    }

    private static void logEventMessage(String message) {

        FMLLog.info("NoFist Event System: " + message);
    }
}