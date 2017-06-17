package nofist.event;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

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
    private ArrayList<String> unpunchableOres;
    private ArrayList<String> unpunchableBlocks;

    // Event handlers.
    private final BlockBreakEventNF BLOCK_BREAK_EVENT_HANDLER = new BlockBreakEventNF();

    private NFConfig nfConfig;

    public EventManagerNF() {

        nfConfig = NoFistAPI.config();

        this.punchBlocks = new ArrayList<Block>();

        this.unpunchableOres = new ArrayList<String>(Arrays.asList(nfConfig.UNPUNCHABLE_ORES.get()));

        this.unpunchableBlocks = new ArrayList<String>(Arrays.asList(nfConfig.UNPUNCHABLE_BLOCKS.get()));

        Block block;
        ArrayList<ItemStack> ores;

        for (int i = 0; i < this.unpunchableOres.size(); i++) {

            //FMLLog.info(this.unpunchableOres.get(i));
            ores = new ArrayList<ItemStack>(OreDictionary.getOres(this.unpunchableOres.get(i)));

            for (int j = 0; j < ores.size(); j++) {

                block = Block.getBlockFromItem(ores.get(j).getItem());
                //FMLLog.info(block.getLocalizedName());

                if (!this.punchBlocks.contains(block)) {
                    this.punchBlocks.add(block);
                }
            }
        }

        for (int i = 0; i < this.unpunchableBlocks.size(); i++) {

            block = GameData.getBlockRegistry().getObject(new ResourceLocation(this.unpunchableBlocks.get(i)));
            //FMLLog.info(block.getLocalizedName());

            if (!this.punchBlocks.contains(block)) {
                this.punchBlocks.add(block);
            }
        }
    }

    public class BlockBreakEventNF {

        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event)
        {
            IBlockState state = event.getState();
            Block block = state.getBlock();

            if (isValidBlock(block) && (event.getPlayer() != null) && event.getPlayer() instanceof EntityPlayer && !(event.getPlayer() instanceof FakePlayer)) {

                EntityPlayer player = (EntityPlayer)event.getPlayer();
                Iterable<ItemStack> tools = player.getHeldEquipment();

                for (ItemStack tool : tools) {

                    int harvestLevel = (tool == null) ? -1 : tool.getItem().getHarvestLevel(tool, "axe", player, state);

                    //FMLLog.info("harvestLevel = %d", harvestLevel);

                    if (harvestLevel < 0) {

                        if (!player.isCreative()) {

                            event.setCanceled(true);
                            String message = "punch_block";

                        if (nfConfig.PUNCH_DAMAGE.get() > 0) {
                            player.attackEntityFrom(NoFist.punchDamageSource, nfConfig.PUNCH_DAMAGE.get());
                                message = "punch_block_with_damage";
                            }

                            if (nfConfig.SHOW_MESSAGES.get() && player.getHealth() > 0f) {
                                player.sendMessage(new TextComponentTranslation(message));
                            }
                        }
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

        MinecraftForge.EVENT_BUS.register(BLOCK_BREAK_EVENT_HANDLER);

        logEventMessage("NoFist's event handlers have been registered successfully.");
    }

    private static void logEventMessage(String message) {

        FMLLog.info("NoFist Event System: " + message);
    }
}