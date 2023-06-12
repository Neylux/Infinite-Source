package de.neylux.infinitesource.register;

import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.types.ModBlockEntities;
import de.neylux.infinitesource.register.types.ModBlocks;
import de.neylux.infinitesource.register.types.ModItems;
import de.neylux.infinitesource.register.types.ModTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InfiniteSource.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, InfiniteSource.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, InfiniteSource.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, InfiniteSource.MOD_ID);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModTabs.register();

        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::clientSetup);
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModTabs.INFINITE_SOURCE_TAB.get()) {
            ITEMS.getEntries().forEach(event::accept);
            BLOCKS.getEntries().forEach(event::accept);
        }
    }

    private static void clientSetup(FMLClientSetupEvent event) {
    }
}
