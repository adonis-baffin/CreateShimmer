package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.block.MagicSoilBlock;
import com.simibubi.create.Create;
import com.simibubi.create.content.materials.ExperienceBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import twilightforest.block.CastleDoorBlock;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

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
            .properties(p -> p.rarity(Rarity.RARE))
            .tag(Tags.Items.STORAGE_BLOCKS)
            .build()
            .register();

    public static void register(IEventBus modBus) {}
}