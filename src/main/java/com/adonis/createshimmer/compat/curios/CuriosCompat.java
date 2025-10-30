package com.adonis.createshimmer.compat.curios;

import com.adonis.createshimmer.client.model.CSModelLayers;
import com.adonis.createshimmer.common.registry.CSItems;
import com.adonis.createshimmer.compat.curios.model.ShimmerCharmNecklaceModel;
import com.adonis.createshimmer.compat.curios.renderer.ShimmerCharmNecklaceRenderer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosCompat {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
            public ItemStack getStack() {
                return stack;
            }

            @Nonnull
            public ICurio.SoundInfo getEquipSound(SlotContext slotContext) {
                return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
            }

            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }
        }, CSItems.CHARM_OF_SHIMMER_1.get(), CSItems.CHARM_OF_SHIMMER_2.get());
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CSModelLayers.CHARM_OF_SHIMMER, ShimmerCharmNecklaceModel::create);
    }

    public static void registerRenderers(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(CSItems.CHARM_OF_SHIMMER_1.get(), () -> new ShimmerCharmNecklaceRenderer(FastColor.ARGB32.colorFromFloat(0.65F, 0.50F, 0.30F, 0.70F)));  // 稍深紫色 for I
            CuriosRendererRegistry.register(CSItems.CHARM_OF_SHIMMER_2.get(), () -> new ShimmerCharmNecklaceRenderer(FastColor.ARGB32.colorFromFloat(0.65F, 0.50F, 0.30F, 0.70F)));  // 稍深紫色 for II（或根据需要调整）
        });
    }

    // 可选：实用方法，检查是否装备
    public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
        return top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(entity)
                .flatMap(handler -> handler.findFirstCurio(stackPredicate))
                .isPresent();
    }
}
