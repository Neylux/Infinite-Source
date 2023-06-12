package de.neylux.infinitesource;

import de.neylux.infinitesource.register.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(InfiniteSource.MOD_ID)
public class InfiniteSource {
    public static final String MOD_ID = "infinitesource";

    public InfiniteSource() {
        Registration.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

}
