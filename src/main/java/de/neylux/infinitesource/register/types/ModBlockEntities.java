package de.neylux.infinitesource.register.types;

import de.neylux.infinitesource.register.Registration;
import de.neylux.infinitesource.register.blocks.InfiniteWaterSourceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final RegistryObject<BlockEntityType<InfiniteWaterSourceBlockEntity>> FARMING_STATION_BLOCK_ENTITY =
            Registration.BLOCK_ENTITIES.register("infinite_water_source_entity",
                    () -> BlockEntityType.Builder.of(InfiniteWaterSourceBlockEntity::new, ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get()).build(null));

    public static void register() {
    }
}
