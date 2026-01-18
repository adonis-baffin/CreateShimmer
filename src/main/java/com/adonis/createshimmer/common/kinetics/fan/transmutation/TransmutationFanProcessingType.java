package com.adonis.createshimmer.common.kinetics.fan.transmutation;

import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.common.registry.CSFluids;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.config.CSConfig;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import java.util.List;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TransmutationFanProcessingType implements FanProcessingType {
    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        boolean configEnabled = CSConfig.recipes().enableBulkTransmutation.get();
        if (!configEnabled) {
            return false;
        }

        var blockState = level.getBlockState(pos);
        if (blockState.is(CSBlocks.MOD_TAGS.fanTransmutationCatalysts)) {
            return true;
        }

        var fluidState = level.getFluidState(pos);
        if (fluidState.is(CSFluids.MOD_TAGS.fanTransmutationCatalysts)) {
            return true;
        }

        return false;
    }

    @Override
    public int getPriority() {
        return 370; // 应该比批量终结优先级更高
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        var recipeManager = level.getRecipeManager();
        var input = new SingleRecipeInput(stack);
        return recipeManager
                .getRecipeFor(CSRecipes.TRANSMUTATION.getType(), input, level)
                .isPresent();
    }

    @Override
    public @Nullable List<ItemStack> process(ItemStack stack, Level level) {
        var recipeManager = level.getRecipeManager();
        var input = new SingleRecipeInput(stack);
        return recipeManager
                .getRecipeFor(CSRecipes.TRANSMUTATION.getType(), input, level)
                .map(recipe -> RecipeApplier.applyRecipeOn(level, stack, recipe.value(), false))
                .orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(6) == 0) {
            // 生成附魔台粒子效果
            level.addParticle(
                    ParticleTypes.ENCHANT,
                    pos.x + (level.random.nextFloat() - .5f) * .5f,
                    pos.y + .5f,
                    pos.z + (level.random.nextFloat() - .5f) * .5f,
                    0, 0.5f, 0);
        }
        if (level.random.nextInt(12) == 0) {
            // 偶尔生成金色闪烁粒子
            level.addParticle(
                    ParticleTypes.CRIT,
                    pos.x + (level.random.nextFloat() - .5f) * .5f,
                    pos.y + .5f,
                    pos.z + (level.random.nextFloat() - .5f) * .5f,
                    (level.random.nextFloat() - .5f) * 0.2f,
                    0.1f,
                    (level.random.nextFloat() - .5f) * 0.2f);
        }
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(0x9A4DFF, 0xFFD700, random.nextFloat()));
        particleAccess.setAlpha(1f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (level.isClientSide)
            return;
        if (entity instanceof LivingEntity livingEntity && livingEntity.isAffectedByPotions() && entity.tickCount % 20 == 0) {
            // 给予发光效果，体现"复生"的主题
            livingEntity.addEffect(new MobEffectInstance(CSEffects.SHIMMER_EFFECT, 60, 0));
        }
    }
}
