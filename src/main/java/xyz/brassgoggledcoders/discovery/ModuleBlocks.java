package xyz.brassgoggledcoders.discovery;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Module(Discovery.MODID)
public class ModuleBlocks extends ModuleBase {

	public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
	public static Map<String, LootEntry[]> lootEntries = new HashMap<String, LootEntry[]>();
	public static final String KEY_ANYTHING = "anything";
	public static final String KEY_NOTHING = "nothing";

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		File jsonConfig =
				new File(this.getConfigRegistry().getConfigFolder().getPath() + File.separator + "blocks.json");
		LootEntry test = new LootEntry("grass", "diamond:1:0", 100);
		lootEntries.put("diamond_pick", new LootEntry[] {test});
		try {
			if(!jsonConfig.exists() && jsonConfig.createNewFile()) {
				lootEntries.put(KEY_ANYTHING, new LootEntry[0]);
				lootEntries.put(KEY_NOTHING, new LootEntry[0]);
				String json = gson.toJson(lootEntries, new TypeToken<Map<String, LootEntry[]>>() {}.getType());
				FileWriter writer = new FileWriter(jsonConfig);
				writer.write(json);
				writer.close();
			}
			lootEntries =
					gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<String, LootEntry[]>>() {}.getType());
		}
		catch(IOException e) {
			this.getLogger().error("Error creating default configuration.");
		}
		for(LootEntry[] loot : lootEntries.values()) {
			this.getLogger().devInfo(Arrays.toString(loot));
		}

	}

	public static class LootEntry {
		public final String blockName;
		public final String lootRep;
		public final int chance;

		public LootEntry(String blockName, String lootRep, int chance) {
			this.blockName = blockName;
			// TODO BLOCK META
			this.lootRep = lootRep;
			this.chance = chance;
		}

		@Override
		public String toString() {
			return "BLOCK: " + blockName + " LOOT: " + lootRep + " CHANCE: " + chance;
		}
	}

	public static ItemStack getStackFromStringRepresentation(String rep) {
		String[] args = rep.split(":");
		Item item = Item.getByNameOrId(args[0]);

		if(args.length == 1)
			return new ItemStack(item);
		else if(args.length == 2) {
			int quantity = Integer.valueOf(args[1]);
			return new ItemStack(item, quantity);
		}
		else {
			int quantity = Integer.valueOf(args[1]);
			int meta = Integer.valueOf(args[2]);
			return new ItemStack(item, quantity, meta);
		}
	}

}
