//package com.adonis.createshimmer.common.block;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BooleanProperty;
//import net.minecraft.world.level.block.state.properties.Property;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.Shapes;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//public class CSCastleDoorBlock extends Block {
//    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
//    public static final BooleanProperty VANISHED = BooleanProperty.create("vanish");
//    private static final VoxelShape REAPPEARING_BB = Shapes.create(new AABB(0.375, 0.375, 0.375, 0.625, 0.625, 0.625));
//
//    public CSCastleDoorBlock(BlockBehaviour.Properties properties) {
//        super(properties);
//        this.registerDefaultState(this.getStateDefinition().any()
//                .setValue(ACTIVE, false)
//                .setValue(VANISHED, false));
//    }
//
//    private static boolean isBlockLocked(Level level, BlockPos pos) {
//        // 基础版本不需要锁定机制，直接返回false
//        return false;
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(ACTIVE, VANISHED);
//    }
//
//    @Override
//    public boolean skipRendering(BlockState state, BlockState otherState, Direction direction) {
//        return otherState.getBlock() instanceof CSCastleDoorBlock &&
//               otherState.getValue(VANISHED).equals(state.getValue(VANISHED));
//    }
//
//    @Override
//    public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
//        return !state.getValue(VANISHED) && state.getValue(ACTIVE) ?
//               super.getOcclusionShape(state, getter, pos) : Shapes.empty();
//    }
//
//    @Override
//    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        return state.getValue(VANISHED) ? Shapes.empty() :
//               super.getCollisionShape(state, getter, pos, context);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
//        return state.getValue(VANISHED) ? REAPPEARING_BB :
//               super.getShape(state, getter, pos, context);
//    }
//
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
//        return this.onActivation(level, pos, state);
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
//        if (!(block instanceof CSCastleDoorBlock) && level.hasNeighborSignal(pos)) {
//            this.onActivation(level, pos, state);
//        }
//    }
//
//    private InteractionResult onActivation(Level level, BlockPos pos, BlockState state) {
//        if (!state.getValue(VANISHED) && !state.getValue(ACTIVE)) {
//            if (isBlockLocked(level, pos)) {
//                level.playSound(null, pos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, 0.3F);
//                return InteractionResult.PASS;
//            } else {
//                this.changeToActiveBlock(level, pos, state);
//                return InteractionResult.SUCCESS;
//            }
//        } else {
//            return InteractionResult.FAIL;
//        }
//    }
//
//    private void changeToActiveBlock(Level level, BlockPos pos, BlockState originState) {
//        if (originState.getBlock() instanceof CSCastleDoorBlock) {
//            level.setBlockAndUpdate(pos, originState.setValue(ACTIVE, true));
//        }
//        level.scheduleTick(pos, originState.getBlock(), 2 + level.getRandom().nextInt(5));
//    }
//
//    @Override
//    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
//        if (state.getValue(VANISHED)) {
//            if (state.getValue(ACTIVE)) {
//                level.setBlockAndUpdate(pos, state.setValue(VANISHED, false).setValue(ACTIVE, false));
//            } else {
//                this.changeToActiveBlock(level, pos, state);
//            }
//            this.playReappearSound(level, pos);
//        } else if (state.getValue(ACTIVE)) {
//            level.setBlockAndUpdate(pos, state.setValue(VANISHED, true).setValue(ACTIVE, false));
//            level.scheduleTick(pos, this, 80);
//            this.playVanishSound(level, pos);
//            this.vanishParticles(level, pos);
//
//            // 激活相邻的城堡门
//            for (Direction direction : Direction.values()) {
//                this.checkAndActivateCastleDoor(level, pos.relative(direction));
//            }
//        }
//    }
//
//    private void playVanishSound(Level level, BlockPos pos) {
//        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS,
//                       0.125F, level.getRandom().nextFloat() * 0.25F + 1.75F);
//    }
//
//    private void playReappearSound(Level level, BlockPos pos) {
//        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS,
//                       0.125F, level.getRandom().nextFloat() * 0.25F + 1.25F);
//    }
//
//    public void checkAndActivateCastleDoor(Level level, BlockPos pos) {
//        BlockState state = level.getBlockState(pos);
//        if (state.getBlock() instanceof CSCastleDoorBlock &&
//            !state.getValue(VANISHED) && !state.getValue(ACTIVE) &&
//            !isBlockLocked(level, pos)) {
//            this.changeToActiveBlock(level, pos, state);
//        }
//    }
//
//    private void vanishParticles(Level level, BlockPos pos) {
//        if (level instanceof ServerLevel serverLevel) {
//            RandomSource rand = level.getRandom();
//
//            // 生成消失粒子效果
//            for (int dx = 0; dx < 4; dx++) {
//                for (int dy = 0; dy < 4; dy++) {
//                    for (int dz = 0; dz < 4; dz++) {
//                        double x = pos.getX() + (dx + 0.5) / 4.0;
//                        double y = pos.getY() + (dy + 0.5) / 4.0;
//                        double z = pos.getZ() + (dz + 0.5) / 4.0;
//
//                        serverLevel.sendParticles(ParticleTypes.PORTAL,
//                                x, y, z, 1,
//                                rand.nextGaussian() * 0.2,
//                                rand.nextGaussian() * 0.2,
//                                rand.nextGaussian() * 0.2,
//                                0.1);
//                    }
//                }
//            }
//        }
//    }
//}