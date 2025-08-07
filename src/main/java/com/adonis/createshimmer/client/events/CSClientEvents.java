package com.adonis.createshimmer.client.events;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.config.CSConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
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
        if (minecraft.player != null && minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            float purpleStrength = 0.9f;
            event.setRed(event.getRed() * (1.0f - purpleStrength) + purpleStrength * 0.29f);
            event.setGreen(event.getGreen() * (1.0f - purpleStrength) + purpleStrength * 0.08f);
            event.setBlue(event.getBlue() * (1.0f - purpleStrength) + purpleStrength * 0.55f);
        }
    }

    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
                float originalStart = event.getNearPlaneDistance();
                float originalEnd = event.getFarPlaneDistance();
                event.setNearPlaneDistance(originalStart * 0.1f);
                event.setFarPlaneDistance(originalEnd * 0.1f);
                event.setCanceled(true);
            }
        }
    }

    // 【恢复】FOV修改效果 - 配置文件控制
    @SubscribeEvent
    public void onComputeFovModifier(ComputeFovModifierEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null &&
                minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                CSConfig.client().shimmerFovEnabled.get()) { // 配置文件控制开关

            float fovModifier = CSConfig.client().shimmerFovMultiplier.get().floatValue(); // 配置文件控制强度
            event.setNewFovModifier(event.getFovModifier() * fovModifier);
        }
    }

    // 【新增】渲染紫色屏幕覆盖层效果
    @SubscribeEvent
    public void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        // 在摄像机覆盖层渲染完成后渲染我们的屏幕效果
        if (event.getName().equals(VanillaGuiLayers.CAMERA_OVERLAYS)) {
            Minecraft minecraft = Minecraft.getInstance();

            // 检查玩家是否有微光效果且不是旁观者模式
            if (minecraft.player != null &&
                    minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT) &&
                    !minecraft.player.isSpectator()) {

                renderShimmerScreenOverlay(event.getGuiGraphics());
            }
        }
    }

    /**
     * 渲染微光屏幕覆盖层效果 - 简化版
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
        RenderSystem.defaultBlendFunc(); // 标准alpha混合

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
        float alpha = 0.45f; // 保持较强的遮蔽效果

        // 添加脉冲效果
        long time = System.currentTimeMillis();
        float pulse = (float) (Math.sin(time * 0.003) * 0.1 + 1.0); // 0.9-1.1之间脉冲
        alpha *= pulse;

        // 【简化】只绘制单层全屏覆盖，去掉突兀的中间区域
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
