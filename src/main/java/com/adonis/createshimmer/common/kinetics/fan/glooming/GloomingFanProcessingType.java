package com.adonis.createshimmer.common.kinetics.fan.glooming;

import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.config.CSConfig;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import java.util.List;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFParticleType;

public class GloomingFanProcessingType implements FanProcessingType {
    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        boolean configEnabled = CSConfig.recipes().enableBulkGlooming.get();

        if (!configEnabled) {
            return false;
        }

        var blockState = level.getBlockState(pos);
        var catalystTag = CSBlocks.MOD_TAGS.fanGloomingCatalysts;

        // 检查标签内容
        var registry = level.registryAccess().registryOrThrow(BuiltInRegistries.BLOCK.key());
        if (!registry.getTag(catalystTag).isPresent()) {
            return false;
        }

        return blockState.is(catalystTag);
    }

    @Override
    public int getPriority() {
        return 380; // 比批量转换优先级更高
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        var recipeManager = level.getRecipeManager();
        var input = new SingleRecipeInput(stack);
        return recipeManager
                .getRecipeFor(CSRecipes.GLOOMING.getType(), input, level)
                .isPresent();
    }

    @Override
    public @Nullable List<ItemStack> process(ItemStack stack, Level level) {
        var recipeManager = level.getRecipeManager();
        var input = new SingleRecipeInput(stack);
        return recipeManager
                .getRecipeFor(CSRecipes.GLOOMING.getType(), input, level)
                .map(recipe -> recipe.value().rollResults(level.random))  // 滚动配方输出，支持随机/多个物品
                .orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(6) == 0) {
            // 只使用暮色森林的冥火粒子效果
            level.addParticle(
                    TFParticleType.OMINOUS_FLAME.get(),
                    pos.x + (level.random.nextFloat() - .5f) * .5f,
                    pos.y + .5f,
                    pos.z + (level.random.nextFloat() - .5f) * .5f,
                    (level.random.nextFloat() - .5f) * 0.1f,
                    0.15f,
                    (level.random.nextFloat() - .5f) * 0.1f);
        }
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        // 更偏蓝的紫色主题，体现冥界的神秘
        particleAccess.setColor(Color.mixColors(0x6A0DAD, 0x9370DB, random.nextFloat())); // 靛蓝紫到中兰花紫
        particleAccess.setAlpha(0.7f); // 略微透亮
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (level.isClientSide)
            return;

        // 增加伤害频率：每5tick检查一次（原来是20tick）
        if (!entity.getType().is(EntityTypeTags.UNDEAD) && entity.tickCount % 5 == 0) {
            entity.hurt(TFDamageTypes.getDamageSource(level, TFDamageTypes.OMINOUS_FIRE, new EntityType[0]), 1.0F);
        }
    }
}
