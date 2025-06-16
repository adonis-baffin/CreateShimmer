package com.adonis.createshimmer.integration.jei.category.grinding;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.*;

import com.adonis.createshimmer.common.kinetics.grindstone.GrindingRecipe;
import com.adonis.createshimmer.common.registry.CSBlocks;
import com.adonis.createshimmer.common.registry.CSRecipes;
import com.adonis.createshimmer.util.CSLang;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

public class GrindingCategory implements IRecipeCategory<RecipeHolder<GrindingRecipe>> {
    public static final RecipeType<RecipeHolder<GrindingRecipe>> TYPE = RecipeType
            .createRecipeHolderType(CSRecipes.GRINDING.getId());
    private final Component title = CSLang.translate("recipe.grinding").component();
    private final IDrawable icon = new ItemIcon(CSBlocks.GRINDSTONE_DRAIN::asStack);
    private final AnimatedGrindstone grindstone = new AnimatedGrindstone();

    @Override
    public RecipeType<RecipeHolder<GrindingRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 177;
    }

    @Override
    public int getHeight() {
        return 70;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<GrindingRecipe> holder, IFocusGroup focuses) {
        var recipe = holder.value();
        var ingredient = recipe.getIngredients().get(0);
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 32)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ingredient);

        var fluidIngredients = recipe.getFluidIngredients();
        if (!fluidIngredients.isEmpty()) {
            CreateRecipeCategory.addFluidSlot(builder, 27, 51, fluidIngredients.getFirst());
        }

        List<ProcessingOutput> results = recipe.getRollableResults();
        int i = 0;
        var fluidResults = recipe.getFluidResults();
        if (!fluidResults.isEmpty()) {
            CreateRecipeCategory.addFluidSlot(builder, 130, 32, fluidResults.getFirst());
            i++;
        }
        for (ProcessingOutput output : results) {
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, 130 + xOffset, 32 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    @Override
    public void draw(RecipeHolder<GrindingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 115, 5);
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 52);
        grindstone.draw(graphics, 68, 32);
    }
}