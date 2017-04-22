package landmaster.landcraft.gui;

import java.text.*;
import java.util.*;

import landmaster.landcraft.*;
import landmaster.landcraft.container.*;
import landmaster.landcraft.tile.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.*;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;

public class GuiTEThoriumGenerator extends GuiEnergy implements IGuiFluid {
	private static final ResourceLocation background = new ResourceLocation(LandCraft.MODID, "textures/gui/thorium_generator.png");
	
	private int fheight = 0;
	
	private int progress;
	private FluidStack fs;
	private ContTEThoriumGenerator cont;
	
	public GuiTEThoriumGenerator(ContTEThoriumGenerator cont) {
		super(cont);
		this.cont = cont;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		fheight = 0;
		if (fs != null) {
			TextureAtlasSprite ftex = mc.getTextureMapBlocks().getTextureExtry(
					fs.getFluid().getStill().toString());
			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			fheight = fs.amount * 50 / 8000;
			drawTexturedModalRect(guiLeft+60, guiTop+15+(50-fheight), ftex, 64, fheight);
		}
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft+60, guiTop+15, 176, 0, 64, 50);
		this.drawBackBar(guiLeft+130, guiTop+15);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		int k = (this.width - this.xSize) / 2; //X asis on GUI
		int l = (this.height - this.ySize) / 2; //Y asis on GUI
		int trueX = mouseX-k, trueY = mouseY-l;
		
		fontRendererObj.drawString(I18n.format("tile.thorium_generator.name"), 8, 6, 0x404040);
		fontRendererObj.drawString(cont.getPlayerInv().getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
		
		if (progress >= 0) {
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(1);
			fontRendererObj.drawString(nf.format(
					((double)progress) / TEThoriumGenerator.THORIUM_BURN_TIME),
					30, 54, 0x0000FF);
		}
		
		if (fs != null && isPointInRegion(60, 15, 64, 50, mouseX, mouseY)) {
			drawHoveringText(Arrays.asList(fs.getLocalizedName(), fs.amount+"mB"), trueX, trueY);
		}
		
		this.drawFrontBar(130, 15, mouseX, mouseY);
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public void setFluidStack(FluidStack fs) {
		this.fs = fs;
	}
}