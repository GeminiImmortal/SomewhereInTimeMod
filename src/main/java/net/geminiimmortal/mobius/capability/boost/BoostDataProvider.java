package net.geminiimmortal.mobius.capability.boost;

import net.geminiimmortal.mobius.MobiusMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BoostDataProvider implements ICapabilitySerializable<INBT> {
    public static final ResourceLocation ID = new ResourceLocation(MobiusMod.MOD_ID, "boost_data");

    private final IBoostData instance = new BoostData();

    @CapabilityInject(IBoostData.class)
    public static Capability<IBoostData> BOOST_CAP = null;

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == BOOST_CAP ? LazyOptional.of(() -> instance).cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLong("LastBoost", instance.getLastBoost());
        return tag;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setLastBoost(tag.getLong("LastBoost"));
    }
}

