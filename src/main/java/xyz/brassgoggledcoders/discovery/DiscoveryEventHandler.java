package xyz.brassgoggledcoders.discovery;

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
		String blockName = event.getState().getBlock().getRegistryName().getResourcePath();
		ItemStack held = event.getHarvester().getHeldItemMainhand();
		if(ModuleBlocks.lootEntries.containsKey(blockName)) {
			for(LootEntry entry : ModuleBlocks.lootEntries.get(blockName)) {
				if(entry.toolRep.equals(ModuleBlocks.KEY_ANYTHING)) {
					event.getDrops().add(ModuleBlocks.getStackFromStringRepresentation(entry.lootRep));
				}
				else if(entry.toolRep.equals(ModuleBlocks.KEY_NOTHING) && held == ItemStack.EMPTY) {
					event.getDrops().add(ModuleBlocks.getStackFromStringRepresentation(entry.lootRep));
				}
				else {
					String itemName = held.getItem().getRegistryName().getResourcePath();
					String[] item = entry.toolRep.split(":");
					// Discovery.instance.getLogger().devInfo(Arrays.toString(item));
					if(item[0].equals(itemName)) {
						if(item.length == 1 || item[1].equals("*")) {
							event.getDrops().add(ModuleBlocks.getStackFromStringRepresentation(entry.lootRep));
						}
						else if(Integer.valueOf(item[1]) == held.getMetadata()) {
							event.getDrops().add(ModuleBlocks.getStackFromStringRepresentation(entry.lootRep));
						}
					}
				}

			}
		}
	}
}
