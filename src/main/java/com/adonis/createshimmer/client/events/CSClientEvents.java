package com.adonis.createshimmer.client.events;

import com.adonis.createshimmer.common.registry.CSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@OnlyIn(Dist.CLIENT)
public class CSClientEvents {
    @SubscribeEvent
    public void onComputeFogColor(ViewportEvent.ComputeFogColor event) { // 移除了 static
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            float purpleStrength = 0.9f;
            event.setRed(event.getRed() * (1.0f - purpleStrength) + purpleStrength * 0.29f);
            event.setGreen(event.getGreen() * (1.0f - purpleStrength) + purpleStrength * 0.08f);
            event.setBlue(event.getBlue() * (1.0f - purpleStrength) + purpleStrength * 0.55f);
        }
    }

    @SubscribeEvent
    public void onRenderFog(ViewportEvent.RenderFog event) { // 移除了 static
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

    @SubscribeEvent
    public void onComputeFovModifier(ComputeFovModifierEvent event) { // 移除了 static
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.hasEffect(CSEffects.SHIMMER_EFFECT)) {
            event.setNewFovModifier(event.getFovModifier() * 0.6f);
        }
    }
}
