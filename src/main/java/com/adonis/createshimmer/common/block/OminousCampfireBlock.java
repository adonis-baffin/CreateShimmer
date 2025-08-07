package com.adonis.createshimmer.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFParticleType;

public class OminousCampfireBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<OminousCampfireBlock> CODEC = simpleCodec(OminousCampfireBlock::new);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);

    @Override
    public MapCodec<OminousCampfireBlock> codec() {
        return CODEC;
    }

    public OminousCampfireBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(LIT, Boolean.valueOf(true))
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
                        .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        boolean flag = levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState()
                .setValue(WATERLOGGED, Boolean.valueOf(flag))
                .setValue(LIT, Boolean.valueOf(!flag))
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // 如果营火点燃且实体是生物
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            // 对非亡灵生物造成冥火伤害
            if (!entity.getType().is(EntityTypeTags.UNDEAD)) {
                entity.hurt(TFDamageTypes.getDamageSource(level, TFDamageTypes.OMINOUS_FIRE, new EntityType[0]), 1.0F);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            // 营火声音效果
            if (random.nextInt(10) == 0) {
                level.playLocalSound(
                        (double) pos.getX() + 0.5,
                        (double) pos.getY() + 0.5,
                        (double) pos.getZ() + 0.5,
                        SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS,
                        0.5F + random.nextFloat(),
                        random.nextFloat() * 0.7F + 0.6F,
                        false);
            }

            // 使用冥火粒子效果
            if (random.nextInt(5) == 0) {
                for (int i = 0; i < random.nextInt(2) + 1; i++) {
                    level.addParticle(
                            TFParticleType.OMINOUS_FLAME.get(),
                            (double) pos.getX() + 0.25 + random.nextDouble() * 0.5,
                            (double) pos.getY() + 0.4,
                            (double) pos.getZ() + 0.25 + random.nextDouble() * 0.5,
                            (double) (random.nextFloat() / 4.0F),
                            0.02,
                            (double) (random.nextFloat() / 4.0F));
                }
            }

            // 添加烟雾粒子效果 - 仿照原版营火
            makeOminousParticles(level, pos);
        }
    }

    // 静态方法：生成通冥营火的烟雾粒子效果
    public static void makeOminousParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();

        // 主要烟雾效果 - 使用灵魂营火的烟雾类型，符合神秘主题
        level.addAlwaysVisibleParticle(
                net.minecraft.core.particles.ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, // 使用信号烟雾，更浓郁
                true,
                (double) pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1),
                (double) pos.getY() + random.nextDouble() + random.nextDouble(),
                (double) pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1),
                0.0,
                0.07,
                0.0);

        // 额外的普通烟雾
        if (random.nextInt(2) == 0) {
            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.SMOKE,
                    (double) pos.getX() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1),
                    (double) pos.getY() + 0.4,
                    (double) pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1),
                    0.0,
                    0.005,
                    0.0);
        }

        // 偶尔添加一些紫色魔法粒子，增加神秘感
        if (random.nextInt(20) == 0) {
            level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.WITCH,
                    (double) pos.getX() + 0.3 + random.nextDouble() * 0.4,
                    (double) pos.getY() + 0.6,
                    (double) pos.getZ() + 0.3 + random.nextDouble() * 0.4,
                    (double) (random.nextFloat() / 6.0F),
                    0.01,
                    (double) (random.nextFloat() / 6.0F));
        }
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean flag = state.getValue(LIT);
            if (flag) {
                if (!level.isClientSide()) {
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            level.setBlock(pos, state.setValue(WATERLOGGED, Boolean.valueOf(true)).setValue(LIT, Boolean.valueOf(false)), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    // 检查指定位置是否在通冥营火的烟雾范围内
    public static boolean isOminousSmokyPos(Level level, BlockPos pos) {
        for (int i = 1; i <= 5; i++) {
            BlockPos blockpos = pos.below(i);
            BlockState blockstate = level.getBlockState(blockpos);
            if (isLitOminousCampfire(blockstate)) {
                return true;
            }

            // 检查是否有阻挡物（参考原版逻辑）
            boolean flag = net.minecraft.world.phys.shapes.Shapes.joinIsNotEmpty(
                    VIRTUAL_FENCE_POST,
                    blockstate.getCollisionShape(level, blockpos, net.minecraft.world.phys.shapes.CollisionContext.empty()),
                    net.minecraft.world.phys.shapes.BooleanOp.AND);
            if (flag) {
                BlockState blockstate1 = level.getBlockState(blockpos.below());
                return isLitOminousCampfire(blockstate1);
            }
        }
        return false;
    }

    // 检查方块状态是否为点燃的通冥营火
    public static boolean isLitOminousCampfire(BlockState state) {
        return state.getBlock() instanceof OminousCampfireBlock && state.getValue(LIT);
    }

    // 添加虚拟栅栏柱形状常量（用于烟雾检测）
    private static final net.minecraft.world.phys.shapes.VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
}
