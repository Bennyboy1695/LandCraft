package landmaster.landcraft.tile;

import java.util.*;

import landmaster.landcraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;

public class TELandiaAltarCore extends TELandiaAltarItemHolder {
	public static int MAX_PYRAMID_SIZE = 8;
	
	public static class PedestalResult {
		public final int pyramidSize;
		public final List<TELandiaAltarPedestal> pedestals;
		
		public PedestalResult(int pyramidSize, List<TELandiaAltarPedestal> pedestals) {
			this.pyramidSize = pyramidSize;
			this.pedestals = pedestals;
		}
	}
	
	public PedestalResult getPedestals() {
		/*
		List<TELandiaAltarPedestal> pedestals = Utils.getTileEntitiesWithinAABB(world, TELandiaAltarPedestal.class,
				Utils.AABBfromVecs(new Vec3d(pos).subtract(MAX_PYRAMID_SIZE, MAX_PYRAMID_SIZE, MAX_PYRAMID_SIZE),
						new Vec3d(pos).add(MAX_PYRAMID_SIZE+1, MAX_PYRAMID_SIZE+1, MAX_PYRAMID_SIZE+1)));
						*/
		List<TELandiaAltarPedestal> pedestals = new ArrayList<>();
		int level = 0;
		levelCheck:
		while (level < MAX_PYRAMID_SIZE) {
			List<TELandiaAltarPedestal> levelPedestals = new ArrayList<>();
			for (BlockPos.MutableBlockPos checkPos: BlockPos.getAllInBoxMutable(
					pos.add(-level-1, -level-1, -level-1),
					pos.add(level+1, -level-1, level+1))) {
				IBlockState state = world.getBlockState(checkPos);
				if (state.getBlock() instanceof BlockLandiaAltar) {
					switch (state.getValue(BlockLandiaAltar.PART)) {
					case MATERIAL:
						break;
					case PEDESTAL:
						TileEntity te = world.getTileEntity(checkPos);
						if (te instanceof TELandiaAltarPedestal) {
							levelPedestals.add((TELandiaAltarPedestal)te);
						}
						break;
					case CORE:
					default:
						break levelCheck;
					}
				} else {
					break levelCheck;
				}
			}
			pedestals.addAll(levelPedestals);
			++level;
		}
		return new PedestalResult(level, pedestals);
	}
}
