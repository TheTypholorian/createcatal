package net.xander.createcatal;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SinningRecipe extends ProcessingRecipe<RecipeWrapper> {
    public SinningRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(RecipeTypes.SINNING, params);
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        if (recipeWrapper.isEmpty()) {
            return false;
        }

        return ingredients.get(0).test(recipeWrapper.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 16;
    }
}
