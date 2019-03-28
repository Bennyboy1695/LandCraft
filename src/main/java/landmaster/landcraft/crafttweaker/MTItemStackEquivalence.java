package landmaster.landcraft.crafttweaker;

import com.google.common.base.*;

import crafttweaker.api.item.*;
import crafttweaker.mc1120.item.*;
import net.minecraft.item.*;

public class MTItemStackEquivalence {
	public static final Equivalence<IItemStack> MTEQUIV = new Equivalence<IItemStack>() {

		@Override
		protected boolean doEquivalent(IItemStack a, IItemStack b) {
			return a.matches(b);
		}

		@Deprecated // TODO: de-deprecate and implement
		@Override
		protected int doHash(IItemStack t) {
			return t.hashCode();
		}
		
	};
	
	public static final Equivalence<ItemStack> VANILLAEQUIV = new Equivalence<ItemStack>() {

		@Override
		protected boolean doEquivalent(ItemStack a, ItemStack b) {
			return MCItemStack.createNonCopy(a).matches(MCItemStack.createNonCopy(b));
		}

		@Deprecated
		@Override
		protected int doHash(ItemStack t) {
			return t.hashCode();
		}
		
	};
}
