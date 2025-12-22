package com.adonis.createshimmer.client;

import com.adonis.createshimmer.common.registry.CSEffects;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class CSShimmerClientHandler {
    private static final DustParticleOptions PURPLE_DUST = new DustParticleOptions(
            new Vector3f(154f / 255f, 77f / 255f, 255f / 255f), 1.0f);

    private static final Map<UUID, Long> PARTICLE_LINE_TARGETS = new LinkedHashMap<>();
    private static final long MAX_DURATION = 8000L; // 8秒

    public static void addParticleLineTarget(UUID uuid, long startTime) {
        PARTICLE_LINE_TARGETS.put(uuid, startTime);
    }

    @SubscribeEvent
    public static void onClientLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide()) {
            tickParticleLines();
        }
    }

    private static void tickParticleLines() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (!mc.player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            PARTICLE_LINE_TARGETS.clear();
            return;
        }

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Long>> iterator = PARTICLE_LINE_TARGETS.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            UUID uuid = entry.getKey();
            long startTime = entry.getValue();

            if (currentTime - startTime > MAX_DURATION) {
                iterator.remove();
                continue;
            }

            // 遍历当前渲染的实体列表查找目标
            LivingEntity target = null;
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity instanceof LivingEntity living && living.getUUID().equals(uuid) && living.isAlive()) {
                    target = living;
                    break;
                }
            }

            if (target == null) {
                iterator.remove();
                continue;
            }

            // ========== 修改：使用脚部位置，避免挡视线 ==========
            Vec3 playerFeetPos = new Vec3(mc.player.getX(), mc.player.getY(), mc.player.getZ());
            Vec3 targetFeetPos = new Vec3(target.getX(), target.getY(), target.getZ());

            var direction = new Vec3(playerFeetPos.x - targetFeetPos.x,
                    playerFeetPos.y - targetFeetPos.y,
                    playerFeetPos.z - targetFeetPos.z);
            double distance = direction.length();

            if (distance > 64 || distance < 0.5) {
                iterator.remove();
                continue;
            }

            direction = direction.normalize();

            int particleCount = 8 + (int) (distance / 4);
            for (int i = 0; i < particleCount; i++) {
                double progress = (double) i / particleCount;
                double curve = Math.pow(progress, 0.7);

                // 基础位置：从目标脚部到玩家脚部
                double baseX = targetFeetPos.x + direction.x * distance * curve;
                double baseY = targetFeetPos.y + direction.y * distance * curve;
                double baseZ = targetFeetPos.z + direction.z * distance * curve;

                // ========== 关键：Y轴向上随机偏移 0.2 ~ 0.8，避免贴地 + 挡视线 ==========
                double yOffset = 0.2 + Math.sin(progress * Math.PI) * 0.6;  // 曲线向上飘浮
                double xOffset = (mc.level.random.nextDouble() - 0.5) * 0.3;  // 轻微左右抖动
                double zOffset = (mc.level.random.nextDouble() - 0.5) * 0.3;

                double x = baseX + xOffset;
                double y = baseY + yOffset;
                double z = baseZ + zOffset;

                // 速度：朝向玩家，带向上飘浮 + 加速度
                double speed = 0.03 + progress * 0.08;
                double vx = direction.x * speed + (mc.level.random.nextDouble() - 0.5) * 0.02;
                double vy = direction.y * speed + 0.02 + (mc.level.random.nextDouble() - 0.5) * 0.01;  // 向上漂浮
                double vz = direction.z * speed + (mc.level.random.nextDouble() - 0.5) * 0.02;

                mc.level.addParticle(PURPLE_DUST,
                        x, y, z,
                        vx, vy, vz);
            }
        }
    }
}
