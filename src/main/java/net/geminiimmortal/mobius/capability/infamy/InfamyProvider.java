package net.geminiimmortal.mobius.capability.infamy;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.ModCapabilities;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class InfamyProvider implements ICapabilitySerializable<INBT> {
    public static final ResourceLocation ID = new ResourceLocation(MobiusMod.MOD_ID, "infamy");

    private final Infamy instance = new Infamy();
    private final LazyOptional<IInfamy> optional = LazyOptional.of(() -> instance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilities.INFAMY_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return ModCapabilities.INFAMY_CAPABILITY.getStorage().writeNBT(ModCapabilities.INFAMY_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ModCapabilities.INFAMY_CAPABILITY.getStorage().readNBT(ModCapabilities.INFAMY_CAPABILITY, instance, null, nbt);
    }
}

