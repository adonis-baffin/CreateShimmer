package com.adonis.createshimmer.integration.jei.category;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.kinetics.fan.glooming.GloomingRecipe;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import java.util.List;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FanGloomingCategory extends ProcessingViaFanCategory<GloomingRecipe> {
    public static final mezz.jei.api.recipe.RecipeType<RecipeHolder<GloomingRecipe>> TYPE = mezz.jei.api.recipe.RecipeType.createRecipeHolderType(CSRecipes.GLOOMING.getId());

    private FanGloomingCategory(Info<GloomingRecipe> info) {
        super(info);
    }

    public static FanGloomingCategory create() {
        var id = CSCommon.asResource("fan_glooming");
        var title = Component.translatable("createshimmer.recipe.fan_glooming");
        var background = new EmptyBackground(178, 72);

        // 使用通冥营火作为图标 - 更有标识性
        var ominousCampfireStack = new ItemStack(CSBlocks.OMINOUS_CAMPFIRE.get());
        var icon = new DoubleItemIcon(AllItems.PROPELLER::asStack, () -> ominousCampfireStack);

        var catalyst = AllBlocks.ENCASED_FAN.asStack();
        catalyst.set(DataComponents.CUSTOM_NAME,
                Component.translatable("createshimmer.recipe.fan_glooming.fan")
                        .withStyle(style -> style.withItalic(false)));
        var info = new Info<>(TYPE, title, background, icon, FanGloomingCategory::getAllRecipes, List.of(() -> catalyst));
        return new FanGloomingCategory(info);
    }

    @Override
    protected void renderAttachedBlock(GuiGraphics graphics) {
        // 渲染暮色森林冥火方块作为催化剂方块
        var ominousFireBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("twilightforest", "ominous_fire"));
        GuiGameElement.of(ominousFireBlock.defaultBlockState())
                .scale(SCALE)
                .atLocal(0, 0, 2)
                .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                .render(graphics);
    }

    private static List<RecipeHolder<GloomingRecipe>> getAllRecipes() {
        var manager = CSJeiPlugin.getRecipeManager();
        return manager.getAllRecipesFor(CSRecipes.GLOOMING.getType());
    }
}
