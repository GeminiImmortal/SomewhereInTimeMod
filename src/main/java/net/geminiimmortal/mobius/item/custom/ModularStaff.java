package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.item.StaffType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ModularStaff extends Item {
    private final StaffType staffType;

    public ModularStaff(Properties properties, StaffType staffType) {
        super(properties);
        this.staffType = staffType;
    }

    private static final List<StaffType> lightningStaffType = new ArrayList<>();
    private static final List<StaffType> galeStaffType = new ArrayList<>();

    static {
        lightningStaffType.add(StaffType.LIGHTNING_OBSIDIAN_MOLVAN_STEEL);
    }

    static {
        galeStaffType.add(StaffType.LIGHTNING_OBSIDIAN_FAE_LEATHER);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack manaVial = findManaVial(player);
        CompoundNBT tag = stack.getOrCreateTag();
        long currentTime = level.getGameTime();
        long lastUsed = tag.getLong("LastUsedTime");

        if (manaVial == null || getStoredMana(manaVial) < staffType.getManaCost()) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
            return ActionResult.fail(stack);
        }

        if (level.isClientSide() && !manaVial.isEmpty()) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(this.staffType.getSound().resolve().get(), 1.0F, 1.0F);
        }

        if (!level.isClientSide()) {

            if (tag.contains("LastUsedTime")) {
                long lastUsedTime = tag.getLong("LastUsedTime");
                if (currentTime - lastUsedTime < this.staffType.getCooldown()) {
                    player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.cooldown"), true);
                    return ActionResult.fail(stack);
                }
            }

            if (manaVial.isEmpty()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.no_mana"), true);
                return ActionResult.fail(stack);
            }

            int currentMana = getStoredMana(manaVial);
            if (currentMana < this.staffType.getManaCost()) {
                player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"), true);
                return ActionResult.fail(stack);
            }

            setStoredMana(manaVial, currentMana - this.staffType.getManaCost());
            if(this.staffType.equals(galeStaffType.stream().findFirst().get())) {
                if (!level.isClientSide()) {
                    double radius = 6.0;
                    double force = 2.5;

                    AxisAlignedBB area = player.getBoundingBox().inflate(radius);
                    List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area,
                            entity -> entity != player && entity.isAlive() && entity.distanceTo(player) < radius);

                    for (LivingEntity entity : entities) {
                        Vector3d pushDirection = entity.position().subtract(player.position()).normalize();

                        Vector3d pushVelocity = pushDirection.scale(force);
                        entity.setDeltaMovement(pushVelocity.x, 0.3, pushVelocity.z);
                    }
                }

            }
            if(this.staffType.equals(lightningStaffType.stream().findFirst().get())) {
                if (!level.isClientSide()) {
                    RayTraceResult rayTrace = player.pick(100, 0.0F, false);

                    if (rayTrace.getType() == RayTraceResult.Type.BLOCK) {
                        BlockRayTraceResult blockHit = (BlockRayTraceResult) rayTrace;
                        BlockPos targetPos = blockHit.getBlockPos();

                        LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(level);
                        if (lightning != null) {
                            lightning.setPos(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                            level.addFreshEntity(lightning);
                        }
                    }
                }
            }
            player.addEffect(new EffectInstance(this.staffType.getEffect(),this.staffType.getEffectDuration(), this.staffType.getEffectLevel()));
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), this.staffType.getSound().resolve().get(), SoundCategory.AMBIENT, 1.0f, 1.0f);

            tag.putLong("LastUsedTime", currentTime);
            stack.setTag(tag);

        }
        return ActionResult.sidedSuccess(stack, level.isClientSide());
    }

    private ItemStack findManaVial(PlayerEntity player) {
        for (ItemStack stack : player.inventory.items) {
            if (stack.getItem() instanceof ManaVial && getStoredMana(stack) > 0) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private int getStoredMana(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        return tag.getInt("StoredMana");
    }

    private void setStoredMana(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("StoredMana", amount);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getCooldownProgress(stack) > 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0 - getCooldownProgress(stack);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xFFAA00;
    }

    @Override
    public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide() && entity instanceof PlayerEntity) {
            updateCooldown(stack, level);
        }
    }

    private void updateCooldown(ItemStack stack, World level) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            assert tag != null;
            long lastUsed = tag.getLong("LastUsedTime");
            long currentTime = level.getGameTime();
            long cooldownTicks = staffType.getCooldown();

            if (currentTime - lastUsed >= cooldownTicks) {
                tag.remove("LastUsedTime");
                stack.setTag(tag);
            }
        }
    }

    private float getCooldownProgress(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            assert tag != null;
            long lastUsed = tag.getLong("LastUsedTime");
            long currentTime = Minecraft.getInstance().level.getGameTime();
            long cooldownTicks = staffType.getCooldown();
            long timeRemaining = cooldownTicks - (currentTime - lastUsed);

            if (timeRemaining <= 0) {
                return 1.0F;
            }

            return 1.0F - ((float) timeRemaining / cooldownTicks);
        }
        return 1.0F;
    }

}

