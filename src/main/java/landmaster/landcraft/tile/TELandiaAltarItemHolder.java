package landmaster.landcraft.tile;

import landmaster.landcore.api.*;
import landmaster.landcraft.net.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.items.*;

public class TELandiaAltarItemHolder extends TileEntity {
	private final ItemStackHandler ish = new ItemStackHandler(1) {
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			if (!world.isRemote) {
				markDirty();
				PacketHandler.INSTANCE.sendToAllAround(new PacketUpdateLandiaAltarItemHolder(
						new Coord4D(TELandiaAltarItemHolder.this), this.getStackInSlot(0)),
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
			}
		}
	};
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (world.isRemote) {
			PacketHandler.INSTANCE.sendToServer(new PacketRequestUpdateLandiaAltarItemHolder(new Coord4D(this)));
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> cap, EnumFacing facing) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, facing);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> cap, EnumFacing facing) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)ish;
		}
		return super.getCapability(cap, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		ish.deserializeNBT(compound.getCompoundTag("StackHandler"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("StackHandler", ish.serializeNBT());
		return compound;
	}
}
