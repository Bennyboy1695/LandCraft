package landmaster.landcraft.tile.render;

import org.lwjgl.opengl.*;

import landmaster.landcraft.tile.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.item.*;
import net.minecraftforge.client.*;
import net.minecraftforge.items.*;

public class TESRLandiaAltarItemHolder extends TileEntitySpecialRenderer<TELandiaAltarItemHolder> {
	@Override
	public void render(TELandiaAltarItemHolder te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
				.getStackInSlot(0);
		if (!stack.isEmpty()) {
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.pushMatrix();
			
			GlStateManager.translate(x + 0.5, y + 0.55, z + 0.5);
			GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
			
			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
	
			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}
}
