package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.block.MagicSoilBlock;
import com.simibubi.create.Create;
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

    public static void register(IEventBus modBus) {}
}