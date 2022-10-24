package de.neylux.infinitesource.register.types;

import de.neylux.infinitesource.register.Registration;
import de.neylux.infinitesource.register.blocks.InfiniteWaterSourceBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final RegistryObject<Block> INFINITE_WATER_SOURCE_BLOCK = register("infinite_water_source_block", InfiniteWaterSourceBlock::new);

    public static void register() {
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(Registration.INFINITE_SOURCE_TAB)));
        return ret;
    }
}
