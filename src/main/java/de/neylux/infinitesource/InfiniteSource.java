package de.neylux.infinitesource;

import com.mojang.logging.LogUtils;
import de.neylux.infinitesource.register.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(InfiniteSource.MOD_ID)
public class InfiniteSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "infinitesource";

    public InfiniteSource() {
        Registration.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

}
