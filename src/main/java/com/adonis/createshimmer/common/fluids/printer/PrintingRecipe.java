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

package com.adonis.createshimmer.common.fluids.printer;

import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.integration.jei.category.assembly.AssemblyPrintingCategory;
import com.adonis.createshimmer.util.CSLang;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.effects.PlaySoundEffect;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class PrintingRecipe extends ProcessingRecipe<PrintingInput, PrintingRecipeParams> implements IAssemblyRecipe {
    public PrintingRecipe(PrintingRecipeParams params) {
        super(CSRecipes.PRINTING, params);
    }

    public static Builder builder(ResourceLocation id, PlaySoundEffect sound) {
        return new Builder(id, sound);
    }

    public static Builder builder(ResourceLocation id) {
        return new Builder(id, new PlaySoundEffect(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ENCHANTMENT_TABLE_USE),
                ConstantFloat.of(1),
                UniformFloat.of(.9f, 1f)));
    }

    public void playSound(Level level, BlockPos pos, SoundSource source) {
        var sound = this.params.sound;
        level.playSound(null,
                pos,
                sound.soundEvent().value(),
                source,
                sound.volume().sample(level.random),
                sound.pitch().sample(level.random));
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    public boolean matches(PrintingInput input, Level level) {
        return ingredients.get(0).test(input.base()) &&
                ingredients.get(1).test(input.template()) &&
                (input.fluid().isEmpty() || fluidIngredients.get(0).test(input.fluid()));
    }

    @Override
    public Component getDescriptionForAssembly() {
        ItemStack[] matchingStacks = ingredients.get(1).getItems();
        List<FluidStack> matchingFluidStacks = fluidIngredients.getFirst().getMatchingFluidStacks();
        if (matchingStacks.length == 0 || matchingFluidStacks.isEmpty()) {
            return Component.literal("Invalid");
        }
        return CSLang.translate("recipe.assembly.printing",
                matchingStacks[0].getHoverName(),
                matchingFluidStacks.getFirst().getHoverName()).component();
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> required) {
        required.add(CSBlocks.PRINTER);
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
        list.add(getIngredients().get(1));
    }

    @Override
    public void addAssemblyFluidIngredients(List<FluidIngredient> list) {
        list.add(getFluidIngredients().getFirst());
    }

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> AssemblyPrintingCategory::new;
    }

    public static class Builder extends ProcessingRecipeBuilder<PrintingRecipeParams, PrintingRecipe, Builder> {
        protected Builder(ResourceLocation id, PlaySoundEffect sound) {
            super(PrintingRecipe::new, id);
            PrintingRecipeParams params = (PrintingRecipeParams) this.params;
            params.sound = sound;
        }

        @Override
        protected PrintingRecipeParams createParams() {
            return new PrintingRecipeParams();
        }

        @Override
        public Builder self() {
            return this;
        }
    }

    public static class Serializer<R extends PrintingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<PrintingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, PrintingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, PrintingRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
