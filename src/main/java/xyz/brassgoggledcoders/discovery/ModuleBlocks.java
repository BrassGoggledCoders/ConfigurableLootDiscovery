package xyz.brassgoggledcoders.discovery;

import java.io.*;
import java.util.*;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.registrysystem.config.ConfigEntry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.brassgoggledcoders.discovery.Discovery.LootEntry;

@Module(Discovery.MODID)
@EventBusSubscriber(modid = Discovery.MODID)
public class ModuleBlocks extends ModuleBase {

	public static Map<String, Discovery.LootEntry[]> lootEntries = new HashMap<String, Discovery.LootEntry[]>();
	public static Map<String, String[]> removalEntries = Maps.newHashMap();

	@Override
	public String getName() {
		return "Blocks";
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		File jsonConfig = new File(
				this.getConfigRegistry().getConfigFolder().getPath() + File.separator + "blocks.json");
		File jsonRemovalConfig = new File(
				this.getConfigRegistry().getConfigFolder().getPath() + File.separator + "blocks_remove.json");

		this.getConfigRegistry().addEntry(new ConfigEntry("general", "enableExamples", Type.BOOLEAN, "false",
				"Delete or rename blocks.json after enabling"));
		if(getConfigRegistry().getBoolean("enableExamples", false)) {
			lootEntries.put("dirt",
					new Discovery.LootEntry[] { new Discovery.LootEntry(Discovery.KEY_NOTHING, "stick", 100),
							new Discovery.LootEntry("diamond_pickaxe", "diamond", 100) });
		}
		try {
			if(!jsonConfig.exists() && jsonConfig.createNewFile()) {
				String json = Discovery.gson.toJson(lootEntries, new TypeToken<Map<String, Discovery.LootEntry[]>>() {
				}.getType());
				FileWriter writer = new FileWriter(jsonConfig);
				writer.write(json);
				writer.close();
			}
			lootEntries = Discovery.gson.fromJson(new FileReader(jsonConfig),
					new TypeToken<Map<String, LootEntry[]>>() {
					}.getType());
			if(!jsonRemovalConfig.exists() && jsonRemovalConfig.createNewFile()) {
				String json = Discovery.gson.toJson(removalEntries, new TypeToken<Map<String, String[]>>() {
				}.getType());
				FileWriter writer = new FileWriter(jsonRemovalConfig);
				writer.write(json);
				writer.close();
			}
		}
		catch(IOException e) {
			getLogger().error("Error creating default configuration.");
		}
		for(Discovery.LootEntry[] loot : lootEntries.values()) {
			getLogger().devInfo(Arrays.toString(loot));
		}
	}

	@SubscribeEvent
	public static void onBlockHarvest(HarvestDropsEvent event) {
		if(event.getWorld().isRemote || event.getHarvester() == null) {
			return;
		}
		String blockName = event.getState().getBlock().getRegistryName().getResourcePath();
		ItemStack held = event.getHarvester().getHeldItemMainhand();
		if(ModuleBlocks.lootEntries.containsKey(blockName)) {
			for(Discovery.LootEntry entry : ModuleBlocks.lootEntries.get(blockName)) {
				if(entry.toolRep.equals(Discovery.KEY_ANYTHING)) {
					if(event.getWorld().rand.nextInt(entry.chance) == 0) {
						event.getDrops().add(Discovery.getStackFromStringRepresentation(entry.lootRep));
					}
				}
				else if(entry.toolRep.equals(Discovery.KEY_NOTHING) && held == ItemStack.EMPTY) {
					if(event.getWorld().rand.nextInt(entry.chance) == 0) {
						event.getDrops().add(Discovery.getStackFromStringRepresentation(entry.lootRep));
					}
				}
				else {
					String itemName = held.getItem().getRegistryName().getResourcePath();
					String[] item = entry.toolRep.split(":");
					// Discovery.instance.getLogger().devInfo(Arrays.toString(item));
					if(item[0].equals(itemName)) {
						if(item.length == 1 || item[1].equals("*")) {
							if(event.getWorld().rand.nextInt(entry.chance) == 0) {
								event.getDrops().add(Discovery.getStackFromStringRepresentation(entry.lootRep));
							}
						}
						else if(Integer.valueOf(item[1]) == held.getMetadata()) {
							if(event.getWorld().rand.nextInt(entry.chance) == 0) {
								event.getDrops().add(Discovery.getStackFromStringRepresentation(entry.lootRep));
							}
						}
					}
				}

			}
		}
	}

}
