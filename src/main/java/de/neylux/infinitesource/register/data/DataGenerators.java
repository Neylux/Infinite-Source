package de.neylux.infinitesource.register.data;


import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.Registration;
import de.neylux.infinitesource.register.types.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = InfiniteSource.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(event.includeServer(), new GeneratorRecipes(generator));
            generator.addProvider(event.includeServer(), new GeneratorLoots(generator));
        }

        if (event.includeClient()) {
            generator.addProvider(event.includeServer(), new GeneratorBlockTags(generator, event.getExistingFileHelper()));
            generator.addProvider(event.includeServer(), new GeneratorLanguage(generator));
            generator.addProvider(event.includeServer(), new GeneratorBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(event.includeServer(), new GeneratorItemModels(generator, event.getExistingFileHelper()));
        }
    }

    static class GeneratorBlockStates extends BlockStateProvider {
        public GeneratorBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, InfiniteSource.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            horizontalBlock(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get(), models()
                    .cubeAll(Registry.BLOCK.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get()).getPath(),
                            modLoc("blocks/infinite_water_source_block")));
        }
    }

    static class GeneratorItemModels extends ItemModelProvider {
        public GeneratorItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
            super(generator, InfiniteSource.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            String path = Registry.BLOCK.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get()).getPath();
            getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
        }

        @Override
        public String getName() {
            return "Item Models";
        }
    }

    static class GeneratorLanguage extends LanguageProvider {
        public GeneratorLanguage(DataGenerator gen) {
            super(gen, InfiniteSource.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            addBlock(ModBlocks.INFINITE_WATER_SOURCE_BLOCK, "Infinite Water Source");
            add("itemGroup.infinitesource", "Infinite Source");
        }
    }

    static class GeneratorLoots extends LootTableProvider {
        public GeneratorLoots(DataGenerator dataGeneratorIn) {
            super(dataGeneratorIn);
        }

        @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK));
        }

        private static class Blocks extends BlockLoot {
            @Override
            protected void addTables() {
                LootPool.Builder builder = LootPool.lootPool()
                        .name(Registry.BLOCK.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get()).toString())
                        .setRolls(ConstantValue.exactly(1))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get())
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
                this.add(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get(), LootTable.lootTable().withPool(builder));
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return ImmutableList.of(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get());
            }
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
            map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
        }
    }

    static class GeneratorRecipes extends RecipeProvider {
        public GeneratorRecipes(DataGenerator generator) {
            super(generator);
        }

        @Override
        protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
            Block block = ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get();
            ShapedRecipeBuilder
                    .shaped(block)
                    .define('i', Tags.Items.GLASS)
                    .define('r', Items.WATER_BUCKET)
                    .define('d', Tags.Items.GEMS_DIAMOND)
                    .pattern("iii")
                    .pattern("rdr")
                    .pattern("iii")
                    .unlockedBy("has_diamonds", has(Tags.Items.GEMS_DIAMOND))
                    .save(consumer);
        }
    }

    static class GeneratorBlockTags extends BlockTagsProvider {
        public GeneratorBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, InfiniteSource.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags() {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get());
        }
    }
}
