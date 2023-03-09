package de.neylux.infinitesource.register;

import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.types.ModBlockEntities;
import de.neylux.infinitesource.register.types.ModBlocks;
import de.neylux.infinitesource.register.types.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;


public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InfiniteSource.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, InfiniteSource.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, InfiniteSource.MOD_ID);

    public static CreativeModeTab INFINITE_SOURCE_TAB;


    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        modEventBus.addListener(Registration::clientSetup);
        modEventBus.addListener(Registration::registerCreativeTab);
    }

    private static void registerCreativeTab(CreativeModeTabEvent.Register register) {
        INFINITE_SOURCE_TAB = register.registerCreativeModeTab(new ResourceLocation(InfiniteSource.MOD_ID, "infinitesource_tab"), builder ->
                builder
                        .title(Component.translatable("item_group." + InfiniteSource.MOD_ID + ".infinitesource_tab"))
                        .icon(() -> ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get().asItem().getDefaultInstance())
                        .displayItems((featureFlags, output, hasPermission) -> {
                            Registration.ITEMS.getEntries().forEach(itemRegistryObject -> output.accept(itemRegistryObject.get()));
                            Registration.BLOCKS.getEntries().forEach(blockRegistryObject -> output.accept(blockRegistryObject.get()));
                        })
                        .build());
    }

    private static void clientSetup(FMLClientSetupEvent event) {
    }

}
