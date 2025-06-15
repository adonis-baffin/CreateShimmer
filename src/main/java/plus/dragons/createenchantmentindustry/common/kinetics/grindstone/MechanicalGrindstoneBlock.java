/*
 * Copyright (C) 2025  DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package plus.dragons.createenchantmentindustry.common.kinetics.grindstone;

import com.simibubi.create.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import plus.dragons.createenchantmentindustry.common.fluids.experience.ExperienceHelper;
import plus.dragons.createenchantmentindustry.common.registry.CEIAdvancements;
import plus.dragons.createenchantmentindustry.common.registry.CEIBlockEntities;
import plus.dragons.createenchantmentindustry.common.registry.CEIStats;

public class MechanicalGrindstoneBlock extends RotatedPillarKineticBlock implements IBE<KineticBlockEntity> {
    protected static VoxelShaper SHAPE = new AllShapes.Builder(Block.box(3, 3, 3, 13, 13, 13))
            .add(AllShapes.SIX_VOXEL_POLE.get(Axis.Y))
            .forAxis();

    public MechanicalGrindstoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (player.isSecondaryUseActive())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        var blockEntity = getBlockEntity(level, pos);
        if (blockEntity == null || Math.abs(blockEntity.getSpeed()) < 30)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        var location = hitResult.getLocation();
        // Sandpaper Polishing
        if (SandPaperPolishingRecipe.canPolish(level, stack)) {
            var item = stack.getItem();
            var fake = player instanceof FakePlayer;
            if (!fake && player.getCooldowns().isOnCooldown(item))
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            var polished = SandPaperPolishingRecipe.applyPolish(level, Vec3.atCenterOf(pos), stack, null);
            if (!fake)
                player.getCooldowns().addCooldown(item, 10);
            SandPaperItem.spawnParticles(location, stack, level);
            AllSoundEvents.SANDING_SHORT.play(level, player, pos, 1, 1);
            stack.shrink(1);
            CEIAdvancements.GRIND_TO_POLISH.awardTo(player);
            if (stack.isEmpty()) {
                player.setItemInHand(hand, polished);
            } else {
                player.getInventory().placeItemBackInInventory(polished);
            }
        } else {
            // Grindstone
            var otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            var otherStack = player.getItemInHand(otherHand);
            var optional = GrindstoneHelper.grindItem(level, stack, otherStack);
            if (optional.isEmpty())
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            var result = optional.get();
            if (result.top().isEmpty()) {
                player.setItemInHand(hand, result.output());
            } else {
                player.setItemInHand(hand, result.top());
                player.getInventory().placeItemBackInInventory(result.output());
            }
            player.setItemInHand(otherHand, result.bottom());
            CEIAdvancements.GONE_WITH_THE_FOIL.awardTo(player);
            player.awardStat(CEIStats.GRINDSTONE_EXPERIENCE.get(), result.experience());
            if (player instanceof ServerPlayer serverPlayer)
                ExperienceHelper.award(result.experience(), serverPlayer);
            level.levelEvent(1042, pos, 0);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(AXIS));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CEIBlockEntities.MECHANICAL_GRINDSTONE.get();
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }
}
