package com.adonis.createshimmer.common.events;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSEffects;
import com.adonis.createshimmer.common.registry.CSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.compat.curios.CuriosCompat;
import twilightforest.init.TFSounds;
import twilightforest.network.SpawnCharmPacket;
import twilightforest.util.TFItemStackUtils;

@EventBusSubscriber(modid = CSCommon.ID)
public class CSCharmEvents {
    /**
     * 监听玩家死亡事件，并尝试使用微光符咒来拯救玩家。
     * 优先级设置为 HIGHEST，确保我们的符咒效果在其他死亡处理逻辑（如暮色森林的守护符咒）之前执行。
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // 检查死亡的实体是否是玩家，并且处于可以被符咒拯救的状态
        if (!event.isCanceled() && !entity.level().isClientSide() && entity instanceof Player player) {
            if (!(player instanceof FakePlayer) && !player.isCreative() && !player.isSpectator()) {
                // 调用核心逻辑，如果成功消耗了符咒，则取消死亡事件
                if (applyShimmerCharm(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * 核心逻辑：检查并消耗玩家身上的微光符咒，然后应用效果。
     * 
     * @param player 目标玩家
     * @return 如果成功消耗符咒并应用了效果，则返回 true
     */
    private static boolean applyShimmerCharm(Player player) {
        // 优先检查并消耗更高级的符咒 (Charm of Shimmer II)
        // 我们直接复用暮色森林的 TFItemStackUtils.consumeInventoryItem 工具，它能完美处理从玩家物品栏消耗物品的逻辑。
        // 同时，我们也复用 hasCharmCurio 来兼容饰品栏（Curios）。
        boolean consumedCharm2 = TFItemStackUtils.consumeInventoryItem(player, CSItems.CHARM_OF_SHIMMER_2.get(), getPlayerData(player), false)
                || hasCharmCurio(CSItems.CHARM_OF_SHIMMER_2.get(), player);

        // 如果没有消耗二级符咒，再检查并消耗一级符咒 (Charm of Shimmer I)
        boolean consumedCharm1 = !consumedCharm2 &&
                (TFItemStackUtils.consumeInventoryItem(player, CSItems.CHARM_OF_SHIMMER_1.get(), getPlayerData(player), false)
                        || hasCharmCurio(CSItems.CHARM_OF_SHIMMER_1.get(), player));

        // 如果两种符咒都没有，直接返回 false
        if (!consumedCharm1 && !consumedCharm2) {
            return false;
        }

        // --- 如果消耗成功，在这里应用效果 ---

        ItemStack consumedStack; // 用于决定显示哪个符咒的粒子效果

        if (consumedCharm1) {
            consumedStack = new ItemStack(CSItems.CHARM_OF_SHIMMER_1.get());
            // 一级符咒效果:
            player.setHealth(10.0F); // 恢复 5 颗心
            player.addEffect(new MobEffectInstance(CSEffects.SHIMMER_EFFECT, 200, 0)); // 给予 10 秒的 微光I 效果
        } else { // 必然是 consumedCharm2
            consumedStack = new ItemStack(CSItems.CHARM_OF_SHIMMER_2.get());
            // 二级符咒效果:
            player.setHealth(player.getMaxHealth()); // 完全恢复生命值
            player.addEffect(new MobEffectInstance(CSEffects.SHIMMER_EFFECT, 600, 0)); // 给予 30 秒的 微光II 效果
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1)); // 给予 5 秒的 再生II
        }

        // 如果是服务器端，向客户端发送数据包以播放声音和粒子效果
        if (player instanceof ServerPlayer serverPlayer) {
            // 【关键】我们直接复用暮色森林的 SpawnCharmPacket 和 TFSounds
            // 这会在玩家身上产生和生命符咒一样的华丽视觉和声音效果
            PacketDistributor.sendToPlayer(serverPlayer, new SpawnCharmPacket(consumedStack, TFSounds.CHARM_LIFE.getKey()));
        }

        // 告诉调用者，我们成功了
        return true;
    }

    /**
     * 辅助方法：从暮色森林的 CharmEvents 类中直接复制而来。
     * 用于获取或创建玩家的持久化NBT数据，这是 TFItemStackUtils 消耗物品时所需要的。
     */
    public static CompoundTag getPlayerData(Player player) {
        if (!player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
            player.getPersistentData().put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
    }

    /**
     * 辅助方法：从暮色森林的 CharmEvents 类中直接复制而来。
     * 用于检查并消耗饰品栏（Curios）中的符咒。
     */
    private static boolean hasCharmCurio(Item item, Player player) {
        return ModList.get().isLoaded("curios") && CuriosCompat.findAndConsumeCurio(item, player);
    }
}
