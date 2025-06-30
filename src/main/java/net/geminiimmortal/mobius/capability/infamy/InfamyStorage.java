package net.geminiimmortal.mobius.capability.infamy;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class InfamyStorage implements Capability.IStorage<IInfamy> {
    @Override
    public INBT writeNBT(Capability<IInfamy> capability, IInfamy instance, Direction side) {
        return IntNBT.valueOf(instance.getInfamy());
    }

    @Override
    public void readNBT(Capability<IInfamy> capability, IInfamy instance, Direction side, INBT nbt) {
        instance.setInfamy(((IntNBT) nbt).getAsInt());
    }
}

