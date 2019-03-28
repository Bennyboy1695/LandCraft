package landmaster.landcraft.crafttweaker;

import java.util.*;

import com.google.common.base.*;

import crafttweaker.api.item.*;
import crafttweaker.mc1120.item.*;
import net.minecraft.item.*;

public class IngredientCollection extends AbstractCollection<Equivalence.Wrapper<ItemStack>> {
	private final IIngredient ingredient;
	
	public IngredientCollection(IIngredient ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public Iterator<Equivalence.Wrapper<ItemStack>> iterator() {
		return this.ingredient.getItems().stream()
				.map(stk -> (ItemStack)stk.getInternal())
				.map(MTItemStackEquivalence.VANILLAEQUIV::wrap)
				.iterator();
	}

	@Override
	public int size() {
		return this.ingredient.getItems().size();
	}
	
	@Override
	public boolean contains(Object o) {
		if (o instanceof Equivalence.Wrapper) {
			Equivalence.Wrapper<?> wrapped = (Equivalence.Wrapper<?>)o;
			if (wrapped.get() instanceof ItemStack) {
				return ingredient.matches(MCItemStack.createNonCopy((ItemStack)wrapped.get()));
			}
		}
		return false;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		if (c instanceof IngredientCollection) {
			return ingredient.contains(((IngredientCollection)c).ingredient);
		}
		return super.containsAll(c);
	}
}
