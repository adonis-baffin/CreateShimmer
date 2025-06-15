

package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.fluids.experience.ExperienceEffectHandler;
import com.adonis.createshimmer.common.fluids.experience.ExperienceFluidType;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSFluids {
    public static final FluidEntry<BaseFlowingFluid.Source> EXPERIENCE = new FluidEntry<>(REGISTRATE,
            DeferredHolder.create(Registries.FLUID, REGISTRATE.asResource("experience")));

    public static final FluidEntry<BaseFlowingFluid.Flowing> EXPERIENCE_FLOWING = REGISTRATE
            .fluid("experience", ExperienceFluidType.create())
            .lang("Liquid Experience")
            .properties(builder -> builder
                    .rarity(Rarity.UNCOMMON)
                    .lightLevel(15)
                    .fallDistanceModifier(0f)
                    .canPushEntity(false)
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.BLOCKED)
                    .adjacentPathType(PathType.BLOCKED))
            .fluidProperties(p -> p.explosionResistance(100f))
            .tag(AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .source(BaseFlowingFluid.Source::new)
            .block()
            .lang("Liquid Experience")
            .build()
            .bucket()
            .lang("Bucket o' Enchanting")
            .properties(properties -> properties
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .tag(Tags.Items.BUCKETS)
            .build()
            .register();

    public static void register(IEventBus modBus) {
        modBus.register(CSFluids.class);
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OpenPipeEffectHandler.REGISTRY.register(EXPERIENCE.get(), new ExperienceEffectHandler());
        });
    }
}
