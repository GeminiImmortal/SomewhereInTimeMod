package net.geminiimmortal.mobius.capability.infamy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class InfamyStorage implements Capability.IStorage<IInfamy> {

    @Override
    public INBT writeNBT(Capability<IInfamy> capability, IInfamy instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("Infamy", instance.getInfamy());
        tag.putLong("TriggerStart", instance.getInfamyTriggerStart());
        tag.putLong("LastPatrolCheck", instance.getLastPatrolCheck());
        return tag;
    }

    @Override
    public void readNBT(Capability<IInfamy> capability, IInfamy instance, Direction side, INBT nbt) {
        if (!(nbt instanceof CompoundNBT)) return;
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setInfamy(tag.getInt("Infamy"));
        instance.setInfamyTriggerStart(tag.getLong("TriggerStart"));
        instance.setLastPatrolCheck(tag.getLong("LastPatrolCheck"));
    }

}


