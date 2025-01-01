package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.MolvanEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;

import java.util.EnumSet;
import java.util.List;

public class AngryOnChestOpenGoal extends Goal {
    private final MolvanEntity mob;
    private final double detectionRadius;

    public AngryOnChestOpenGoal(CreatureEntity mob, double detectionRadius) {
        this.mob = (MolvanEntity) mob;
        this.detectionRadius = detectionRadius;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // Check for nearby players opening chests
        List<PlayerEntity> nearbyPlayers = mob.level.getEntitiesOfClass(
                PlayerEntity.class,
                mob.getBoundingBox().inflate(detectionRadius)
        );

        for (PlayerEntity player : nearbyPlayers) {
            if (isPlayerOpeningChest(player) && mob.getSensing().canSee(player)) {
                mob.setAngry(true);
                mob.setTarget(player);
//                mob.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.MOLVAN_ANGRY.get(), SoundCategory.HOSTILE, 2.0F, 0.8f);
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
    }

    private boolean isPlayerOpeningChest(PlayerEntity player) {
        // Check if the player is interacting with a chest
        return player.containerMenu instanceof ChestContainer; // ContainerMenu for chest
    }
}

