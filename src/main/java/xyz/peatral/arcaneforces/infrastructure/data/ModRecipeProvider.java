package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModItems;
import xyz.peatral.arcaneforces.content.incense.IncenseLogBlock;
import xyz.peatral.arcaneforces.content.incense.TappingRecipe;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    public static void tappingRecipe(
            String name,
            BlockState input,
            List<Property<?>> requiredProps,
            BlockState blockOutput,
            List<Property<?>> propsToCopy,
            ItemStack dropOutput,
            float chance,
            Map<String, Criterion<?>> criteria,
            RecipeOutput pRecipeOutput
    ) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, name);
        TappingRecipe recipe = new TappingRecipe(
                input,
                requiredProps.stream().map(Property::getName).toList(),
                blockOutput,
                propsToCopy.stream().map(Property::getName).toList(),
                dropOutput,
                chance
        );
        Advancement.Builder builder = pRecipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
                .rewards(AdvancementRewards.Builder.recipe(location))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(builder::addCriterion);
        pRecipeOutput.accept(location, recipe, builder.build(location.withPrefix("recipes/tapping/")));
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.OLIBANUM_WOOD.block().get(), 3))
                .define('L', ModBlocks.OLIBANUM_LOG.block().get())
                .pattern("LL")
                .pattern("LL")
                .unlockedBy(getHasName(ModBlocks.OLIBANUM_LOG.block().get()), has(ModBlocks.OLIBANUM_LOG.block().get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "olibanum_wood"));
        ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.STRIPPED_OLIBANUM_WOOD.block().get(), 3))
                .define('L', ModBlocks.STRIPPED_OLIBANUM_LOG.block().get())
                .pattern("LL")
                .pattern("LL")
                .unlockedBy(getHasName(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get()), has(ModBlocks.STRIPPED_OLIBANUM_LOG.block().get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "stripped_olibanum_wood"));
        ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.MYRRH_WOOD.block().get(), 3))
                .define('L', ModBlocks.MYRRH_LOG.block().get())
                .pattern("LL")
                .pattern("LL")
                .unlockedBy(getHasName(ModBlocks.MYRRH_LOG.block().get()), has(ModBlocks.MYRRH_LOG.block().get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "myrrh_wood"));
        ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModBlocks.STRIPPED_MYRRH_WOOD.block().get(), 3))
                .define('L', ModBlocks.STRIPPED_MYRRH_LOG.block().get())
                .pattern("LL")
                .pattern("LL")
                .unlockedBy(getHasName(ModBlocks.STRIPPED_MYRRH_LOG.block().get()), has(ModBlocks.STRIPPED_MYRRH_LOG.block().get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "stripped_myrrh_wood"));

        ShapedRecipeBuilder
                .shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RITUAL_DAGGER.get())
                .define('G', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .pattern(" G")
                .pattern("S ")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "ritual_dagger"));

        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.DECORATIONS, ModBlocks.INCENSE_STICK.block().get())
                .requires(Blocks.BAMBOO)
                .requires(ModItems.FRAGRANT_RESIN.get())
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .unlockedBy(getHasName(ModItems.FRAGRANT_RESIN.get()), has(ModItems.FRAGRANT_RESIN.get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "incense_stick"));

        tappingRecipe(
                "olibanum_resin",
                ModBlocks.OLIBANUM_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.RESIN),
                List.of(IncenseLogBlock.STATE),
                ModBlocks.OLIBANUM_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.SCARRED),
                List.of(IncenseLogBlock.AXIS, IncenseLogBlock.PLACED_BY_PLAYER),
                new ItemStack(ModItems.FRAGRANT_RESIN.get()),
                1.0f,
                Map.of(getHasName(ModItems.RITUAL_DAGGER.get()), has(ModItems.RITUAL_DAGGER.get())),
                pRecipeOutput
        );
        tappingRecipe(
                "myrrh_resin",
                ModBlocks.MYRRH_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.RESIN),
                List.of(IncenseLogBlock.STATE),
                ModBlocks.MYRRH_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.SCARRED),
                List.of(IncenseLogBlock.AXIS, IncenseLogBlock.PLACED_BY_PLAYER),
                new ItemStack(ModItems.FRAGRANT_RESIN.get()),
                1.0f,
                Map.of(getHasName(ModItems.RITUAL_DAGGER.get()), has(ModItems.RITUAL_DAGGER.get())),
                pRecipeOutput
        );

        tappingRecipe(
                "olibanum_scarred",
                ModBlocks.OLIBANUM_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.DEFAULT),
                List.of(IncenseLogBlock.STATE, IncenseLogBlock.PLACED_BY_PLAYER),
                ModBlocks.OLIBANUM_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.SCARRED),
                List.of(IncenseLogBlock.AXIS, IncenseLogBlock.PLACED_BY_PLAYER),
                new ItemStack(ModItems.FRAGRANT_RESIN.get()),
                0.05f,
                Map.of(getHasName(ModItems.RITUAL_DAGGER.get()), has(ModItems.RITUAL_DAGGER.get())),
                pRecipeOutput
        );
        tappingRecipe(
                "myrrh_scarred",
                ModBlocks.MYRRH_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.DEFAULT),
                List.of(IncenseLogBlock.STATE, IncenseLogBlock.PLACED_BY_PLAYER),
                ModBlocks.MYRRH_LOG.block().get().defaultBlockState().setValue(IncenseLogBlock.STATE, IncenseLogBlock.State.SCARRED),
                List.of(IncenseLogBlock.AXIS, IncenseLogBlock.PLACED_BY_PLAYER),
                new ItemStack(ModItems.FRAGRANT_RESIN.get()),
                0.05f,
                Map.of(getHasName(ModItems.RITUAL_DAGGER.get()), has(ModItems.RITUAL_DAGGER.get())),
                pRecipeOutput
        );
    }
}
