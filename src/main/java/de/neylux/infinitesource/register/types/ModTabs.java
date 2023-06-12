package de.neylux.infinitesource.register.types;

import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {
    public static void register() {
    }

    public static final RegistryObject<CreativeModeTab> INFINITE_SOURCE_TAB = Registration.CREATIVE_TABS.register("infinitesource_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + InfiniteSource.MOD_ID + ".infinitesource_tab"))
            .icon(() -> ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get().asItem().getDefaultInstance())
            .build()
    );
}
