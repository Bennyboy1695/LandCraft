package landmaster.landcraft.jei;

import java.util.*;

import landmaster.landcraft.api.*;
import mezz.jei.api.*;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.*;
import mezz.jei.api.recipe.*;
import net.minecraft.client.resources.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class LandiaAltarRecipeCategory implements IRecipeCategory<LandiaAltarRecipeJEI> {
	public static final ResourceLocation background_rl = new ResourceLocation(ModInfo.MODID, "textures/jei/landia_altar.png");
	
	public static final String UID = "landcraft.altar";
	
	private final IDrawable background;
	final IDrawable itemFrame;
	
	LandiaAltarRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(background_rl, 0, 0, 160, 72);
		itemFrame = guiHelper.createDrawable(background_rl, 238, 238, 18, 18);
	}
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return I18n.format("landcraft.altar.name");
	}
	
	@Override
	public String getModName() {
		return ModInfo.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, LandiaAltarRecipeJEI recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup igroup = recipeLayout.getItemStacks();
		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		for (int i=0; i<inputs.size(); ++i) {
			igroup.init(i, true, 5 + 18*(i%7), 5 + 18*(i/7));
		}
		
		igroup.init(inputs.size(), false, 5, 12 + 18*((inputs.size()+6)/7));
		
		igroup.set(ingredients);
		
		recipeWrapper.itemFrame = itemFrame;
	}
	
}
