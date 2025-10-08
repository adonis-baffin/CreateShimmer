package com.adonis.createshimmer.client.gui;

import com.adonis.createshimmer.common.registry.CSEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

    // 使用原版相同的随机数生成器
    private final RandomSource random = RandomSource.create();

    // 血量跟踪变量（与原版保持同步）
    private int lastHealth = 0;
    private int displayHealth = 0;
    private long lastHealthTime = 0;
    private long healthBlinkTime = 0;

    @SubscribeEvent
    public void onRenderHealthLayer(RenderGuiLayerEvent.Pre event) {
        // 在渲染原版血条之前拦截
        if (event.getName() != VanillaGuiLayers.PLAYER_HEALTH) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || !player.hasEffect(CSEffects.SHIMMER_EFFECT)) {  // 直接使用 DeferredHolder
            return;
        }

        // 检查是否应该渲染血条（创造模式和观察者模式不渲染）
        if (!shouldRenderHealth(minecraft)) {
            // 如果原版不渲染血条，我们也不应该渲染微光血条
            return;
        }

        // 取消原版血条渲染
        event.setCanceled(true);

        // 渲染微光血条
        renderShimmerHealth(event.getGuiGraphics(), minecraft, player);
    }

    /**
     * 检查是否应该渲染血条（与原版逻辑保持一致）
     */
    private boolean shouldRenderHealth(Minecraft minecraft) {
        // 检查玩家是否存在
        if (minecraft.player == null) {
            return false;
        }

        // 检查是否隐藏GUI
        if (minecraft.options.hideGui) {
            return false;
        }

        // 检查游戏模式
        // 创造模式和观察者模式不显示血条
        if (minecraft.gameMode != null) {
            var gameType = minecraft.gameMode.getPlayerMode();
            if (gameType.isCreative() || gameType == net.minecraft.world.level.GameType.SPECTATOR) {
                return false;
            }
        }

        return true;
    }

    private void renderShimmerHealth(GuiGraphics guiGraphics, Minecraft minecraft, Player player) {
        // 获取原版GUI的tick计数器
        Gui gui = minecraft.gui;
        int tickCount = gui.getGuiTicks();

        // 获取玩家血量数据
        int currentHealth = Mth.ceil(player.getHealth());
        float maxHealth = Math.max((float) player.getAttributeValue(Attributes.MAX_HEALTH),
                Math.max(displayHealth, currentHealth));
        int absorptionAmount = Mth.ceil(player.getAbsorptionAmount());

        // 更新闪烁时间（完全复制原版逻辑）
        boolean shouldBlink = healthBlinkTime > (long) tickCount &&
                (healthBlinkTime - (long) tickCount) / 3L % 2L == 1L;

        long currentTime = System.currentTimeMillis(); // 使用System.currentTimeMillis()替代Util.getMillis()
        if (currentHealth < lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = currentTime;
            healthBlinkTime = (long) (tickCount + 20);
        } else if (currentHealth > lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = currentTime;
            healthBlinkTime = (long) (tickCount + 10);
        }

        if (currentTime - lastHealthTime > 1000L) {
            lastHealth = currentHealth;
            displayHealth = currentHealth;
            lastHealthTime = currentTime;
        }

        lastHealth = currentHealth;

        // 设置随机数种子（与原版完全相同）
        random.setSeed((long) (tickCount * 312871));

        // 计算基础位置
        int left = guiGraphics.guiWidth() / 2 - 91;
        int top = guiGraphics.guiHeight() - 39;

        // 计算心的行数和间距
        int totalHearts = Mth.ceil((maxHealth + (float) absorptionAmount) / 2.0F);
        int heartRows = Mth.ceil(totalHearts / 10.0F);
        int rowHeight = Math.max(10 - (heartRows - 2), 3);

        // 获取regeneration效果的偏移心索引
        int regenOffset = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regenOffset = tickCount % Mth.ceil(maxHealth + 5.0F);
        }

        // 是否是极限模式
        boolean isHardcore = player.level().getLevelData().isHardcore();

        // 启用混合
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 渲染所有心（从最后一个开始，这样容器在底层）
        for (int heartIndex = totalHearts - 1; heartIndex >= 0; heartIndex--) {
            int row = heartIndex / 10;
            int col = heartIndex % 10;
            int heartX = left + col * 8;
            int heartY = top - row * rowHeight;

            // 低血量跳动效果（完全复制原版逻辑）
            if (currentHealth + absorptionAmount <= 4) {
                heartY += random.nextInt(2);
            }

            // regeneration效果偏移
            if (heartIndex == regenOffset) {
                heartY -= 2;
            }

            // 选择容器贴图
            ResourceLocation containerTexture;
            if (isHardcore) {
                containerTexture = shouldBlink ? SHIMMER_CONTAINER_HARDCORE_BLINKING : SHIMMER_CONTAINER_HARDCORE;
            } else {
                containerTexture = shouldBlink ? SHIMMER_CONTAINER_BLINKING : SHIMMER_CONTAINER;
            }

            // 渲染容器
            guiGraphics.blit(containerTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);

            // 计算心的值
            int heartValue = heartIndex * 2;
            boolean isAbsorption = heartIndex >= Mth.ceil(maxHealth / 2.0F);

            // 渲染高亮心（闪烁效果）
            if (shouldBlink && heartValue < displayHealth && !isAbsorption) {
                boolean isHalf = heartValue + 1 == displayHealth;
                ResourceLocation blinkTexture = getHeartTexture(isHardcore, true, isHalf);
                guiGraphics.blit(blinkTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);
            }

            // 渲染实际的心
            if (heartValue < currentHealth && !isAbsorption) {
                boolean isHalf = heartValue + 1 == currentHealth;
                ResourceLocation heartTexture = getHeartTexture(isHardcore, false, isHalf);
                guiGraphics.blit(heartTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);
            }

            // 渲染吸收心
            if (isAbsorption) {
                int absorbValue = heartValue - Mth.ceil(maxHealth);
                if (absorbValue < absorptionAmount * 2) {
                    boolean isHalf = absorbValue + 1 == absorptionAmount * 2;
                    // 吸收心使用相同的微光贴图（你可以创建专门的吸收心贴图）
                    ResourceLocation absorbTexture = getHeartTexture(isHardcore, false, isHalf);
                    guiGraphics.blit(absorbTexture, heartX, heartY, 0, 0, 9, 9, 9, 9);
                }
            }
        }

        // 禁用混合
        RenderSystem.disableBlend();
    }

    private ResourceLocation getHeartTexture(boolean hardcore, boolean blinking, boolean half) {
        if (hardcore) {
            if (half) {
                return blinking ? SHIMMER_HARDCORE_HALF_BLINKING : SHIMMER_HARDCORE_HALF;
            } else {
                return blinking ? SHIMMER_HARDCORE_FULL_BLINKING : SHIMMER_HARDCORE_FULL;
            }
        } else {
            if (half) {
                return blinking ? SHIMMER_HALF_BLINKING : SHIMMER_HALF;
            } else {
                return blinking ? SHIMMER_FULL_BLINKING : SHIMMER_FULL;
            }
        }
    }
}