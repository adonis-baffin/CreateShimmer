
package com.adonis.createshimmer.common.fluids.experience;

import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayer;
import plus.dragons.createdragonsplus.common.processing.blaze.BlazeBlock;

public abstract class BlazeExperienceBlock<T extends BlazeExperienceBlockEntity> extends BlazeBlock<T> {
    public BlazeExperienceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        T blockEntity = getBlockEntity(level, pos);
        if (blockEntity == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        boolean notConsume = player.hasInfiniteMaterials();
        boolean forceOverflow = !(player instanceof FakePlayer);
        var resultHolder = applyFuel(state, level, pos, stack, forceOverflow, notConsume, false);
        var result = resultHolder.getResult();
        if (result == InteractionResult.PASS)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (result == InteractionResult.FAIL)
            return ItemInteractionResult.FAIL;
        var remainder = resultHolder.getObject();
        if (!remainder.isEmpty()) {
            if (stack.isEmpty())
                player.setItemInHand(hand, remainder);
            else
                player.getInventory().placeItemBackInInventory(remainder);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    public static InteractionResultHolder<ItemStack> applyFuel(BlockState state, Level level, BlockPos pos, ItemStack stack, boolean forceOverflow, boolean notConsume, boolean simulate) {
        if (!state.hasBlockEntity())
            return InteractionResultHolder.fail(ItemStack.EMPTY);

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof BlazeExperienceBlockEntity blaze))
            return InteractionResultHolder.fail(ItemStack.EMPTY);

        if (stack.is(AllItems.CREATIVE_BLAZE_CAKE)) {
            blaze.applyCreativeFuel();
            if (!notConsume)
                stack.shrink(1);
            return InteractionResultHolder.success(ItemStack.EMPTY);
        }
        var fuel = ExperienceFuel.get(level, stack);
        if (fuel != null) {
            boolean applied = blaze.applyExperienceFuel(fuel, forceOverflow, simulate);
            if (applied) {
                if (!notConsume)
                    stack.shrink(1);
                ItemStack remainder = notConsume
                        ? ItemStack.EMPTY
                        : fuel.usingConvertTo().orElse(stack.getCraftingRemainingItem());
                return InteractionResultHolder.success(remainder);
            }
            return InteractionResultHolder.fail(ItemStack.EMPTY);
        }
        return InteractionResultHolder.pass(ItemStack.EMPTY);
    }
}
