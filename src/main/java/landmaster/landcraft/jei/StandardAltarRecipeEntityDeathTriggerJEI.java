package landmaster.landcraft.jei;

import java.util.stream.*;

import com.google.common.base.*;

import landmaster.landcraft.api.*;
import mezz.jei.api.ingredients.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;

public class StandardAltarRecipeEntityDeathTriggerJEI extends LandiaAltarRecipeJEI {
	private final LandiaAltarRecipes.StandardAltarRecipeEntityDeathTrigger recipe;
	
	public StandardAltarRecipeEntityDeathTriggerJEI(LandiaAltarRecipes.StandardAltarRecipeEntityDeathTrigger recipe) {
		super(recipe);
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, recipe.stackMatchList.stream()
				.map(coll -> coll.stream().map(Equivalence.Wrapper::get).collect(Collectors.toList()))
				.collect(Collectors.toList()));
		ingredients.setOutput(ItemStack.class, recipe.output.getLeft());
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
		String entityLocName = EntityList.getTranslationName(EntityList.getKey(recipe.entityClass));
		minecraft.fontRenderer.drawString(
				I18n.format("info.altar.jei.death_trigger", entityLocName),
				27, 12 + 18*((recipe.getMaxNumItems()+6)/7), 0x0000FF);
		minecraft.fontRenderer.drawString(
				I18n.format("info.altar.jei.min_size", recipe.output.getRight()),
				5, 12 + 22 + 18*((recipe.getMaxNumItems()+6)/7), 0xFF00FF);
	}
}
