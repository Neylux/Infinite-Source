package de.neylux.infinitesource.register.data;


import com.google.common.collect.ImmutableList;
import de.neylux.infinitesource.InfiniteSource;
import de.neylux.infinitesource.register.types.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = InfiniteSource.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();
        PackOutput packOutput = event.getGenerator().getPackOutput();

        // Client
        generator.addProvider(includeClient, new GeneratorLanguage(packOutput));
        generator.addProvider(includeClient, new GeneratorBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(includeClient, new GeneratorLoots(packOutput));

        // Server
        generator.addProvider(includeServer, new GeneratorRecipes(packOutput));
        generator.addProvider(includeServer, new GeneratorBlockTags(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
        ;
        generator.addProvider(includeServer, new GeneratorItemModels(packOutput, event.getExistingFileHelper()));

    }

    static class GeneratorBlockStates extends BlockStateProvider {


        public GeneratorBlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
            super(output, InfiniteSource.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            horizontalBlock(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get(), models()
                    .cubeAll(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get())).getPath(),
                            modLoc("block/infinite_water_source_block")));
        }
    }

    static class GeneratorItemModels extends ItemModelProvider {

        public GeneratorItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, InfiniteSource.MOD_ID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            String path = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get())).getPath();
            getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
        }

        @Override
        public String getName() {
            return "Item Models";
        }
    }

    static class GeneratorLanguage extends LanguageProvider {
        public GeneratorLanguage(PackOutput output) {
            super(output, InfiniteSource.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            addBlock(ModBlocks.INFINITE_WATER_SOURCE_BLOCK, "Infinite Water Source");
            add("item_group.infinitesource.infinitesource_tab", "Infinite Source");
        }
    }

    static class GeneratorLoots extends LootTableProvider {
        public GeneratorLoots(PackOutput output) {
            super(output, Set.of(), ImmutableList.of(
                    new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)
            ));
        }

        private static class Blocks extends BlockLootSubProvider {

            protected Blocks() {
                super(Set.of(), FeatureFlags.REGISTRY.allFlags());
            }

            @Override
            protected void generate() {
                LootPool.Builder builder = LootPool.lootPool()
                        .name(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get())).toString())
                        .setRolls(ConstantValue.exactly(1))
                        .when(ExplosionCondition.survivesExplosion())
                        .add(LootItem.lootTableItem(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get())
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)));
                this.add(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get(), LootTable.lootTable().withPool(builder));
            }

            @Override
            protected @NotNull Iterable<Block> getKnownBlocks() {
                return ImmutableList.of(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get());
            }
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
            map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
        }
    }

    static class GeneratorRecipes extends RecipeProvider {
        public GeneratorRecipes(PackOutput output) {
            super(output);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get(), 1)
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
        public GeneratorBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, InfiniteSource.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookup) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.INFINITE_WATER_SOURCE_BLOCK.get());
        }
    }
}
