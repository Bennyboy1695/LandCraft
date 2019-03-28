package landmaster.landcraft.tile.render;

import org.lwjgl.opengl.*;

import landmaster.landcraft.api.*;
import landmaster.landcraft.tile.*;
import landmaster.landcraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.*;
import net.minecraftforge.items.*;

public class TESRLandiaAltarItemHolder extends TileEntitySpecialRenderer<TELandiaAltarItemHolder> {
	public static final ResourceLocation LASER_LOC = new ResourceLocation(ModInfo.MODID, "textures/effects/laserbeam.png");
	
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
			
			if (te instanceof TELandiaAltarCore
					&& ((TELandiaAltarCore)te).doRenderBeam()) {
				GlStateManager.pushMatrix();
				
				EntityPlayerSP player = Minecraft.getMinecraft().player;
				
				double doubleX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
	            double doubleY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
	            double doubleZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
				
				
				Vec3d vec = new Vec3d(doubleX, doubleY+player.getEyeHeight(), doubleZ);
				Vec3d vec0 = new Vec3d(te.getPos()).add(0.5, 0.5, 0.5);
				Vec3d vec1 = new Vec3d(te.getPos().getX()+0.5, te.getWorld().getHeight(), te.getPos().getZ()+0.5);
				
				GlStateManager.translate(-doubleX, -doubleY, -doubleZ);
				
				Tessellator tessellator = Tessellator.getInstance();
	            BufferBuilder buffer = tessellator.getBuffer();
	            
	            this.bindTexture(LASER_LOC);
	            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
				ClientUtils.drawBeam(buffer, vec0, vec1, vec, 0.09f);
				tessellator.draw();
				
				GlStateManager.popMatrix();
			}
			
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}
}
