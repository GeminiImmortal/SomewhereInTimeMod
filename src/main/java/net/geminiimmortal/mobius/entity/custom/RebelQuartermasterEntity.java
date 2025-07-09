package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.item.ModItems;
import net.geminiimmortal.mobius.villager.ModVillagers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
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
    private boolean tradesInitialized = false;
    private final MerchantOffer basic = new MerchantOffer((new ItemStack(Items.EMERALD)), new ItemStack(ModItems.MANAWOOD_LOG.get()), 1, 16, 0.05f);
    public RebelQuartermasterEntity(EntityType<? extends VillagerEntity> entityType, World world){
        this(entityType, world, VillagerType.register("rebel_quartermaster"));
    }

    public RebelQuartermasterEntity(EntityType<? extends VillagerEntity> entityType, World world, VillagerType villagerType) {
        super(entityType, world);
        this.setVillagerData(this.getVillagerData().setType(VillagerType.register("rebel_quartermaster")).setProfession(ModVillagers.QUARTERMASTER.get()));
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

    @Override
    public void tick() {
        super.tick();
        if (!tradesInitialized && !level.isClientSide) {
        //    setupTrades();
            tradesInitialized = true;
        }
    }

    private void setupTrades() {
        assert this.offers != null;
        this.basic.createTag();
        this.offers.add(basic);
        // Prevent restocking and leveling up
        this.xpReward = 0;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
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
