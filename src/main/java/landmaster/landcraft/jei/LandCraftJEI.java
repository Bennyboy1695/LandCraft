package landmaster.landcraft.jei;

import landmaster.landcraft.api.*;
import landmaster.landcraft.config.*;
import landmaster.landcraft.container.*;
import landmaster.landcraft.content.*;
import landmaster.landcraft.crafttweaker.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.transfer.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class LandCraftJEI implements IModPlugin {
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		if (Config.breeder) {
			registry.addRecipeCategories(new BreederFeedstockCategory(guiHelper));
		}
		if (Config.pot) {
			registry.addRecipeCategories(new PotRecipeCategory(guiHelper));
		}
		registry.addRecipeCategories(new LandiaAltarRecipeCategory(guiHelper));
	}
	
	@Override
	public void register(IModRegistry registry) {
		LCLog.log.debug("Adding JEI integration for LandCraft");
		
		final IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		if (Config.breeder) {
			registry.handleRecipes(BreederFeedstock.OreMassTempTri.class, BreederFeedstockJEI::new, BreederFeedstockCategory.UID);
			registry.addRecipes(BreederFeedstock.getOreMassTempTris(), BreederFeedstockCategory.UID);
			//recipeTransferRegistry.addRecipeTransferHandler(ContTEBreeder.class, BreederFeedstockCategory.UID, 0, 1, 3, 36);
			registry.addRecipeCatalyst(new ItemStack(LandCraftContent.breeder), BreederFeedstockCategory.UID);
		}
		
		if (Config.pot) {
			registry.handleRecipes(PotRecipes.RecipePOredict.class, PotOredictRecipeJEI::new, PotRecipeCategory.UID);
			if (Loader.isModLoaded("crafttweaker")) {
				CraftTweaker.handlePotRecipes(registry);
			}
			registry.addRecipes(PotRecipes.getRecipeList(), PotRecipeCategory.UID);
			recipeTransferRegistry.addRecipeTransferHandler(ContTEPot.class, PotRecipeCategory.UID, 0, 3, 4, 36);
			registry.addRecipeCatalyst(new ItemStack(LandCraftContent.pot), PotRecipeCategory.UID);
		}
		
		registry.handleRecipes(LandiaAltarRecipes.StandardAltarRecipeEntityDeathTrigger.class,
				StandardAltarRecipeEntityDeathTriggerJEI::new, LandiaAltarRecipeCategory.UID);
		registry.addRecipes(LandiaAltarRecipes.getEntityTriggerList(), LandiaAltarRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(LandCraftContent.landia_altar), LandiaAltarRecipeCategory.UID);
	}
	
	private static class CraftTweaker {
		public static void handlePotRecipes(IModRegistry registry) {
			registry.handleRecipes(MTPotRecipeProcess.class, PotMTRecipeJEI::new, PotRecipeCategory.UID);
		}
	}
}
