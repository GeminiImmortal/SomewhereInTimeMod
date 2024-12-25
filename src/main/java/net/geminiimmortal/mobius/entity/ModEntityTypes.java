package net.geminiimmortal.mobius.entity;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.custom.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MobiusMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, MobiusMod.MOD_ID);

    public static final RegistryObject<EntityType<MolvanEntity>> MOLVAN =
            ENTITY_TYPES.register("molvan",
                    () -> EntityType.Builder.of(MolvanEntity::new,
                                    EntityClassification.CREATURE).sized(0.75f, 0.75f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "molvan").toString()));

    public static final RegistryObject<EntityType<ClubGolemEntity>> CLUB_GOLEM =
            ENTITY_TYPES.register("club_golem",
                    () -> EntityType.Builder.of(ClubGolemEntity::new,
                                    EntityClassification.CREATURE).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "club_golem").toString()));

    public static final RegistryObject<EntityType<DiamondGolemEntity>> DIAMOND_GOLEM =
            ENTITY_TYPES.register("diamond_golem",
                    () -> EntityType.Builder.of(DiamondGolemEntity::new,
                                    EntityClassification.CREATURE).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "diamond_golem").toString()));

    public static final RegistryObject<EntityType<HeartGolemEntity>> HEART_GOLEM =
            ENTITY_TYPES.register("heart_golem",
                    () -> EntityType.Builder.of(HeartGolemEntity::new,
                                    EntityClassification.CREATURE).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "heart_golem").toString()));

    public static final RegistryObject<EntityType<SpadeGolemEntity>> SPADE_GOLEM =
            ENTITY_TYPES.register("spade_golem",
                    () -> EntityType.Builder.of(SpadeGolemEntity::new,
                                    EntityClassification.CREATURE).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "spade_golem").toString()));

    public static final RegistryObject<EntityType<FaedeerEntity>> FAEDEER =
            ENTITY_TYPES.register("faedeer",
                    () -> EntityType.Builder.of(FaedeerEntity::new,
                                    EntityClassification.CREATURE).sized(1f, 1f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "faedeer").toString()));

    public static final RegistryObject<EntityType<CloneEntity>> CLONE =
            ENTITY_TYPES.register("clone",
                    () -> EntityType.Builder.of(CloneEntity::new,
                                    EntityClassification.MONSTER).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "clone").toString()));

    public static final RegistryObject<EntityType<SorcererEntity>> SORCERER =
            ENTITY_TYPES.register("sorcerer",
                    () -> EntityType.Builder.of(SorcererEntity::new,
                                    EntityClassification.MONSTER).sized(1f, 2.5f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "sorcerer").toString()));

    public static final RegistryObject<EntityType<GovernorEntity>> GOVERNOR =
            ENTITY_TYPES.register("governor",
                    () -> EntityType.Builder.of(GovernorEntity::new,
                                    EntityClassification.MONSTER).sized(1f, 3f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "governor").toString()));

    public static final RegistryObject<EntityType<SpellEntity>> SPELL =
            ENTITY_TYPES.register("spell",
                    () -> EntityType.Builder.of(SpellEntity::new,
                                    EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "spell").toString()));

    public static final RegistryObject<EntityType<ShatterCloneEntity>> SHATTER_CLONE =
            ENTITY_TYPES.register("shatter_clone",
                    () -> EntityType.Builder.of(ShatterCloneEntity::new,
                                    EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "shatter_clone").toString()));

    public static final RegistryObject<EntityType<GovernorKnivesOutEntity>> KNIVES_OUT =
            ENTITY_TYPES.register("knives_out",
                    () -> EntityType.Builder.of(GovernorKnivesOutEntity::new,
                                    EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "knives_out").toString()));

    public static final RegistryObject<EntityType<ModBoatEntity>> MARROWOOD_BOAT =
            ENTITY_TYPES.register("marrowood_boat",
                    () -> EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new,
                                    EntityClassification.MISC).sized(0.5f, 0.5f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "marrowood_boat").toString()));

    public static final RegistryObject<EntityType<ManawoodBoatEntity>> MANAWOOD_BOAT =
            ENTITY_TYPES.register("manawood_boat",
                    () -> EntityType.Builder.<ManawoodBoatEntity>of(ManawoodBoatEntity::new,
                                    EntityClassification.MISC).sized(0.5f, 0.5f)
                            .build(new ResourceLocation(MobiusMod.MOD_ID, "manawood_boat").toString()));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
