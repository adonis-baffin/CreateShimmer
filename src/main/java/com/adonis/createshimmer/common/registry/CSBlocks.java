
package com.adonis.createshimmer.common.registry;

// import com.adonis.createshimmer.common.fluids.experience.ExperienceHatchBlock;
// import com.adonis.createshimmer.common.fluids.lantern.ExperienceLanternBlock;
// import com.adonis.createshimmer.common.fluids.lantern.ExperienceLanternMovementBehavior;
// import com.adonis.createshimmer.common.fluids.printer.PrinterBlock;
// import com.adonis.createshimmer.common.kinetics.grindstone.GrindstoneDrainBlock;
// import com.adonis.createshimmer.common.kinetics.grindstone.MechanicalGrindStoneItem;
// import com.adonis.createshimmer.common.kinetics.grindstone.MechanicalGrindstoneBlock;
// import com.adonis.createshimmer.common.processing.enchanter.BlazeEnchanterBlock;
// import com.adonis.createshimmer.common.processing.forger.BlazeForgerBlock;
import net.neoforged.bus.api.IEventBus;

@SuppressWarnings("removal")
public class CSBlocks {
//    public static final BlockEntry<MechanicalGrindstoneBlock> MECHANICAL_GRINDSTONE = REGISTRATE
//            .block("mechanical_grindstone", MechanicalGrindstoneBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(CSConfig.stress().setImpact(4.0))
//            .transform(pickaxeOnly())
//            .blockstate(BlockStateGen.axisBlockProvider(false))
//            .item(MechanicalGrindStoneItem::new)
//            .build()
//            .register();
//    public static final BlockEntry<GrindstoneDrainBlock> GRINDSTONE_DRAIN = REGISTRATE
//            .block("grindstone_drain", prop -> new GrindstoneDrainBlock(MECHANICAL_GRINDSTONE.get(), prop))
//            .initialProperties(SharedProperties::copperMetal)
//            .transform(CSConfig.stress().setImpact(4.0))
//            .transform(pickaxeOnly())
//            .blockstate(BlockStateGen.horizontalBlockProvider(true))
//            .item()
//            .transform(customItemModel())
//            .loot((loots, block) -> loots.add(block, LootTable.lootTable()
//                    .withPool(loots.applyExplosionCondition(MECHANICAL_GRINDSTONE, LootPool.lootPool()
//                            .setRolls(ConstantValue.exactly(1.0F))
//                            .add(LootItem.lootTableItem(MECHANICAL_GRINDSTONE))))
//                    .withPool(loots.applyExplosionCondition(AllBlocks.ITEM_DRAIN, LootPool.lootPool()
//                            .setRolls(ConstantValue.exactly(1.0F))
//                            .add(LootItem.lootTableItem(AllBlocks.ITEM_DRAIN))))))
//            .register();
//    public static final BlockEntry<ExperienceHatchBlock> EXPERIENCE_HATCH = REGISTRATE
//            .block("experience_hatch", ExperienceHatchBlock::new)
//            .initialProperties(SharedProperties::copperMetal)
//            .properties(p -> p.mapColor(MapColor.COLOR_GREEN).lightLevel(state -> 12))
//            .transform(pickaxeOnly())
//            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.get(), AssetLookup.standardModel(ctx, prov)))
//            .simpleItem()
//            .register();
//    public static final BlockEntry<PrinterBlock> PRINTER = REGISTRATE
//            .block("printer", PrinterBlock::new)
//            .initialProperties(SharedProperties::copperMetal)
//            .transform(pickaxeOnly())
//            .blockstate((ctx, prov) -> prov.horizontalBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
//            .item(AssemblyOperatorBlockItem::new)
//            .transform(customItemModel())
//            .register();
//    public static final BlockEntry<BlazeEnchanterBlock> BLAZE_ENCHANTER = REGISTRATE
//            .block("blaze_enchanter", BlazeEnchanterBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).lightLevel(BlazeBlock::getLight))
//            .transform(pickaxeOnly())
//            .addLayer(() -> RenderType::cutoutMipped)
//            .onRegister(block -> MovementBehaviour.REGISTRY.register(block, new BlazeMovementBehaviour()))
//            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag)
//            .blockstate((ctx, prov) -> prov.horizontalBlock(
//                    ctx.getEntry(),
//                    prov.models().getExistingFile(Create.asResource("block/blaze_burner/block"))))
//            .item()
//            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(),
//                    Create.asResource("block/blaze_burner/block_with_blaze")))
//            .build()
//            .register();
//    public static final BlockEntry<BlazeForgerBlock> BLAZE_FORGER = REGISTRATE
//            .block("blaze_forger", BlazeForgerBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).lightLevel(BlazeBlock::getLight))
//            .transform(pickaxeOnly())
//            .addLayer(() -> RenderType::cutoutMipped)
//            .onRegister(block -> MovementBehaviour.REGISTRY.register(block, new BlazeMovementBehaviour()))
//            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.tag)
//            .blockstate((ctx, prov) -> prov.horizontalBlock(
//                    ctx.getEntry(),
//                    prov.models().getExistingFile(Create.asResource("block/blaze_burner/block"))))
//            .item()
//            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(),
//                    Create.asResource("block/blaze_burner/block_with_blaze")))
//            .build()
//            .register();
//    public static final BlockEntry<ExperienceBlock> SUPER_EXPERIENCE_BLOCK = REGISTRATE
//            .block("super_experience_block", ExperienceBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .properties(p -> p.mapColor(MapColor.DIAMOND)
//                    .sound(new DeferredSoundType(1, .5f, () -> SoundEvents.AMETHYST_BLOCK_BREAK,
//                            () -> SoundEvents.AMETHYST_BLOCK_STEP, () -> SoundEvents.AMETHYST_BLOCK_PLACE,
//                            () -> SoundEvents.AMETHYST_BLOCK_HIT, () -> SoundEvents.AMETHYST_BLOCK_FALL))
//                    .requiresCorrectToolForDrops()
//                    .lightLevel(state -> 15))
//            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models()
//                    .withExistingParent(ctx.getName(), Create.asResource("block/experience_block"))
//                    .texture("all", ctx.getId().withPrefix("block/"))
//                    .texture("particle", ctx.getId().withPrefix("block/"))))
//            .transform(pickaxeOnly())
//            .lang("Block of Super Experience")
//            .tag(Tags.Blocks.STORAGE_BLOCKS)
//            .tag(BlockTags.BEACON_BASE_BLOCKS)
//            .item()
//            .properties(p -> p.rarity(Rarity.RARE))
//            .tag(Tags.Items.STORAGE_BLOCKS)
//            .build()
//            .register();
//    public static final BlockEntry<ExperienceLanternBlock> EXPERIENCE_LANTERN = REGISTRATE
//            .block("experience_lantern", ExperienceLanternBlock::new)
//            .initialProperties(SharedProperties::softMetal)
//            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GREEN))
//            .transform(pickaxeOnly())
//            .transform(mountedFluidStorage(CSMountedStorageTypes.EXPERIENCE_LANTERN))
//            .onRegister(block -> MovementBehaviour.REGISTRY.register(block, new ExperienceLanternMovementBehavior()))
//            .addLayer(() -> RenderType::cutoutMipped)
//            .blockstate((ctx, prov) -> prov.directionalBlock(ctx.get(), AssetLookup.standardModel(ctx, prov)))
//            .simpleItem()
//            .register();
    public static void register(IEventBus modBus) {}
}
