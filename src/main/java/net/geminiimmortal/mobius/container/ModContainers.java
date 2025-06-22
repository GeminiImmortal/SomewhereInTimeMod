package net.geminiimmortal.mobius.container;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.container.custom.*;
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

    public static final RegistryObject<ContainerType<EssenceChannelerContainer>> ESSENCE_CHANNELER_CONTAINER
            = CONTAINERS.register("essence_channeler_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new EssenceChannelerContainer(windowId, world, pos, inv, inv.player);
            })));

    public static final RegistryObject<ContainerType<LatentManaCollectorContainer>> LATENT_MANA_COLLECTOR_CONTAINER =
            CONTAINERS.register("latent_mana_collector_container",
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World world = inv.player.level;
                        return new LatentManaCollectorContainer(windowId, world, pos, inv, inv.player);
                    })));

    public static final RegistryObject<ContainerType<JournalContainer>> JOURNAL_CONTAINER =
            CONTAINERS.register("journal", () ->
                    IForgeContainerType.create((windowId, inv, data) ->
                             new JournalContainer(windowId, inv)));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
