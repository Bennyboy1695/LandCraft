package landmaster.landcraft.net;

import io.netty.buffer.*;
import landmaster.landcore.api.*;
import landmaster.landcraft.tile.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.items.*;

public class PacketRequestUpdateLandiaAltarItemHolder implements IMessage {
	private Coord4D pos;
	public PacketRequestUpdateLandiaAltarItemHolder() {}
	public PacketRequestUpdateLandiaAltarItemHolder(Coord4D pos) {
		this.pos = pos;
	}
	
	public static IMessage onMessage(PacketRequestUpdateLandiaAltarItemHolder message, MessageContext ctx) {
		TileEntity te = message.pos.TE();
		if (te instanceof TELandiaAltarItemHolder) {
			return new PacketUpdateLandiaAltarItemHolder(message.pos, te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0));
		}
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = Coord4D.fromByteBuf(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		pos.toByteBuf(buf);
	}
}
