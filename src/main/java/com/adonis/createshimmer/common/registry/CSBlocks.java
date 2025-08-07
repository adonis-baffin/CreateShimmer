package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.block.MagicSoilBlock;
import com.adonis.createshimmer.common.block.OminousCampfireBlock;
import com.adonis.createshimmer.common.block.OminousCampfireBlock;
import com.simibubi.create.Create;
import com.simibubi.create.content.materials.ExperienceBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import twilightforest.block.CastleDoorBlock;

@SuppressWarnings("removal")
public class CSBlocks {
    public static final BlockEntry<MagicSoilBlock> MAGIC_SOIL = REGISTRATE
            .block("magic_soil", MagicSoilBlock::new)
            .initialProperties(() -> Blocks.DIRT)
            .properties(p -> p.mapColor(MapColor.COLOR_BROWN))
            .transform(pickaxeOnly())
            .simpleItem()
            .lang("Magic Soil")
            .register();

    public static final BlockEntry<CastleDoorBlock> CASTLE_DOOR = REGISTRATE
            .block("castle_door", CastleDoorBlock::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p
                    .forceSolidOn()
                    .mapColor((state) -> {
                        return state.getValue(CastleDoorBlock.VANISHED) ? MapColor.NONE : MapColor.STONE;
                    })
                    .pushReaction(PushReaction.BLOCK)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE_TILES)
                    .strength(100.0F, 100.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM))
            .transform(pickaxeOnly())
            .simpleItem()
            .lang("Castle Door")
            .register();

    public static final BlockEntry<ExperienceBlock> SHIMMER_BLOCK = REGISTRATE
            .block("shimmer_block", ExperienceBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.DIAMOND)
                    .sound(new DeferredSoundType(1, .5f, () -> SoundEvents.AMETHYST_BLOCK_BREAK,
                            () -> SoundEvents.AMETHYST_BLOCK_STEP, () -> SoundEvents.AMETHYST_BLOCK_PLACE,
                            () -> SoundEvents.AMETHYST_BLOCK_HIT, () -> SoundEvents.AMETHYST_BLOCK_FALL))
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 15))
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models()
                    .withExistingParent(ctx.getName(), Create.asResource("block/shimmer_block"))
                    .texture("all", ctx.getId().withPrefix("block/"))
                    .texture("particle", ctx.getId().withPrefix("block/"))))
            .transform(pickaxeOnly())
            .lang("Block of Shimmer")
            .tag(Tags.Blocks.STORAGE_BLOCKS)
            .tag(BlockTags.BEACON_BASE_BLOCKS)
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();

    // 通冥营火 - 简化版，无方块实体，具有真正冥火特性
    public static final BlockEntry<OminousCampfireBlock> OMINOUS_CAMPFIRE = REGISTRATE
            .block("ominous_campfire", OminousCampfireBlock::new)
            .initialProperties(() -> Blocks.CAMPFIRE)
            .properties(p -> p
                    .mapColor(MapColor.COLOR_PURPLE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(state -> state.getValue(OminousCampfireBlock.LIT) ? 8 : 0) // 比普通营火暗一点，神秘感
                    .noOcclusion()
                    .ignitedByLava())
            .transform(pickaxeOnly())
            .tag(BlockTags.CAMPFIRES) // 添加到营火标签，兼容性更好
            .tag(MOD_TAGS.fanGloomingCatalysts) // 添加到通冥催化剂标签
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .model((ctx, prov) -> prov.blockItem(ctx))
            .build()
            .lang("Ominous Campfire")
            .register();

    public static void register(IEventBus modBus) {}

    // 方块标签类
    public static class MOD_TAGS {
        // 通冥催化剂方块标签
        public static final TagKey<Block> fanGloomingCatalysts =
                tag("fan_glooming_catalysts");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, CSCommon.asResource(name));
        }
    }
}