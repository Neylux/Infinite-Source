package de.neylux.infinitesource.register;

import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.types.ModBlockEntities;
import de.neylux.infinitesource.register.types.ModBlocks;
import de.neylux.infinitesource.register.types.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;


public class Registration {
    public static final DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = create(ForgeRegistries.BLOCK_ENTITIES);

    public static final CreativeModeTab INFINITE_SOURCE_TAB = new CreativeModeTab(InfiniteSource.MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get());
        }
    };

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        modEventBus.addListener(Registration::clientSetup);
    }

    private static void clientSetup(FMLClientSetupEvent event) {
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, InfiniteSource.MOD_ID);
    }

}
