package com.adonis.createshimmer.common.block;

import com.adonis.createshimmer.common.fluids.shimmer.ShimmerLiquidBlock;
import com.adonis.createshimmer.common.registry.CSFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import twilightforest.block.TorchberryPlantBlock;

/**
 * 复生泥土方块 - 能够加速周围植物生长并使浆果类植物重新长出果实
 * 当附近有微光流体时会增强效果并改变外观
 */
public class MagicSoilBlock extends Block {

    public static final BooleanProperty SHIMMER_ENHANCED = BooleanProperty.create("shimmer_enhanced");

    public MagicSoilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(SHIMMER_ENHANCED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHIMMER_ENHANCED);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // 被微光流体增强时发光更亮
        return state.getValue(SHIMMER_ENHANCED) ? 8 : 2;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        // 只有在被微光流体增强时才产生粒子效果
        if (state.getValue(SHIMMER_ENHANCED)) {
            addShimmerParticles(level, pos, random);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        // 检查并更新微光流体增强状态
        boolean hasShimmerNearby = hasShimmerFluidNearby(level, pos);
        boolean currentlyEnhanced = state.getValue(SHIMMER_ENHANCED);

        if (hasShimmerNearby != currentlyEnhanced) {
            level.setBlock(pos, state.setValue(SHIMMER_ENHANCED, hasShimmerNearby), 3);
        }

        // 随机决定是否触发效果，避免过于频繁
        if (random.nextInt(2) != 0) {
            return;
        }

        // 复生效果 - 不需要微光流体限制
        regenerateEffects(level, pos, random);

        // 催生效果 - 需要微光流体
        if (hasShimmerNearby) {
            acceleratePlantGrowth(level, pos, random);
        }
    }

    /**
     * 检查附近是否有微光流体（最简单安全的版本）
     */
    /**
     * 检查附近是否有微光流体
     */
    private boolean hasShimmerFluidNearby(Level level, BlockPos pos) {
        // 检查周围 3x3x3 区域内是否有微光流体
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);

                    try {
                        // 检查流体状态
                        FluidState fluidState = level.getFluidState(checkPos);
                        if (!fluidState.isEmpty()) {
                            // 直接比较流体类型
                            if (fluidState.getType() == CSFluids.SHIMMER.get() ||
                                    fluidState.getType() == CSFluids.SHIMMER.getSource()) {
                                return true;
                            }
                        }

                        // 检查方块（微光流体方块）
                        BlockState blockState = level.getBlockState(checkPos);
                        if (blockState.getBlock() instanceof ShimmerLiquidBlock) {
                            return true;
                        }

                    } catch (Exception e) {
                        // 如果出现任何异常，继续检查下一个位置
                        continue;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 添加微光粒子效果 - 优化的版本，更均匀的粒子分布
     */
    private void addShimmerParticles(Level level, BlockPos pos, RandomSource random) {
        // 增加粒子生成频率，使效果更明显
        if (random.nextInt(3) == 0) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP) { // 排除向上方向
                BlockPos blockpos = pos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos);

                // 简化遮挡检测，让更多粒子能够生成
                if (!blockstate.isSolid() || !blockstate.isFaceSturdy(level, blockpos, direction.getOpposite())) {
                    double d0 = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepX() * 0.6;
                    double d1 = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepY() * 0.6;
                    double d2 = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double)direction.getStepZ() * 0.6;
                    level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
                            (double)pos.getX() + d0,
                            (double)pos.getY() + d1,
                            (double)pos.getZ() + d2,
                            0.0, 0.0, 0.0);
                }
            }
        }

        // 添加额外的粒子从所有侧面生成，确保更均匀的分布
        if (random.nextInt(8) == 0) {
            // 随机选择一个侧面（排除上下）
            Direction[] horizontalDirections = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN};
            Direction dir = horizontalDirections[random.nextInt(horizontalDirections.length)];

            double x = pos.getX() + 0.5 + dir.getStepX() * 0.6 + (random.nextDouble() - 0.5) * 0.2;
            double y = pos.getY() + (dir == Direction.DOWN ? 0.1 : random.nextDouble());
            double z = pos.getZ() + 0.5 + dir.getStepZ() * 0.6 + (random.nextDouble() - 0.5) * 0.2;

            level.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    /**
     * 复生效果 - 复生浆果
     */
    private void regenerateEffects(ServerLevel level, BlockPos pos, RandomSource random) {
        // 检查并复生洞穴藤蔓的发光浆果
        regenerateCaveVineBerries(level, pos, random);

        // 检查并复生火炬浆果
        regenerateTorchberries(level, pos, random);
    }

    /**
     * 复生洞穴藤蔓的发光浆果 - 检查整个藤蔓链
     */
    private void regenerateCaveVineBerries(ServerLevel level, BlockPos pos, RandomSource random) {
        // 检查下方的整个洞穴藤蔓链
        regenerateVineChainBelow(level, pos, random);

        // 检查上方的整个洞穴藤蔓链
        regenerateVineChainAbove(level, pos, random);
    }

    /**
     * 复生下方的洞穴藤蔓链
     */
    private void regenerateVineChainBelow(ServerLevel level, BlockPos startPos, RandomSource random) {
        BlockPos currentPos = startPos.below();
        int maxDepth = 16; // 防止无限循环，最多检查16格
        int depth = 0;

        while (depth < maxDepth) {
            BlockState currentState = level.getBlockState(currentPos);

            if (!isCaveVineBlock(currentState)) {
                break; // 不是洞穴藤蔓，停止检查
            }

            // 如果没有浆果，有概率复生
            if (!CaveVines.hasGlowBerries(currentState)) {
                if (random.nextInt(4) == 0) { // 25% 概率
                    BlockState newState = currentState.setValue(BlockStateProperties.BERRIES, true);
                    level.setBlock(currentPos, newState, 2);
                }
            }

            currentPos = currentPos.below();
            depth++;
        }
    }

    /**
     * 复生上方的洞穴藤蔓链
     */
    private void regenerateVineChainAbove(ServerLevel level, BlockPos startPos, RandomSource random) {
        BlockPos currentPos = startPos.above();
        int maxHeight = 16; // 防止无限循环，最多检查16格
        int height = 0;

        while (height < maxHeight) {
            BlockState currentState = level.getBlockState(currentPos);

            if (!isCaveVineBlock(currentState)) {
                break; // 不是洞穴藤蔓，停止检查
            }

            // 如果没有浆果，有概率复生
            if (!CaveVines.hasGlowBerries(currentState)) {
                if (random.nextInt(4) == 0) { // 25% 概率
                    BlockState newState = currentState.setValue(BlockStateProperties.BERRIES, true);
                    level.setBlock(currentPos, newState, 2);
                }
            }

            currentPos = currentPos.above();
            height++;
        }
    }

    /**
     * 复生火炬浆果 - 只处理直接附着的植物
     */
    private void regenerateTorchberries(ServerLevel level, BlockPos pos, RandomSource random) {
        // 检查上方和下方的火炬浆果植株
        BlockPos[] checkPositions = {pos.above(), pos.below()};

        for (BlockPos checkPos : checkPositions) {
            BlockState checkState = level.getBlockState(checkPos);

            if (checkState.getBlock() instanceof TorchberryPlantBlock) {
                BooleanProperty hasBerries = TorchberryPlantBlock.HAS_BERRIES;
                if (checkState.hasProperty(hasBerries) && !checkState.getValue(hasBerries)) {
                    // 25% 概率复生火炬浆果
                    if (random.nextInt(4) == 0) {
                        BlockState newState = checkState.setValue(hasBerries, true);
                        level.setBlock(checkPos, newState, 2);
                    }
                }
            }
        }
    }

    /**
     * 加速植物生长 - 只在有微光流体时触发
     */
    private void acceleratePlantGrowth(ServerLevel level, BlockPos pos, RandomSource random) {
        // 只检查直接上方和下方的方块
        BlockPos[] checkPositions = {pos.above(), pos.below()};

        for (BlockPos checkPos : checkPositions) {
            BlockState checkState = level.getBlockState(checkPos);
            Block checkBlock = checkState.getBlock();

            // 方法1: 检查是否实现了BonemealableBlock接口，但排除花类
            if (checkBlock instanceof BonemealableBlock bonemealable && !isFlowerBlock(checkState)) {
                tryBonemealGrowth(level, checkPos, checkState, bonemealable, random);
                continue;
            }

            // 方法2: 特殊处理一些原版植物
            handleSpecialVanillaPlants(level, checkPos, checkState, checkBlock, random);
        }
    }

    /**
     * 检查是否为花类方块 - 避免骨粉效果产生额外的花
     */
    private boolean isFlowerBlock(BlockState state) {
        return state.is(BlockTags.FLOWERS) ||
                state.is(BlockTags.SMALL_FLOWERS) ||
                state.is(BlockTags.TALL_FLOWERS);
    }

    /**
     * 尝试骨粉生长效果
     */
    private void tryBonemealGrowth(ServerLevel level, BlockPos pos, BlockState state,
                                   BonemealableBlock bonemealable, RandomSource random) {
        try {
            if (bonemealable.isValidBonemealTarget(level, pos, state) &&
                    bonemealable.isBonemealSuccess(level, random, pos, state)) {
                // 25% 概率触发生长
                if (random.nextInt(4) == 0) {
                    bonemealable.performBonemeal(level, random, pos, state);
                }
            }
        } catch (Exception e) {
            // 忽略异常，某些模组可能有特殊的骨粉逻辑
        }
    }

    /**
     * 处理特殊的原版植物
     */
    private void handleSpecialVanillaPlants(ServerLevel level, BlockPos pos, BlockState state,
                                            Block block, RandomSource random) {
        // 甘蔗特殊处理
        if (block == Blocks.SUGAR_CANE) {
            if (random.nextInt(5) == 0) {
                growSugarCane(level, pos, random);
            }
            return;
        }

        // 仙人掌特殊处理
        if (block == Blocks.CACTUS) {
            if (random.nextInt(5) == 0) {
                growCactus(level, pos, random);
            }
        }
    }

    /**
     * 甘蔗生长逻辑
     */
    private void growSugarCane(ServerLevel level, BlockPos pos, RandomSource random) {
        int height = 1;
        // 计算甘蔗当前高度
        while (level.getBlockState(pos.above(height)).is(Blocks.SUGAR_CANE)) {
            height++;
        }
        // 如果高度小于3，尝试生长
        if (height < 3) {
            BlockPos growPos = pos.above(height);
            if (level.isEmptyBlock(growPos)) {
                level.setBlockAndUpdate(growPos, Blocks.SUGAR_CANE.defaultBlockState());
            }
        }
    }

    /**
     * 仙人掌生长逻辑
     */
    private void growCactus(ServerLevel level, BlockPos pos, RandomSource random) {
        int height = 1;
        // 计算仙人掌当前高度
        while (level.getBlockState(pos.above(height)).is(Blocks.CACTUS)) {
            height++;
        }
        // 如果高度小于3，尝试生长
        if (height < 3) {
            BlockPos growPos = pos.above(height);
            if (level.isEmptyBlock(growPos)) {
                level.setBlockAndUpdate(growPos, Blocks.CACTUS.defaultBlockState());
            }
        }
    }

    /**
     * 检查是否为洞穴藤蔓方块
     */
    private boolean isCaveVineBlock(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.CAVE_VINES ||
                block == Blocks.CAVE_VINES_PLANT ||
                (state.hasProperty(BlockStateProperties.BERRIES) &&
                        (block.toString().contains("cave_vine") ||
                                block.getClass().getSimpleName().toLowerCase().contains("cavevine")));
    }
}