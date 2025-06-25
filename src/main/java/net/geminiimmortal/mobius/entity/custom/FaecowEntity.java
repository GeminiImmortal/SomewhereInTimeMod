package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.block.ModBlocks;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class FaecowEntity extends CowEntity {
    public FaecowEntity(EntityType<? extends CowEntity> p_i48567_1_, World p_i48567_2_) {
        super(p_i48567_1_, p_i48567_2_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(ModItems.MANA_WART.get()), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }

    public static boolean canMobSpawn(EntityType<? extends CowEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return (world.getBlockState(pos.below()) == Blocks.GRASS.defaultBlockState()) || (world.getBlockState(pos.below()) == ModBlocks.AURORA_GRASS_BLOCK.get().defaultBlockState());
    }

    @Override
    public FaecowEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return ModEntityTypes.FAECOW.get().create(world);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == ModItems.MANA_WART.get();
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.getItem() == Items.BUCKET && !player.isCreative()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            itemstack.shrink(1);
            ItemStack customMilk = new ItemStack(ModItems.ECTOPLASM_BUCKET.get());

            if (itemstack.isEmpty()) {
                player.setItemInHand(hand, customMilk);
            } else if (!player.inventory.add(customMilk)) {
                player.drop(customMilk, false);
            }

            return ActionResultType.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }


}
