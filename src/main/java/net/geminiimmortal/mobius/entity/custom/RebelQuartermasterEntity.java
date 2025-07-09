package net.geminiimmortal.mobius.entity.custom;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class RebelQuartermasterEntity extends VillagerEntity implements IMerchant, IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public RebelQuartermasterEntity(EntityType<? extends VillagerEntity> entityType, World world){
        this(entityType, world, VillagerType.PLAINS);
    }

    public RebelQuartermasterEntity(EntityType<? extends VillagerEntity> entityType, World world, VillagerType villagerType) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().setType(villagerType).setProfession(VillagerProfession.NONE));
        this.maxUpStep = 1;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.31D)
                .add(Attributes.ATTACK_DAMAGE, 2.5D)
                .add(Attributes.FOLLOW_RANGE, 30.0D)
                .add(Attributes.ARMOR, 2.5D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.15D);
    }

    @Nullable
    @Override
    public VillagerEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<RebelQuartermasterEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if (this.getDeltaMovement().length() > 0.075) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rebel_quartermaster.run", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rebel_quartermaster.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
