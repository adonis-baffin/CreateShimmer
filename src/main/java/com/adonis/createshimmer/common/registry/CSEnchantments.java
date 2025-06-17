package com.adonis.createshimmer.common.registry;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import plus.dragons.createdragonsplus.data.tag.TagRegistry;

public class CSEnchantments {
    public static final ModTags MOD_TAGS = new ModTags();

    public static void register(IEventBus modBus) {
        REGISTRATE.registerEnchantmentTags(MOD_TAGS);
    }

    public static class ModTags extends TagRegistry<Enchantment, RegistrateTagsProvider<Enchantment>> {
        public final TagKey<Enchantment> enchanting = tag(
                "blaze_enchanter/enchanting",
                "Blaze Enchanter Normal Enchanting Enchantments");
        public final TagKey<Enchantment> enchantingExclusive = tag(
                "blaze_enchanter/enchanting_exclusive",
                "Blaze Enchanter Normal Enchanting Exclusive Enchantments");
        public final TagKey<Enchantment> superEnchanting = tag(
                "blaze_enchanter/super_enchanting",
                "Blaze Enchanter Super Enchanting Enchantments");
        public final TagKey<Enchantment> superEnchantingExclusive = tag(
                "blaze_enchanter/super_enchanting_exclusive",
                "Blaze Enchanter Super Enchanting Exclusive Enchantments");
        public final TagKey<Enchantment> printingDeny = tag(
                "printer/deny",
                "Printer-Denied Enchantments");

        protected ModTags() {
            super(CSCommon.ID, Registries.ENCHANTMENT);
        }

        @Override
        public void generate(RegistrateTagsProvider<Enchantment> provider) {
            super.generate(provider);
            provider.addTag(enchanting)
                    .addTag(EnchantmentTags.IN_ENCHANTING_TABLE);
            provider.addTag(enchantingExclusive);
            provider.addTag(superEnchanting)
                    .addTag(enchanting)
                    .remove(enchantingExclusive)
                    .addTag(superEnchantingExclusive);
            provider.addTag(superEnchantingExclusive)
                    .addTag(EnchantmentTags.TREASURE)
                    .remove(EnchantmentTags.CURSE);
            provider.addTag(enchantingExclusive);
            provider.addTag(printingDeny);
        }
    }
}
