package landmaster.landcraft.crafttweaker;

import crafttweaker.*;
import crafttweaker.api.item.*;
import crafttweaker.mc1120.item.*;
import landmaster.landcraft.api.*;

public class RemoveAltarRecipeEntityDeathTriggerAction implements IAction {
	private final IIngredient output;
	
	public RemoveAltarRecipeEntityDeathTriggerAction(IIngredient output) {
		this.output = output;
	}
	
	@Override
	public void apply() {
		LandiaAltarRecipes.getEntityDeathTriggerList().removeIf(recipe -> recipe.possibleOutputs().stream()
				.anyMatch(pair -> output.matches(MCItemStack.createNonCopy(pair.getLeft()))));
	}
	
	@Override
	public String describe() {
		return "Removing entity death trigger altar recipe for "+this.output;
	}
	
}
