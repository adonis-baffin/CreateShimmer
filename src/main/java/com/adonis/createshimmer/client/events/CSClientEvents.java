package com.adonis.createshimmer.client.events;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.config.CSConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class CSClientEvents {
    @SubscribeEvent
    public void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        Minecraft minecraft = Minecraft.getInstance();

        // 检查玩家或正在观察的实体是否有微光效果
        LivingEntity viewEntity = null;
        if (minecraft.getCameraEntity() instanceof LivingEntity living) {
            viewEntity = living;
        } else if (minecraft.player != null) {
            viewEntity = minecraft.player;
        }

        if (viewEntity != null && viewEntity.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            float purpleStrength = 0.9f;
            event.setRed(event.getRed() * (1.0f - purpleStrength) + purpleStrength * 0.29f);
            event.setGreen(event.getGreen() * (1.0f - purpleStrength) + purpleStrength * 0.08f);
            event.setBlue(event.getBlue() * (1.0f - purpleStrength) + purpleStrength * 0.55f);
        }
    }

    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();

        // 检查玩家或正在观察的实体是否有微光效果
        LivingEntity viewEntity = null;
        if (minecraft.getCameraEntity() instanceof LivingEntity living) {
            viewEntity = living;
        } else if (minecraft.player != null) {
            viewEntity = minecraft.player;
        }

        if (viewEntity != null && viewEntity.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
                float originalStart = event.getNearPlaneDistance();
                float originalEnd = event.getFarPlaneDistance();
                event.setNearPlaneDistance(originalStart * 0.1f);
                event.setFarPlaneDistance(originalEnd * 0.1f);
                event.setCanceled(true);
            }
        }
    }

    // FOV修改效果 - 配置文件控制
    @SubscribeEvent
    public void onComputeFovModifier(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null &&
                minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                CSConfig.client().shimmerFovEnabled.get()) {

            float fovModifier = CSConfig.client().shimmerFovMultiplier.get().floatValue();
            event.setNewFovModifier(event.getFovModifier() * fovModifier);
        }
    }

    // 渲染紫色屏幕覆盖层效果 - 仅对玩家第一人称视角
    @SubscribeEvent
    public void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        if (event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
            Minecraft minecraft = Minecraft.getInstance();

            // 只在第一人称视角下渲染覆盖层
            if (minecraft.player != null &&
                    minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                    !minecraft.player.isSpectator() &&
                    minecraft.options.getCameraType().isFirstPerson()) {

                renderShimmerScreenOverlay(event.getGuiGraphics());
            }
        }
    }

    /**
     * 渲染微光屏幕覆盖层效果
     */
    private void renderShimmerScreenOverlay(net.minecraft.client.gui.GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();

        // 获取屏幕尺寸
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // 设置渲染状态
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 设置着色器
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // 获取矩阵
        Matrix4f matrix = guiGraphics.pose().last().pose();

        // 创建渲染缓冲区
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        // 深紫色参数
        float red = 0.20f;
        float green = 0.05f;
        float blue = 0.40f;
        float alpha = 0.45f;

        // 添加脉冲效果
        long time = System.currentTimeMillis();
        float pulse = (float) (Math.sin(time * 0.003) * 0.1 + 1.0);
        alpha *= pulse;

        // 绘制全屏覆盖
        bufferbuilder.addVertex(matrix, 0, screenHeight, 0).setColor(red, green, blue, alpha);
        bufferbuilder.addVertex(matrix, screenWidth, screenHeight, 0).setColor(red, green, blue, alpha);
        bufferbuilder.addVertex(matrix, screenWidth, 0, 0).setColor(red, green, blue, alpha);
        bufferbuilder.addVertex(matrix, 0, 0, 0).setColor(red, green, blue, alpha);

        // 提交渲染
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

        // 恢复渲染状态
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
