package de.neylux.infinitesource.register.blocks;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class InfiniteWaterTank extends FluidTank {

    public InfiniteWaterTank(int capacity) {
        super(capacity);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tank != 0 ? FluidStack.EMPTY : new FluidStack(Fluids.WATER, Integer.MAX_VALUE);
    }

    @Override
    public int getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.getFluid() == Fluids.WATER) {
            return resource.copy();
        }
        return super.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return new FluidStack(Fluids.WATER, maxDrain);
    }
}
