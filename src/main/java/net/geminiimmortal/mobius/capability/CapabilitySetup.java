package net.geminiimmortal.mobius.capability;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.capability.boost.IBoostData;
import net.geminiimmortal.mobius.capability.boost.BoostData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilitySetup {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
                IBoostData.class,
                new Capability.IStorage<IBoostData>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<IBoostData> capability, IBoostData instance, Direction side) {
                        CompoundNBT tag = new CompoundNBT();
                        tag.putLong("LastBoost", instance.getLastBoost());
                        return tag;
                    }

                    @Override
                    public void readNBT(Capability<IBoostData> capability, IBoostData instance, Direction side, INBT nbt) {
                        CompoundNBT tag = (CompoundNBT) nbt;
                        instance.setLastBoost(tag.getLong("LastBoost"));
                    }
                },
                BoostData::new
        );
    }
}

