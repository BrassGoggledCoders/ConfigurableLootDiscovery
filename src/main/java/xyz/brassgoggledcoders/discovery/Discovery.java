package xyz.brassgoggledcoders.discovery;

import com.teamacronymcoders.base.BaseModFoundation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// TODO Require enchantment
@Mod(modid = Discovery.MODID, name = Discovery.MODNAME, version = Discovery.MODVERSION,
		dependencies = Discovery.DEPENDS)
public class Discovery extends BaseModFoundation<Discovery> {

	public static final String MODID = "discovery";
	public static final String MODNAME = "Discovery";
	public static final String MODVERSION = "@VERSION@";
	public static final String DEPENDS = "required-after:base@[0.0.0,];";

	@Instance(MODID)
	public static Discovery instance;

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
		MinecraftForge.EVENT_BUS.register(new DiscoveryEventHandler());
	}

}
