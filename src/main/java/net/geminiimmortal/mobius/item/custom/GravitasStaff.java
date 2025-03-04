package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.item.StaffType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GravitasStaff extends ModularStaff {
    private final Map<UUID, Vector3d> frozenProjectiles = new HashMap<>();
    private final StaffType staffType;

    public GravitasStaff(Properties properties, StaffType staffType) {
        super(properties, staffType);
        this.staffType = staffType;
    }

    @Override
    public void onUseTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClientSide()) {
            double range = 10.0;
            List<ProjectileEntity> projectiles = world.getEntitiesOfClass(ProjectileEntity.class,
                    user.getBoundingBox().inflate(range),
                    e -> e != null && e.tickCount > 2);

            for (ProjectileEntity projectile : projectiles) {
                if (!frozenProjectiles.containsKey(projectile.getUUID())) {
                    frozenProjectiles.put(projectile.getUUID(), projectile.getDeltaMovement());
                    projectile.setDeltaMovement(Vector3d.ZERO);
                    projectile.setNoGravity(true);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity user, int timeCharged) {
        if (!world.isClientSide()) {
            for (UUID id : frozenProjectiles.keySet()) {
                Entity entity = ((ServerWorld) world).getEntity(id);

                if (entity instanceof ProjectileEntity) {
                    ProjectileEntity projectile = (ProjectileEntity) entity;
                    Vector3d originalVelocity = frozenProjectiles.get(id);
                    projectile.setDeltaMovement(originalVelocity.scale(-1)); // Corrected velocity reversal
                    projectile.setNoGravity(false);
                }
            }
            user.addEffect(new EffectInstance(this.staffType.getEffect(),this.staffType.getEffectDuration(), this.staffType.getEffectLevel()));
            user.level.playSound(null, user.getX(), user.getY(), user.getZ(), this.staffType.getSound().resolve().get(), SoundCategory.AMBIENT, 1.0f, 1.0f);
            frozenProjectiles.clear();
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack manaVial = findManaVial(player);
        if (manaVial == null || getStoredMana(manaVial) < staffType.getManaCost()) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff.not_enough_mana"), true);
            return ActionResult.fail(stack);
        }

        int currentMana = getStoredMana(manaVial);
        if (currentMana < this.staffType.getManaCost()) {
            player.displayClientMessage(new TranslationTextComponent("item.mobius.staff_of_protection.not_enough_mana"), true);
            return ActionResult.fail(stack);
        }

        if (world.isClientSide() && !manaVial.isEmpty()) {
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.playSound(this.staffType.getSound().resolve().get(), 1.0F, 1.0F);
        }

        player.startUsingItem(hand);
        setStoredMana(manaVial, currentMana - this.staffType.getManaCost());
        return ActionResult.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
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
}
