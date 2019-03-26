package landmaster.landcraft.net;

import io.netty.buffer.*;
import landmaster.landcore.api.*;
import landmaster.landcraft.tile.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.items.*;

public class PacketUpdateLandiaAltarItemHolder implements IMessage {
	private Coord4D coord;
	private ItemStack stack;
	
	public PacketUpdateLandiaAltarItemHolder() {}
	public PacketUpdateLandiaAltarItemHolder(Coord4D coord, ItemStack stack) {
		this.coord = coord;
		this.stack = stack;
	}
	
	public static IMessage onMessage(PacketUpdateLandiaAltarItemHolder message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			if (Minecraft.getMinecraft().world != null && message.coord.dimensionId == Minecraft.getMinecraft().world.provider.getDimension()) {
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.coord.pos());
				if (te instanceof TELandiaAltarItemHolder) {
					((IItemHandlerModifiable)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
					.setStackInSlot(0, message.stack);
					//System.out.println(message.stack);
				}
			}
		});
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		coord = Coord4D.fromByteBuf(buf);
		stack = ByteBufUtils.readItemStack(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(coord.toByteBuf(buf), stack);
	}
	
}
