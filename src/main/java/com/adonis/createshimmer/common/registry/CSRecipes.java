package com.adonis.createshimmer.common.registry;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.fan.glooming.GloomingRecipe;
import com.adonis.createshimmer.common.kinetics.fan.transmutation.TransmutationRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import plus.dragons.createdragonsplus.common.recipe.RecipeTypeInfo;

public class CSRecipes {
    private static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, CSCommon.ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CSCommon.ID);

    public static final RecipeTypeInfo<TransmutationRecipe> TRANSMUTATION = register("transmutation", () -> new StandardProcessingRecipe.Serializer<>(TransmutationRecipe::new));
    public static final RecipeTypeInfo<GloomingRecipe> GLOOMING = register("glooming", () -> new StandardProcessingRecipe.Serializer<>(GloomingRecipe::new));

    public static void register(IEventBus modBus) {
        TYPES.register(modBus);
        SERIALIZERS.register(modBus);
    }

    private static <R extends Recipe<?>> RecipeTypeInfo<R> register(String name, Supplier<? extends RecipeSerializer<R>> serializer) {
        return new RecipeTypeInfo<>(name, serializer, SERIALIZERS, TYPES);
    }
}
