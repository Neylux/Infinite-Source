package de.neylux.infinitesource.register.blocks;

import de.neylux.infinitesource.register.types.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfiniteWaterSourceBlockEntity  extends BlockEntity {

    private final FluidTank fluidTank = new InfiniteWaterTank(Integer.MAX_VALUE);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluidTank);

    public InfiniteWaterSourceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FARMING_STATION_BLOCK_ENTITY.get(), blockPos, blockState);
        fluidTank.setFluid(new FluidStack(Fluids.WATER, Integer.MAX_VALUE));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(capability, side);
    }
}
