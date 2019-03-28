package landmaster.landcraft.tile;

import java.util.*;

import landmaster.landcraft.block.*;
import landmaster.landcraft.config.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class TELandiaAltarCore extends TELandiaAltarItemHolder implements ITickable {
	public static int MAX_PYRAMID_SIZE = 8;
	
	private boolean oldDoRenderBeam = doRenderBeam();
	
	public static class PedestalResult {
		public final int pyramidSize;
		public final List<TELandiaAltarPedestal> pedestals;
		
		public PedestalResult(int pyramidSize, List<TELandiaAltarPedestal> pedestals) {
			this.pyramidSize = pyramidSize;
			this.pedestals = pedestals;
		}
	}
	
	public boolean isBlockInRightPosition() {
		return this.hasWorld() && this.world.provider.getDimension() == Config.landiaDimensionID
				&& this.world.canBlockSeeSky(pos);
	}
	
	public boolean doRenderBeam() {
		return isBlockInRightPosition() && !ish.getStackInSlot(0).isEmpty();
	}
	
	public PedestalResult getPedestals() {
		if (!isBlockInRightPosition()) {
			return new PedestalResult(0, Collections.emptyList());
		}
		
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
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public void update() {
		if (oldDoRenderBeam != doRenderBeam()) {
			oldDoRenderBeam = doRenderBeam();
			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}
}
