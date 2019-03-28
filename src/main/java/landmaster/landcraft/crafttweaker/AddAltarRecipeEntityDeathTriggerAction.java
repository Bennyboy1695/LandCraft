package landmaster.landcraft.crafttweaker;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.tuple.*;

import crafttweaker.*;
import crafttweaker.api.item.*;
import landmaster.landcraft.api.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class AddAltarRecipeEntityDeathTriggerAction implements IAction {
	private IItemStack output;
	private int minPyramidSize;
	private IIngredient[] ingredients;
	private String entity;
	
	public AddAltarRecipeEntityDeathTriggerAction(IItemStack output, int minPyramidSize, IIngredient[] ingredients, String entity) {
		this.output = output;
		this.minPyramidSize = minPyramidSize;
		this.ingredients = ingredients;
		this.entity = entity;
	}

	@Override
	public void apply() {
		Class<? extends Entity> clazz = EntityList.getClass(new ResourceLocation(entity));
		if (clazz != null) {
			LandiaAltarRecipes.addEntityTriggerRecipe(
					new LandiaAltarRecipes.StandardAltarRecipeEntityDeathTrigger(
							Pair.of((ItemStack)output.getInternal(), minPyramidSize),
							MTItemStackEquivalence.VANILLAEQUIV,
							Arrays.stream(ingredients)
							.map(IngredientCollection::new)
							.collect(Collectors.toList()),
							clazz
					));
		} else {
			CraftTweakerAPI.logWarning("No entity exists with identifier "+entity);
		}
	}

	@Override
	public String describe() {
		return "Adding entity death trigger altar recipe for "+this.output;
	}
	
}
