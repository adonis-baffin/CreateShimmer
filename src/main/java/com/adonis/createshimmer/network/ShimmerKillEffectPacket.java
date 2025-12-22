package com.adonis.createshimmer.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector3f;

public record ShimmerKillEffectPacket(Vec3 pos) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("create_shimmer", "shimmer_kill");
    public static final Type<ShimmerKillEffectPacket> TYPE = new Type<>(ID);

    private static final DustParticleOptions PURPLE_DUST = new DustParticleOptions(
            new Vector3f(154f / 255f, 77f / 255f, 255f / 255f), 1.2f);

    // 手动编解码 Vec3
    public static final StreamCodec<FriendlyByteBuf, ShimmerKillEffectPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, p -> p.pos.x,
            ByteBufCodecs.DOUBLE, p -> p.pos.y,
            ByteBufCodecs.DOUBLE, p -> p.pos.z,
            (x, y, z) -> new ShimmerKillEffectPacket(new Vec3(x, y, z)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShimmerKillEffectPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var mc = Minecraft.getInstance();
            if (mc.level == null) return;

            // 紫色粒子爆发
            for (int i = 0; i < 60; i++) {
                double ox = mc.level.random.nextGaussian() * 0.6;
                double oy = mc.level.random.nextGaussian() * 0.6 + 0.5;
                double oz = mc.level.random.nextGaussian() * 0.6;
                mc.level.addParticle(PURPLE_DUST,
                        packet.pos.x + ox,
                        packet.pos.y + oy,
                        packet.pos.z + oz,
                        mc.level.random.nextGaussian() * 0.1,
                        0.15 + mc.level.random.nextFloat() * 0.1,
                        mc.level.random.nextGaussian() * 0.1);
            }

            mc.level.addParticle(ParticleTypes.DRAGON_BREATH, packet.pos.x, packet.pos.y + 0.5, packet.pos.z, 0, 0.1, 0);
            for (int i = 0; i < 20; i++) {
                mc.level.addParticle(ParticleTypes.WITCH,
                        packet.pos.x + mc.level.random.nextGaussian() * 0.5,
                        packet.pos.y + mc.level.random.nextFloat() * 1.0,
                        packet.pos.z + mc.level.random.nextGaussian() * 0.5,
                        mc.level.random.nextGaussian() * 0.05,
                        0.1,
                        mc.level.random.nextGaussian() * 0.05);
            }

            mc.level.playLocalSound(packet.pos.x, packet.pos.y, packet.pos.z,
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.7f, 1.3f + mc.level.random.nextFloat() * 0.2f, false);
            mc.level.playLocalSound(packet.pos.x, packet.pos.y, packet.pos.z,
                    SoundEvents.THORNS_HIT, SoundSource.PLAYERS, 1.0f, 1.5f, false);
        });
    }
}
