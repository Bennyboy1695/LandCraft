package landmaster.landcraft.jei;

import landmaster.landcraft.api.*;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.*;
import net.minecraft.client.*;

public abstract class LandiaAltarRecipeJEI implements IRecipeWrapper {
	protected final LandiaAltarRecipes.IAltarRecipe recipe;
	protected IDrawable itemFrame;
	
	public LandiaAltarRecipeJEI(LandiaAltarRecipes.IAltarRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		if (itemFrame != null) {
			for (int i=0; i<recipe.getMaxNumItems(); ++i) {
				itemFrame.draw(minecraft, 5 + 18*(i%7), 5 + 18*(i/7));
			}
			
			itemFrame.draw(minecraft, 5, 12 + 18*((recipe.getMaxNumItems()+6)/7));
		}
	}
}
