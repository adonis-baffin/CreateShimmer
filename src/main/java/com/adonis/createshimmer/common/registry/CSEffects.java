package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.effects.ShimmerEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CSEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, CSCommon.ID);

    private static final Holder<Attribute> MOVEMENT_SPEED_ATTRIBUTE = getAttributeHolder("generic.movement_speed");
    private static final Holder<Attribute> ATTACK_SPEED_ATTRIBUTE = getAttributeHolder("generic.attack_speed");
    private static final Holder<Attribute> ATTACK_DAMAGE_ATTRIBUTE = getAttributeHolder("generic.attack_damage");
    private static final Holder<Attribute> BLOCK_BREAK_SPEED_ATTRIBUTE = getAttributeHolder("generic.attack_damage");

    public static final DeferredHolder<MobEffect, MobEffect> SHIMMER_EFFECT = EFFECTS.register("shimmer", () -> new ShimmerEffect()
//            .addAttributeModifier(
//                    MOVEMENT_SPEED_ATTRIBUTE,
//                    CSCommon.asResource("effect.shimmer_movement_speed"),
//                    ShimmerEffect.MOVEMENT_SPEED_MODIFIER,
//                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(
                    ATTACK_SPEED_ATTRIBUTE,
                    CSCommon.asResource("effect.shimmer_attack_speed"),
                    ShimmerEffect.ATTACK_SPEED_MODIFIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(
                    ATTACK_DAMAGE_ATTRIBUTE,
                    CSCommon.asResource("effect.shimmer_attack_damage"),
                    ShimmerEffect.ATTACK_DAMAGE_MODIFIER,
                    AttributeModifier.Operation.ADD_VALUE))
//            .addAttributeModifier(
//                    BLOCK_BREAK_SPEED_ATTRIBUTE,
//                    CSCommon.asResource("effect.shimmer_attack_damage"),
//                    ShimmerEffect.BLOCK_BREAK_SPEED_MODIFIER,
//                    AttributeModifier.Operation.ADD_VALUE))
    ;

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }

    private static Holder<Attribute> getAttributeHolder(String id) {
        ResourceLocation location = ResourceLocation.withDefaultNamespace(id);
        return BuiltInRegistries.ATTRIBUTE.getHolder(location)
                .orElseThrow(() -> new IllegalStateException("FATAL: Could not find vanilla attribute: " + location));
    }
}
