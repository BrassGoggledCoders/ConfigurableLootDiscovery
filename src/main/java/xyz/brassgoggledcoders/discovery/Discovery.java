package xyz.brassgoggledcoders.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamacronymcoders.base.BaseModFoundation;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;

// TODO Require enchantment. Block meta. Quantity ranges! item removal
@Mod(modid = Discovery.MODID, name = Discovery.MODNAME, version = Discovery.MODVERSION, dependencies = Discovery.DEPENDS)
public class Discovery extends BaseModFoundation<Discovery> {

	public static class LootEntry {
		public final String toolRep;
		public final String lootRep;
		public final int chance;

		public LootEntry(String toolRep, String lootRep, int chance) {
			this.toolRep = toolRep;
			this.lootRep = lootRep;
			this.chance = chance;
		}

		@Override
		public String toString() {
			return "TOOL: " + toolRep + " LOOT: " + lootRep + " CHANCE: " + chance;
		}
	}

	public static final String MODID = "discovery";
	public static final String MODNAME = "Configurable Loot Discovery";
	public static final String MODVERSION = "@VERSION@";
	public static final String DEPENDS = "required-after:base@[0.0.0,];";

	@Instance(MODID)
	public static Discovery instance;
	public static final String KEY_ANYTHING = "anything";
	public static final String KEY_NOTHING = "nothing";
	public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

	public Discovery() {
		super(MODID, MODNAME, MODVERSION, null);
	}

	@Override
	public Discovery getInstance() {
		return instance;
	}

	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	public static ItemStack getStackFromStringRepresentation(String rep) {
		String[] args = rep.split(":");
		Item item = Item.getByNameOrId(args[0]);

		if(args.length == 1) {
			return new ItemStack(item);
		}
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
