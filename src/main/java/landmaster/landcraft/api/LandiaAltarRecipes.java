package landmaster.landcraft.api;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.tuple.*;

import com.google.common.base.*;

import landmaster.landcraft.tile.*;
import landmaster.landcraft.util.*;
import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.items.*;

public class LandiaAltarRecipes {
	public static final int ENTITY_DEATH_TRIGGER_RANGE = 5;
	
	static {
		MinecraftForge.EVENT_BUS.register(LandiaAltarRecipes.class);
	}
	
	public static interface IAltarRecipe {
		/**
		 * 
		 * @param altarCore the tile entity
		 * @param candidates the itemstacks
		 * @return the result itemstack and the minimum pyramid size
		 */
		Pair<ItemStack, Integer> process(TELandiaAltarCore altarCore, List<ItemStack> candidates);
		
		Collection<Pair<ItemStack, Integer>> possibleOutputs();
	}
	
	public static interface IAltarRecipeEntityDeathTrigger extends IAltarRecipe {
		boolean shouldWork(LivingDeathEvent event);
	}
	
	public static class StandardAltarRecipeEntityDeathTrigger implements IAltarRecipeEntityDeathTrigger {
		public final Pair<ItemStack, Integer> output;
		public final Equivalence<ItemStack> equiv;
		public final List<Collection<Equivalence.Wrapper<ItemStack>>> stackMatchList;
		public final Class<?> entityClass;
		
		public StandardAltarRecipeEntityDeathTrigger(Pair<ItemStack, Integer> output, Equivalence<ItemStack> equiv, List<Collection<Equivalence.Wrapper<ItemStack>>> stackMatchList, Class<?> entityClass) {
			this.output = output;
			this.equiv = equiv;
			this.stackMatchList = stackMatchList;
			this.entityClass = entityClass;
		}
		
		@Override
		public Pair<ItemStack, Integer> process(TELandiaAltarCore altarCore, List<ItemStack> candidates) {
			if (stackMatchList.size() != candidates.size()) {
				return null;
			}
			List<Collection<Equivalence.Wrapper<ItemStack>>> stackMatchListCopy = new ArrayList<>(stackMatchList);
			for (ItemStack stackToTest: candidates) {
				Iterator<Collection<Equivalence.Wrapper<ItemStack>>> matchIt = stackMatchListCopy.iterator();
				boolean found = false;
				while (matchIt.hasNext()) {
					Collection<Equivalence.Wrapper<ItemStack>> coll = matchIt.next();
					if (coll.contains(equiv.wrap(stackToTest))) {
						found = true;
						matchIt.remove();
						break;
					}
				}
				if (!found) {
					return null;
				}
			}
			return output;
		}

		@Override
		public Collection<Pair<ItemStack, Integer>> possibleOutputs() {
			return Collections.singleton(output);
		}

		@Override
		public boolean shouldWork(LivingDeathEvent event) {
			return entityClass.isInstance(event.getEntityLiving());
		}
		
	}
	
	private static final List<IAltarRecipeEntityDeathTrigger> entityTriggerList = new ArrayList<>();
	public static void addEntityTriggerRecipe(IAltarRecipeEntityDeathTrigger recipe) {
		entityTriggerList.add(recipe);
	}
	
	public static Collection<IAltarRecipeEntityDeathTrigger> getEntityTriggerList() {
		return Collections.unmodifiableList(entityTriggerList);
	}
	
	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		for (TELandiaAltarCore te: Utils.getTileEntitiesWithinAABB(event.getEntity().world, TELandiaAltarCore.class,
				Utils.AABBfromVecs(event.getEntity().getPositionVector().subtract(ENTITY_DEATH_TRIGGER_RANGE, ENTITY_DEATH_TRIGGER_RANGE, ENTITY_DEATH_TRIGGER_RANGE),
						event.getEntity().getPositionVector().add(ENTITY_DEATH_TRIGGER_RANGE, ENTITY_DEATH_TRIGGER_RANGE, ENTITY_DEATH_TRIGGER_RANGE)))) {
			TELandiaAltarCore.PedestalResult pedRes = te.getPedestals();
			List<ItemStack> stacks = pedRes.pedestals.stream()
					.map(tePed -> tePed.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					.map(cap -> cap.getStackInSlot(0).copy())
					.filter(stack -> !stack.isEmpty())
					.collect(Collectors.toList());
			for (IAltarRecipeEntityDeathTrigger elem: entityTriggerList) {
				if (elem.shouldWork(event)) {
					Pair<ItemStack, Integer> result = elem.process(te, stacks);
					if (result != null && !result.getLeft().isEmpty() && pedRes.pyramidSize >= result.getRight()) {
						for (TELandiaAltarPedestal pedestal: pedRes.pedestals) {
							((IItemHandlerModifiable)pedestal.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
							.setStackInSlot(0, ItemStack.EMPTY);
						}
						((IItemHandlerModifiable)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
						.setStackInSlot(0, result.getLeft());
						break;
					}
				}
			}
		}
	}
}
