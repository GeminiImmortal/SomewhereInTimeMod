package net.geminiimmortal.mobius.container;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.container.custom.AstralConduitContainer;
import net.geminiimmortal.mobius.container.custom.SoulForgeContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, MobiusMod.MOD_ID);

    public static final RegistryObject<ContainerType<SoulForgeContainer>> SOUL_FORGE_CONTAINER
            = CONTAINERS.register("soul_forge_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new SoulForgeContainer(windowId, world, pos, inv, inv.player);
            })));

    public static final RegistryObject<ContainerType<AstralConduitContainer>> ASTRAL_CONDUIT_CONTAINER
            = CONTAINERS.register("astral_conduit_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new AstralConduitContainer(windowId, world, pos, inv, inv.player);
            })));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
