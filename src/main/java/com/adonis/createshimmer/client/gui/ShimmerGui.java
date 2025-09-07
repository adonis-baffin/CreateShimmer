package com.adonis.createshimmer.client.gui;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

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

    // 原版心形纹理UV坐标（用于覆盖）
    private static final ResourceLocation GUI_ICONS = ResourceLocation.withDefaultNamespace("textures/gui/icons.png");

    // 使用RenderGuiEvent.Post，它在所有GUI层渲染完成后触发
    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || minecraft.gameMode == null) {
            return;
        }

        // 检查是否应该渲染
        if (!shouldRenderShimmerHealth(player)) {
            return;
        }

        // 在所有GUI渲染完成后，覆盖原版血条
        renderShimmerHealthOverlay(event.getGuiGraphics(), minecraft, player);
    }

    private boolean shouldRenderShimmerHealth(Player player) {
        // 检查玩家是否有微光效果
        if (!player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            return false;
        }

        // 创造和观察者模式不显示血条
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }

        return true;
    }

    private void renderShimmerHealthOverlay(GuiGraphics guiGraphics, Minecraft minecraft, Player player) {
        int health = Mth.ceil(player.getHealth());
        int maxHealth = Mth.ceil(player.getMaxHealth());

        if (maxHealth <= 0) {
            return;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // 计算血条位置（标准GUI位置）
        int left = screenWidth / 2 - 91;
        int top = screenHeight - 39;

        // 如果GUI缩放或偏移，需要考虑
        // 注意：在1.21.1中，血条位置是固定的

        boolean hardcore = minecraft.level != null && minecraft.level.getLevelData().isHardcore();
        boolean blinking = player.getHealth() <= 4.0F || (player.getHealth() <= maxHealth * 0.25F);

        // 保存当前渲染状态
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // 绘制自定义血条
        renderHealthBar(guiGraphics, left, top, health, maxHealth, hardcore, blinking);

        // 恢复渲染状态
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void renderHealthBar(GuiGraphics guiGraphics, int x, int y, int health, int maxHealth, boolean hardcore, boolean blinking) {
        // 计算需要渲染的心形数量
        int heartsPerRow = 10;
        int totalHearts = Mth.ceil(maxHealth / 2.0F);
        int rows = Mth.ceil(totalHearts / (float) heartsPerRow);

        // 从底部开始渲染每一行
        for (int row = rows - 1; row >= 0; row--) {
            int rowY = y - row * 10; // 每行间隔10像素

            for (int col = 0; col < heartsPerRow; col++) {
                int heartIndex = row * heartsPerRow + col;

                // 超过最大血量就不渲染了
                if (heartIndex >= totalHearts) {
                    break;
                }

                int heartX = x + col * 8; // 每个心形间隔8像素

                // 先清除原版心形区域（用透明黑色覆盖）
                // 注意：这可能会影响透明度，如果有问题可以注释掉
                // guiGraphics.fill(heartX, rowY, heartX + 9, rowY + 9, 0xFF000000);

                // 渲染容器
                ResourceLocation containerTexture;
                if (hardcore) {
                    containerTexture = blinking ? SHIMMER_CONTAINER_HARDCORE_BLINKING : SHIMMER_CONTAINER_HARDCORE;
                } else {
                    containerTexture = blinking ? SHIMMER_CONTAINER_BLINKING : SHIMMER_CONTAINER;
                }

                // 绘制容器（背景）
                guiGraphics.blit(containerTexture, heartX, rowY, 0, 0, 9, 9, 9, 9);

                // 计算当前心形应该显示的状态
                int heartHealth = heartIndex * 2;

                if (heartHealth < health) {
                    ResourceLocation heartTexture;

                    if (heartHealth + 1 == health) {
                        // 半心
                        if (hardcore) {
                            heartTexture = blinking ? SHIMMER_HARDCORE_HALF_BLINKING : SHIMMER_HARDCORE_HALF;
                        } else {
                            heartTexture = blinking ? SHIMMER_HALF_BLINKING : SHIMMER_HALF;
                        }
                    } else {
                        // 满心
                        if (hardcore) {
                            heartTexture = blinking ? SHIMMER_HARDCORE_FULL_BLINKING : SHIMMER_HARDCORE_FULL;
                        } else {
                            heartTexture = blinking ? SHIMMER_FULL_BLINKING : SHIMMER_FULL;
                        }
                    }

                    // 绘制心形
                    guiGraphics.blit(heartTexture, heartX, rowY, 0, 0, 9, 9, 9, 9);
                }
            }
        }
    }
}
