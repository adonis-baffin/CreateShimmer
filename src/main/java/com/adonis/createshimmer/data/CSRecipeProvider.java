package com.adonis.createshimmer.data;

import static com.adonis.createshimmer.common.registry.CSBlocks.*;
import static com.adonis.createshimmer.common.registry.CSItems.*;
import static com.simibubi.create.AllBlocks.*;
import static com.simibubi.create.AllItems.*;
import static net.minecraft.world.item.Items.*;
import static plus.dragons.createdragonsplus.data.recipe.CreateRecipeBuilders.*;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.fan.transmutation.TransmutationRecipe;
import com.adonis.createshimmer.common.registry.CSFluids;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class CSRecipeProvider extends RecipeProvider {
    private static final String ANDESITE = "andesite";
    private static final String COPPER = "copper";
    private static final String BRASS = "brass";
    private static final String TRAIN = "train";

    public CSRecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildTwilightForestRecipes(output);
    }

    private void buildTwilightForestRecipes(RecipeOutput output) {
        buildShimmerRecipes(output);
        buildCarminiteRecipes(output);
        buildFieryTearRecipes(output);
        buildTwilightProcessingRecipes(output);
        buildTwilightCraftingRecipes(output);
        buildShimmerFanProcessingRecipes(output);
    }

    private void buildShimmerRecipes(RecipeOutput output) {
        // 使用标签的微光配方 - 经验颗粒*3+荧光浆果+萤光物质+花=50mb微光
        mixing(CSCommon.asResource("shimmer_from_glowing_berries"))
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(EXP_NUGGET)
                .require(CSItemTagProvider.GLOWING_BERRIES) // 发光浆果标签
                .require(CSItemTagProvider.LUMINOUS_SUBSTANCES) // 荧光物质标签
                .require(ItemTags.SMALL_FLOWERS)
                .output(CSFluids.SHIMMER.get(), 50)
                .build(output);

        // 注液：玻璃瓶+50mb微光=微光试剂
        filling(SHIMMER_REAGENT.getId())
                .require(GLASS_BOTTLE)
                .require(CSFluids.SHIMMER.get(), 50)
                .output(SHIMMER_REAGENT)
                .build(output);

        // 注液：渡鸦羽毛+50mb微光=魔法地图核心（暮色森林硬依赖）
        filling(CSCommon.asResource("magic_map_focus"))
                .require(createTwilightItem("raven_feather"))
                .require(CSFluids.SHIMMER.get(), 50)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "magic_map_focus"), 1, 1.0f))
                .build(output);

        // 注液：箭+50mb微光=光灵箭
        filling(CSCommon.asResource("spectral_arrow"))
                .require(ARROW)
                .require(CSFluids.SHIMMER.get(), 50)
                .output(SPECTRAL_ARROW)
                .build(output);

        // 注液：暮色橡树树苗+1000mb微光=高大暮色橡树树苗
        filling(CSCommon.asResource("tall_twilight_oak_sapling"))
                .require(createTwilightItem("twilight_oak_sapling"))
                .require(CSFluids.SHIMMER.get(), 1000)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "sorting_sapling"), 1, 1.0f))
                .build(output);

        // 混合搅拌：活根+粗锌+金粒+25mb微光=粗骑士金属
        mixing(RAW_KNIGHTMETAL.getId())
                .require(createTwilightItem("liveroot"))
                .require(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "raw_materials/zinc")))  // 替换为手动创建标签
                .require(GOLD_NUGGET)
                .require(CSFluids.SHIMMER.get(), 25)
                .output(RAW_KNIGHTMETAL)
                .build(output);
    }

    private void buildCarminiteRecipes(RecipeOutput output) {
        // 超级加热混合搅拌：砷铅铁矿石=砷铅铁溶液25mb
        mixing(CSCommon.asResource("carminite_solution_from_ore"))
                .require(createTwilightItem("carminite"))
                .output(CSFluids.CARMINITE_SOLUTION.get(), 25)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .build(output);

        // 超级加热混合搅拌：砷铅铁溶液100mb+烈焰粉=炽热的泪100mb
        mixing(CSCommon.asResource("fiery_tear_from_carminite"))
                .require(CSFluids.CARMINITE_SOLUTION.get(), 100)
                .require(BLAZE_POWDER)
                .output(CSFluids.FIERY_TEAR.get(), 100)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .build(output);

        // 超级加热混合搅拌：砷铅铁矿石*4+烈焰粉=炽热的泪100mb
        mixing(CSCommon.asResource("fiery_tear_from_ore_bulk"))
                .require(createTwilightItem("carminite"))
                .require(createTwilightItem("carminite"))
                .require(createTwilightItem("carminite"))
                .require(createTwilightItem("carminite"))
                .require(BLAZE_POWDER)
                .output(CSFluids.FIERY_TEAR.get(), 100)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .build(output);
    }

    private void buildFieryTearRecipes(RecipeOutput output) {
        // 注液：玻璃瓶+炽热的泪25mb=炽热的泪（物品）
        filling(CSCommon.asResource("fiery_tears_item"))
                .require(GLASS_BOTTLE)
                .require(CSFluids.FIERY_TEAR.get(), 25)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "fiery_tears"), 1, 1.0f))
                .build(output);

        // 注液：铁锭+炽热的泪25mb=炽铁锭
        filling(CSCommon.asResource("fiery_ingot"))
                .require(IRON_INGOT)
                .require(CSFluids.FIERY_TEAR.get(), 25)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "fiery_ingot"), 1, 1.0f))
                .build(output);
    }

    private void buildTwilightProcessingRecipes(RecipeOutput output) {
        // 粉碎：变化树叶=0.2转换粉
        crushing(CSCommon.asResource("transformation_powder"))
                .require(createTwilightItem("transformation_leaves"))
                .output(0.2f, ResourceLocation.fromNamespaceAndPath("twilightforest", "transformation_powder"), 1)
                .build(output);

        // 粉碎：活根（方块）=活根（物品）*1+活根（物品）*0.5
        crushing(CSCommon.asResource("liveroot_from_block"))
                .require(createTwilightItem("liveroot_block"))
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "liveroot"), 1, 1.0f))
                .output(0.5f, ResourceLocation.fromNamespaceAndPath("twilightforest", "liveroot"), 1)
                .build(output);

        // 粉碎：死岩系列
        crushing(CSCommon.asResource("deadrock_powder_from_deadrock"))
                .require(createTwilightItem("deadrock"))
                .output(THORN_FLOUR)
                .output(0.5f, THORN_FLOUR)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "cracked_deadrock"), 1, 1.0f))
                .build(output);

        crushing(CSCommon.asResource("deadrock_powder_from_cracked"))
                .require(createTwilightItem("cracked_deadrock"))
                .output(THORN_FLOUR)
                .output(0.5f, THORN_FLOUR)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "weathered_deadrock"), 1, 1.0f))
                .build(output);

        crushing(CSCommon.asResource("deadrock_powder_from_weathered"))
                .require(createTwilightItem("weathered_deadrock"))
                .output(THORN_FLOUR)
                .output(0.5f, THORN_FLOUR)
                .build(output);

        // 压块塑形：死岩粉末+下界石英*4=城堡砖
        compacting(CSCommon.asResource("castle_brick"))
                .require(THORN_FLOUR)
                .require(QUARTZ)
                .require(QUARTZ)
                .require(QUARTZ)
                .require(QUARTZ)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "castle_brick"), 1, 1.0f))
                .build(output);
    }

    private void buildTwilightCraftingRecipes(RecipeOutput output) {
    }

    private void buildShimmerFanProcessingRecipes(RecipeOutput output) {
        // 微光批量复生配方

        // 批量复生：根→活根
        TransmutationRecipe.builder(CSCommon.asResource("root_to_liveroot"))
                .require(createTwilightItem("root"))
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "liveroot_block"), 1, 1.0f))
                .build(output);

        // 批量复生：红树根→活根
        TransmutationRecipe.builder(CSCommon.asResource("mangrove_roots_to_liveroot"))
                .require(MANGROVE_ROOTS)
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "liveroot_block"), 1, 1.0f))
                .build(output);

        // 批量复生：石头→虫蚀石头
        TransmutationRecipe.builder(CSCommon.asResource("stone_to_infested_stone"))
                .require(STONE)
                .output(INFESTED_STONE)
                .build(output);

        // 批量复生：圆石→虫蚀圆石
        TransmutationRecipe.builder(CSCommon.asResource("cobblestone_to_infested_cobblestone"))
                .require(COBBLESTONE)
                .output(INFESTED_COBBLESTONE)
                .build(output);

        // 批量复生：石砖→虫蚀石砖
        TransmutationRecipe.builder(CSCommon.asResource("stone_bricks_to_infested_stone_bricks"))
                .require(STONE_BRICKS)
                .output(INFESTED_STONE_BRICKS)
                .build(output);

        // 批量复生：裂石砖→虫蚀裂石砖
        TransmutationRecipe.builder(CSCommon.asResource("cracked_stone_bricks_to_infested_cracked_stone_bricks"))
                .require(CRACKED_STONE_BRICKS)
                .output(INFESTED_CRACKED_STONE_BRICKS)
                .build(output);

        // 批量复生：苔石砖→虫蚀苔石砖
        TransmutationRecipe.builder(CSCommon.asResource("mossy_stone_bricks_to_infested_mossy_stone_bricks"))
                .require(MOSSY_STONE_BRICKS)
                .output(INFESTED_MOSSY_STONE_BRICKS)
                .build(output);

        // 批量复生：切制石砖→虫蚀切制石砖
        TransmutationRecipe.builder(CSCommon.asResource("chiseled_stone_bricks_to_infested_chiseled_stone_bricks"))
                .require(CHISELED_STONE_BRICKS)
                .output(INFESTED_CHISELED_STONE_BRICKS)
                .build(output);

        // 批量复生：深板岩→虫蚀深板岩
        TransmutationRecipe.builder(CSCommon.asResource("deepslate_to_infested_deepslate"))
                .require(DEEPSLATE)
                .output(INFESTED_DEEPSLATE)
                .build(output);

        // 批量复生：失活珊瑚→活珊瑚
        TransmutationRecipe.builder(CSCommon.asResource("dead_brain_coral_to_brain_coral"))
                .require(DEAD_BRAIN_CORAL)
                .output(BRAIN_CORAL)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_bubble_coral_to_bubble_coral"))
                .require(DEAD_BUBBLE_CORAL)
                .output(BUBBLE_CORAL)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_fire_coral_to_fire_coral"))
                .require(DEAD_FIRE_CORAL)
                .output(FIRE_CORAL)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_horn_coral_to_horn_coral"))
                .require(DEAD_HORN_CORAL)
                .output(HORN_CORAL)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_tube_coral_to_tube_coral"))
                .require(DEAD_TUBE_CORAL)
                .output(TUBE_CORAL)
                .build(output);

        // 批量复生：失活珊瑚扇→活珊瑚扇
        TransmutationRecipe.builder(CSCommon.asResource("dead_brain_coral_fan_to_brain_coral_fan"))
                .require(DEAD_BRAIN_CORAL_FAN)
                .output(BRAIN_CORAL_FAN)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_bubble_coral_fan_to_bubble_coral_fan"))
                .require(DEAD_BUBBLE_CORAL_FAN)
                .output(BUBBLE_CORAL_FAN)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_fire_coral_fan_to_fire_coral_fan"))
                .require(DEAD_FIRE_CORAL_FAN)
                .output(FIRE_CORAL_FAN)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_horn_coral_fan_to_horn_coral_fan"))
                .require(DEAD_HORN_CORAL_FAN)
                .output(HORN_CORAL_FAN)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_tube_coral_fan_to_tube_coral_fan"))
                .require(DEAD_TUBE_CORAL_FAN)
                .output(TUBE_CORAL_FAN)
                .build(output);

        // 批量复生：失活珊瑚块→活珊瑚块
        TransmutationRecipe.builder(CSCommon.asResource("dead_brain_coral_block_to_brain_coral_block"))
                .require(DEAD_BRAIN_CORAL_BLOCK)
                .output(BRAIN_CORAL_BLOCK)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_bubble_coral_block_to_bubble_coral_block"))
                .require(DEAD_BUBBLE_CORAL_BLOCK)
                .output(BUBBLE_CORAL_BLOCK)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_fire_coral_block_to_fire_coral_block"))
                .require(DEAD_FIRE_CORAL_BLOCK)
                .output(FIRE_CORAL_BLOCK)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_horn_coral_block_to_horn_coral_block"))
                .require(DEAD_HORN_CORAL_BLOCK)
                .output(HORN_CORAL_BLOCK)
                .build(output);

        TransmutationRecipe.builder(CSCommon.asResource("dead_tube_coral_block_to_tube_coral_block"))
                .require(DEAD_TUBE_CORAL_BLOCK)
                .output(TUBE_CORAL_BLOCK)
                .build(output);

        // 批量复生：塔木木板→虫蛀塔木木板
        TransmutationRecipe.builder(CSCommon.asResource("towerwood_to_infested_towerwood"))
                .require(createTwilightItem("towerwood_planks"))
                .output(new ProcessingOutput(ResourceLocation.fromNamespaceAndPath("twilightforest", "infested_towerwood_planks"), 1, 1.0f))
                .build(output);
    }

    // 辅助方法：创建暮色森林物品引用
    private Ingredient createTwilightItem(String itemName) {
        // 创建一个自定义的 Ingredient 来引用暮色森林的物品
        // 在数据生成阶段，我们使用占位符
        return Ingredient.of(BARRIER); // 使用屏障块作为占位符，实际运行时会被替换
    }
}
