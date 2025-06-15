/*
 * Copyright (C) 2025  DragonsPlus
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.adonis.createshimmer.integration.jei.category.printing;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.processing.enchanter.CSEnchantmentHelper;
import com.adonis.createshimmer.common.registry.CSDataMaps;
import com.adonis.createshimmer.common.registry.CSEnchantments;
import com.adonis.createshimmer.common.registry.CSFluids;
import com.adonis.createshimmer.config.CSConfig;
import com.adonis.createshimmer.util.CSIntIntPair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.recipe.IRecipeManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.common.CommonHooks;
import plus.dragons.createdragonsplus.util.Pairs;

public class EnchantedBookPrintingRecipeJEI implements PrintingRecipeJEI {
    public static final PrintingRecipeJEI.Type TYPE = PrintingRecipeJEI
            .register(CSCommon.asResource("enchanted_book"), EnchantedBookPrintingRecipeJEI::createCodec);
    private final ResourceLocation id;
    private final EnchantmentInstance enchantment;
    private final ItemStack enchantmentBook;
    private final int cost;

    public EnchantedBookPrintingRecipeJEI(EnchantmentInstance enchantment) {
        this.id = PrintingRecipeJEI.super.getRegistryName().withSuffix("/" +
                enchantment.enchantment.getRegisteredName().replace(':', '/') +
                enchantment.level);
        this.enchantment = enchantment;
        this.enchantmentBook = EnchantedBookItem.createForEnchantment(enchantment);
        Optional<CSIntIntPair> optional = Optional.empty();
        var customCost = enchantment.enchantment.getData(CSDataMaps.PRINTING_ENCHANTED_BOOK_COST);
        if (customCost != null) {
            optional = customCost.stream().filter(pair -> pair.level() == enchantment.level).findFirst();
        }
        this.cost = (int) (optional.map(CSIntIntPair::value).orElseGet(() -> CSEnchantmentHelper.getEnchantmentCost(enchantment.enchantment, enchantment.level)) * CSConfig.fluids().printingEnchantedBookCostMultiplier.get());
    }

    public static MapCodec<EnchantedBookPrintingRecipeJEI> createCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return RecordCodecBuilder.<EnchantmentInstance>mapCodec(instance -> instance.group(
                Enchantment.CODEC.fieldOf("enchantment").forGetter(it -> it.enchantment),
                Codec.INT.fieldOf("level").forGetter(it -> it.level)).apply(instance, EnchantmentInstance::new)).xmap(
                        EnchantedBookPrintingRecipeJEI::new,
                        recipe -> recipe.enchantment);
    }

    public static List<PrintingRecipeJEI> listAll() {
        return Objects.requireNonNull(CommonHooks.resolveLookup(Registries.ENCHANTMENT))
                .listElements()
                .filter(enchantment -> !enchantment.is(CSEnchantments.MOD_TAGS.printingDeny))
                .flatMap(enchantment -> IntStream
                        .rangeClosed(enchantment.value().getMinLevel(), CSEnchantmentHelper.maxLevel(enchantment))
                        .mapToObj(level -> new EnchantedBookPrintingRecipeJEI(new EnchantmentInstance(enchantment, level))))
                .collect(Collectors.toList());
    }

    @Override
    public void setBase(IRecipeSlotBuilder slot) {
        slot.addItemLike(Items.BOOK);
    }

    @Override
    public void setTemplate(IRecipeSlotBuilder slot) {
        slot.addItemStack(enchantmentBook);
    }

    @Override
    public void setFluid(IRecipeSlotBuilder slot) {
        slot.addFluidStack(CSFluids.EXPERIENCE.get(), cost);
        CSDataMaps.getSourceFluidEntries(CSDataMaps.FLUID_UNIT_EXPERIENCE)
                .forEach(Pairs.accept((fluid, unit) -> slot.addFluidStack(fluid, (long) unit * cost)));
    }

    @Override
    public void setOutput(IRecipeSlotBuilder slot) {
        slot.addItemStack(enchantmentBook);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return id;
    }
}
