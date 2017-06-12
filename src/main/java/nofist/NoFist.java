package nofist;

import java.io.File;

import net.minecraft.util.DamageSource;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import nofist.api.NoFistAPI;
import nofist.api.config.NFConfig;
import nofist.event.EventManagerNF;
import nofist.proxy.ClientProxy;
import nofist.proxy.CommonProxy;
import nofist.reference.ModInfo;
import static nofist.api.NoFistAPI.configPath;


@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(
    modid                    = ModInfo.MOD_ID,
    name                     = ModInfo.MOD_NAME,
    version                  = ModInfo.MOD_VERSION,
    dependencies             = "required-after:Forge@[" + ModInfo.MCF_MINVER + "," + ModInfo.MCF_MAXVER + ")" + ModInfo.MOD_DEPS,
    acceptableRemoteVersions = "*"
)
public class NoFist {

    public static EventManagerNF eventMgr;
    public static DamageSource punchDamage;

    @Instance(ModInfo.MOD_ID)
    public static NoFist instance;

    @SidedProxy(serverSide = CommonProxy.LOCATION, clientSide = ClientProxy.LOCATION)
    public static CommonProxy proxy;

    @EventHandler
    public void initPre(FMLPreInitializationEvent event) {

        instance = this;

        NoFistAPI.configPath = event.getModConfigurationDirectory() + File.separator + ModInfo.CONFIG_DIRECTORY + File.separator;
        NoFistAPI.nfConfig = new NFConfig();
        NoFistAPI.nfConfig.load(configPath + "NoFist.cfg");


    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        eventMgr = new EventManagerNF();
        eventMgr.registerEventHandlers();

        punchDamage = new DamageSource("punch").setDamageBypassesArmor();
    }

    @EventHandler
    public void initPost(FMLPostInitializationEvent event) {

    }
}
