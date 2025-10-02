package net.geminiimmortal.mobius.capability;

import net.geminiimmortal.mobius.capability.boost.IBoostData;
import net.geminiimmortal.mobius.capability.infamy.IInfamy;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ModCapabilities {
    @CapabilityInject(IInfamy.class)
    public static Capability<IInfamy> INFAMY_CAPABILITY = null;

    @CapabilityInject(IBoostData.class)
    public static Capability<IBoostData> BOOST_CAPABILITY = null;
}

