package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.fluids.carminite.CarminiteFluidType;
import com.adonis.createshimmer.common.fluids.carminite.CarminiteLiquidBlock;
import com.adonis.createshimmer.common.fluids.carminite.CarminiteOpenPipeEffect;
import com.adonis.createshimmer.common.fluids.fierytear.FieryTearFluidType;
import com.adonis.createshimmer.common.fluids.fierytear.FieryTearLiquidBlock;
import com.adonis.createshimmer.common.fluids.fierytear.FieryTearOpenPipeEffect;
import com.adonis.createshimmer.common.fluids.shimmer.ShimmerFluidType;
import com.adonis.createshimmer.common.fluids.shimmer.ShimmerLiquidBlock;
import com.adonis.createshimmer.common.fluids.shimmer.ShimmerOpenPipeEffect;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class CSFluids {
    public static final FluidEntry<BaseFlowingFluid.Flowing> SHIMMER = REGISTRATE
            .fluid("shimmer",
                    REGISTRATE.asResource("fluid/shimmer_still"),
                    REGISTRATE.asResource("fluid/shimmer_flow"),
                    ShimmerFluidType.create())
            .lang("Shimmer")
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
                    .density(3000)  // Same as dragon breath
                    .viscosity(6000)  // Same as dragon breath
                    .lightLevel(15)  // Same as dragon breath
                    .motionScale(0.07)  // Same as dragon breath
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.WATER)  // Less dangerous than DAMAGE_OTHER
                    .adjacentPathType(null)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.AMETHYST_BLOCK_CHIME)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)  // Same as dragon breath
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA))  // Same as dragon breath
            .fluidProperties(properties -> properties
                    .explosionResistance(100F)
                    .levelDecreasePerBlock(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .slopeFindDistance(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .tickRate(30))  // Same as dragon breath
            .source(BaseFlowingFluid.Source::new)
            .tag(AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .block(ShimmerLiquidBlock::new)
            .lang("Shimmer")
            .build()
            .bucket()
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
            .lang("Shimmer Bucket")
            .tag(Tags.Items.BUCKETS)
            .build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> CARMINITE_SOLUTION = REGISTRATE
            .fluid("carminite_solution",
                    REGISTRATE.asResource("fluid/carminite_solution_still"),
                    REGISTRATE.asResource("fluid/carminite_solution_flow"),
                    CarminiteFluidType.create())
            .lang("Carminite Solution")
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
                    .density(3000)  // Same as dragon breath
                    .viscosity(6000)  // Same as dragon breath
                    .lightLevel(15)  // Same as dragon breath
                    .motionScale(0.07)  // Same as dragon breath
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.WATER)  // Less dangerous than DAMAGE_OTHER
                    .adjacentPathType(null)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.AMETHYST_BLOCK_CHIME)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)  // Same as dragon breath
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA))  // Same as dragon breath
            .fluidProperties(properties -> properties
                    .explosionResistance(100F)
                    .levelDecreasePerBlock(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .slopeFindDistance(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .tickRate(30))  // Same as dragon breath
            .source(BaseFlowingFluid.Source::new)
            .tag(AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .block(CarminiteLiquidBlock::new)
            .lang("Carminite Solution")
            .build()
            .bucket()
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
//                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            )
            .lang("Carminite Solution Bucket")
            .tag(Tags.Items.BUCKETS)
            .build()
            .register();

    public static final FluidEntry<BaseFlowingFluid.Flowing> FIERY_TEAR = REGISTRATE
            .fluid("fiery_tear",
                    REGISTRATE.asResource("fluid/fiery_tear_still"),
                    REGISTRATE.asResource("fluid/fiery_tear_flow"),
                    FieryTearFluidType.create())
            .lang("Fiery Tear")
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
                    .density(3000)  // Same as dragon breath
                    .viscosity(6000)  // Same as dragon breath
                    .lightLevel(15)  // Same as dragon breath
                    .motionScale(0.07)  // Same as dragon breath
                    .canSwim(false)
                    .canDrown(false)
                    .pathType(PathType.WATER)  // Less dangerous than DAMAGE_OTHER
                    .adjacentPathType(null)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.AMETHYST_BLOCK_CHIME)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)  // Same as dragon breath
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA))  // Same as dragon breath
            .fluidProperties(properties -> properties
                    .explosionResistance(100F)
                    .levelDecreasePerBlock(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .slopeFindDistance(2)  // Same as dragon breath - KEY FOR SPREAD CONTROL!
                    .tickRate(30))  // Same as dragon breath
            .source(BaseFlowingFluid.Source::new)
            .tag(AllTags.AllFluidTags.BOTTOMLESS_DENY.tag)
            .block(FieryTearLiquidBlock::new)
            .lang("Fiery Tear")
            .build()
            .bucket()
            .properties(properties -> properties
                    .rarity(Rarity.RARE)
//                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
            )
            .lang("Fiery Tear Bucket")
            .tag(Tags.Items.BUCKETS)
            .build()
            .register();

    /**
     * 模组特定的流体标签
     */
    public static class MOD_TAGS {
        /**
         * 鼓风机复生催化剂流体标签
         */
        public static final TagKey<Fluid> fanTransmutationCatalysts = TagKey.create(Registries.FLUID, CSCommon.asResource("fan_transmutation_catalysts"));
    }

    public static void register(IEventBus modBus) {
        modBus.register(CSFluids.class);
    }

    @SubscribeEvent
    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
//            OpenPipeEffectHandler.REGISTRY.register(EXPERIENCE.get(), new ExperienceEffectHandler());
            OpenPipeEffectHandler.REGISTRY.register(SHIMMER.getSource(), new ShimmerOpenPipeEffect());
            OpenPipeEffectHandler.REGISTRY.register(CARMINITE_SOLUTION.getSource(), new CarminiteOpenPipeEffect());
            OpenPipeEffectHandler.REGISTRY.register(FIERY_TEAR.getSource(), new FieryTearOpenPipeEffect());
        });
    }
}
