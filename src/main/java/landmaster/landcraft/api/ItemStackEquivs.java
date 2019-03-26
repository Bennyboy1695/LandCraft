package landmaster.landcraft.api;

import java.util.*;
import java.util.stream.*;

import com.google.common.base.*;

import net.minecraft.item.*;

public class ItemStackEquivs {
	public static final Equivalence<ItemStack> EQUAL_ITEMS = new Equivalence<ItemStack>() {

		@Override
		protected boolean doEquivalent(ItemStack a, ItemStack b) {
			return ItemStack.areItemsEqual(a, b);
		}

		@Override
		protected int doHash(ItemStack t) {
			return 31*t.getItem().hashCode() + t.getMetadata();
		}
		
	};
	
	public static List<Equivalence.Wrapper<ItemStack>> equalItemsList(ItemStack...itemStacks) {
		return Arrays.stream(itemStacks).map(EQUAL_ITEMS::wrap).collect(Collectors.toList());
	}
}
