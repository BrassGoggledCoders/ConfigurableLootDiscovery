package xyz.brassgoggledcoders.discovery;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.discovery.ModuleBlocks.LootEntry;

// TODO CHANCES
public class DiscoveryEventHandler {

	@SubscribeEvent
	public void onBlockHarvest(HarvestDropsEvent event) {
		if(event.getWorld().isRemote || event.getHarvester() == null)
			return;
		// TODO Optimise. Invert block and tool params
		tryAddLoot(event, ModuleBlocks.KEY_ANYTHING);
		if(event.getHarvester().getHeldItemMainhand() == ItemStack.EMPTY) {
			tryAddLoot(event, ModuleBlocks.KEY_NOTHING);
		}
		else {
			ItemStack held = event.getHarvester().getHeldItemMainhand();
			String itemName = held.getItem().getRegistryName().getResourcePath();
			Discovery.instance.getLogger().devInfo(itemName);
			for(String key : ModuleBlocks.lootEntries.keySet()) {
				Discovery.instance.getLogger().devInfo("KEY: " + key);
				String[] item = key.split(":");
				Discovery.instance.getLogger().devInfo(Arrays.toString(item));
				if(item[0].equals(itemName)) {
					if(item.length == 1 || item[1].equals("*")) {
						tryAddLoot(event, key);
					}
					else if(Integer.valueOf(item[1]) == held.getMetadata()) {
						tryAddLoot(event, key);
					}
				}
			}
		}
	}

	public void tryAddLoot(HarvestDropsEvent event, String heldString) {
		for(LootEntry entry : ModuleBlocks.lootEntries.get(heldString)) {
			Discovery.instance.getLogger().devInfo(entry.toString());
			Block block = Block.getBlockFromName(entry.blockName);
			if(block != null) {
				// TODO BLOCK META
				if(event.getState().getBlock().getRegistryName().getResourcePath()
						.equals(block.getRegistryName().getResourcePath())) {

					event.getDrops().add(ModuleBlocks.getStackFromStringRepresentation(entry.lootRep));

				}
			}
		}
	}
}
