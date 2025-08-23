package com.adonis.createshimmer.mixin;

import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block {
    public SugarCaneBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * 修改canSurvive方法，让甘蔗能在复生泥土上种植，并识别微光流体
     */
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    protected void injectCanSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockStateBelow = level.getBlockState(pos.below());

        // 如果下方是甘蔗，直接返回true（原版逻辑）
        if (blockStateBelow.is(this)) {
            cir.setReturnValue(true);
            return;
        }

        // 检查TriState（原版逻辑）
        TriState soilDecision = blockStateBelow.canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) {
            cir.setReturnValue(soilDecision.isTrue());
            return;
        }

        // 检查是否为支持的土壤类型（包括复生泥土）
        boolean isSupportedSoil = blockStateBelow.is(BlockTags.DIRT) ||
                blockStateBelow.is(BlockTags.SAND) ||
                blockStateBelow.is(CSBlocks.MAGIC_SOIL.get());

        if (isSupportedSoil) {
            BlockPos blockPosBelow = pos.below();

            // 检查周围是否有水或微光流体
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockState adjacentState = level.getBlockState(blockPosBelow.relative(direction));
                FluidState fluidState = level.getFluidState(blockPosBelow.relative(direction));

                // 检查是否能被滋润（原版水判断）
                if (state.canBeHydrated(level, pos, fluidState, blockPosBelow.relative(direction))) {
                    cir.setReturnValue(true);
                    return;
                }

                // 检查是否为霜冰（原版逻辑）
                if (adjacentState.is(Blocks.FROSTED_ICE)) {
                    cir.setReturnValue(true);
                    return;
                }

                // 检查是否为微光流体
                if (isShimmerFluid(fluidState)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }

        cir.setReturnValue(false);
    }

    /**
     * 检查是否为微光流体
     */
    private boolean isShimmerFluid(FluidState fluidState) {
        if (fluidState.isEmpty()) {
            return false;
        }

        // 检查是否为微光流体
        return fluidState.getType() == CSFluids.SHIMMER.get() ||
                fluidState.getType() == CSFluids.SHIMMER.getSource();
    }
}
