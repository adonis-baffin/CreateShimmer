package com.adonis.createshimmer.network;

import com.adonis.createshimmer.client.CSShimmerClientHandler;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShimmerParticleLinePacket(UUID targetUuid, long startTime) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("create_shimmer", "shimmer_line");
    public static final Type<ShimmerParticleLinePacket> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, ShimmerParticleLinePacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ShimmerParticleLinePacket::targetUuid,
            ByteBufCodecs.VAR_LONG, ShimmerParticleLinePacket::startTime,
            ShimmerParticleLinePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShimmerParticleLinePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                CSShimmerClientHandler.addParticleLineTarget(packet.targetUuid, packet.startTime);
            }
        });
    }
}
