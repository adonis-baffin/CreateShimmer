package com.adonis.createshimmer.client.gui;

import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@OnlyIn(Dist.CLIENT)
public class ShimmerGui {
    private static final ResourceLocation SHIMMER_CONTAINER = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/container.png");
    private static final ResourceLocation SHIMMER_CONTAINER_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/container_blinking.png");
    private static final ResourceLocation SHIMMER_CONTAINER_HARDCORE = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/container_hardcore.png");
    private static final ResourceLocation SHIMMER_CONTAINER_HARDCORE_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/container_hardcore_blinking.png");
    private static final ResourceLocation SHIMMER_FULL = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_full.png");
    private static final ResourceLocation SHIMMER_FULL_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_full_blinking.png");
    private static final ResourceLocation SHIMMER_HALF = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_half.png");
    private static final ResourceLocation SHIMMER_HALF_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_half_blinking.png");
    private static final ResourceLocation SHIMMER_HARDCORE_FULL = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_hardcore_full.png");
    private static final ResourceLocation SHIMMER_HARDCORE_FULL_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_hardcore_full_blinking.png");
    private static final ResourceLocation SHIMMER_HARDCORE_HALF = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_hardcore_half.png");
    private static final ResourceLocation SHIMMER_HARDCORE_HALF_BLINKING = ResourceLocation.fromNamespaceAndPath("create_shimmer", "textures/gui/icons/shimmer_hardcore_half_blinking.png");

    @SubscribeEvent
    public void onRenderPlayerHealth(RenderGuiLayerEvent.Pre event) {
        if (event.getName().equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            // 获取玩家的游戏模式
            // 使用 minecraft.gameMode 来获取 ClientGameMode
            if (player != null && minecraft.gameMode != null &&
            // 确保玩家有效果 并且 不是创造模式或观察者模式
                    player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                    !player.isCreative() && !player.isSpectator()) {

                event.setCanceled(true);
                this.renderShimmerHealth(event.getGuiGraphics(), minecraft);
            }
        }
    }

    private void renderShimmerHealth(GuiGraphics guiGraphics, Minecraft minecraft) { // 移除了 static
        Player player = minecraft.player;
        if (player == null) return;

        int health = Mth.ceil(player.getHealth());
        int maxHealth = Mth.ceil(player.getMaxHealth());

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 91;
        int y = screenHeight - 39;

        boolean hardcore = minecraft.level != null && minecraft.level.getLevelData().isHardcore();

        boolean blinking = player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                (player.getHealth() <= 4.0F || (player.getHealth() <= maxHealth * 0.25F));

        for (int i = 0; i < Math.ceil(maxHealth / 2.0); i++) {
            int heartX = x + i % 10 * 8;
            int heartY = y - i / 10 * 10;
            ResourceLocation containerTexture;
            if (hardcore) {
                containerTexture = blinking ? SHIMMER_CONTAINER_HARDCORE_BLINKING : SHIMMER_CONTAINER_HARDCORE;
            } else {
                containerTexture = blinking ? SHIMMER_CONTAINER_BLINKING : SHIMMER_CONTAINER;
            }
            guiGraphics.blit(containerTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);
            ResourceLocation heartTexture = null;
            if (i * 2 + 1 < health) {
                if (hardcore) {
                    heartTexture = blinking ? SHIMMER_HARDCORE_FULL_BLINKING : SHIMMER_HARDCORE_FULL;
                } else {
                    heartTexture = blinking ? SHIMMER_FULL_BLINKING : SHIMMER_FULL;
                }
            } else if (i * 2 + 1 == health) {
                if (hardcore) {
                    heartTexture = blinking ? SHIMMER_HARDCORE_HALF_BLINKING : SHIMMER_HARDCORE_HALF;
                } else {
                    heartTexture = blinking ? SHIMMER_HALF_BLINKING : SHIMMER_HALF;
                }
            }
            if (heartTexture != null) {
                guiGraphics.blit(heartTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);
            }
        }
    }
}
