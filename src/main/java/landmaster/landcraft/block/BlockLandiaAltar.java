package landmaster.landcraft.block;

import java.util.*;
import java.util.Optional;

import javax.annotation.*;

import com.google.common.base.*;

import landmaster.landcore.api.block.*;
import landmaster.landcraft.content.*;
import landmaster.landcraft.tile.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.items.*;

public class BlockLandiaAltar extends Block implements IMetaBlockName {
	public static enum Part implements IStringSerializable {
		CORE, MATERIAL, PEDESTAL;
		
		@Override
		public String getName() {
			return this.toString().toLowerCase(Locale.US);
		}
	}
	public static final IProperty<Part> PART = PropertyEnum.create("part", Part.class);
	
	public BlockLandiaAltar() {
		super(Material.ROCK);
		this.setDefaultState(blockState.getBaseState().withProperty(PART, Part.CORE));
		this.setHarvestLevel("pickaxe", 8);
		this.setHardness(9.5f);
		this.setResistance(23.0f);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(LandCraftContent.creativeTab);
		this.setTranslationKey("landia_altar").setRegistryName("landia_altar");
	}
	
	private static final Map<Part, Supplier<TileEntity>> partToTE = new EnumMap<>(Part.class);
	static {
		partToTE.put(Part.CORE, TELandiaAltarCore::new);
		partToTE.put(Part.PEDESTAL, TELandiaAltarPedestal::new);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return partToTE.containsKey(state.getValue(PART));
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return Optional.ofNullable(partToTE.get(state.getValue(PART)))
				.map(Supplier::get).orElse(null);
	}
	
	public static final AxisAlignedBB BASE_BB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	
	@SuppressWarnings("deprecation") // Need to call super method
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(PART)) {
		case CORE:
		case PEDESTAL:
			return BASE_BB;
		default:
			return super.getBoundingBox(state, source, pos);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return blockState.getValue(PART) == Part.MATERIAL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState blockState) {
		return blockState.getValue(PART) == Part.MATERIAL;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return blockState.getValue(PART) == Part.MATERIAL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PART, Part.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PART).ordinal();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PART);
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i=0; i<Part.values().length; ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		return Part.values()[stack.getMetadata()].getName();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TELandiaAltarItemHolder) {
				if (!worldIn.isRemote) {
					IItemHandlerModifiable handler = (IItemHandlerModifiable)te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					ItemStack stack = playerIn.getHeldItem(hand);
					if (stack.isEmpty()) {
						playerIn.setHeldItem(hand, handler.extractItem(0, 1, false));
					} else {
						playerIn.setHeldItem(hand, handler.insertItem(0, stack, false));
					}
				}
				return true;
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TELandiaAltarItemHolder) {
			ItemStack stack = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
			if (!stack.isEmpty()) {
				EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
				world.spawnEntity(item);
			}
		}
		super.breakBlock(world, pos, state);
	}
}
