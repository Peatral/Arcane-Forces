package xyz.peatral.arcaneforces.infrastructure.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModBlocks;
import xyz.peatral.arcaneforces.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
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
    }
}
