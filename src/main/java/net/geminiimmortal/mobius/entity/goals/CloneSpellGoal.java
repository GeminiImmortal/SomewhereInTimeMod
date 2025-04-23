package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.custom.ShatterCloneEntity;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CloneSpellGoal extends Goal {

    private GovernorEntity boss;
    private LivingEntity target;

    public CloneSpellGoal(GovernorEntity boss, LivingEntity target) {
        super();
        this.boss = boss;
        this.target = target;
    }

    private boolean activated = false;

    @Override
    public boolean canUse() {
        // Trigger when boss's health is 25% or lower and the goal isn't already activated
        return this.boss.getHealth() <= 100 && !this.activated;
    }

    @Override
    public void start() {
        this.boss.setCloned(true);
        this.activated = true;
        LivingEntity target = this.boss.getTarget();
        if (target != null) {
            // Summon clones around the player
            summonClones(target);
            summonCircle();
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    private void summonClones(LivingEntity target) {
        // Positioning the clones in a square around the player
        Vector3d targetPos = target.position();
        double distance = 5.0; // Distance from the player
        this.boss.setCloned(true);

        for (int i = 0; i < 4; i++) {
            double xOffset = (i % 2 == 0) ? distance : -distance;
            double zOffset = (i < 2) ? distance : -distance;

            CloneEntity clone = new CloneEntity(ModEntityTypes.CLONE.get(), boss.level);
            clone.setPos(targetPos.x + xOffset, targetPos.y, targetPos.z + zOffset);
            clone.lookAt(EntityAnchorArgument.Type.EYES, targetPos);
            boss.level.addFreshEntity(clone);
            boss.level.playSound(null, target, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f);
            flashAndBoom(clone.level, clone.blockPosition());
        }
    }

    public static void flashAndBoom(World world, BlockPos pos) {
        ((ServerWorld) world).sendParticles(ParticleTypes.EXPLOSION,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                1, 0, 0, 0, 0);

        world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE,
                SoundCategory.HOSTILE, 1.0f, 1.0f);
    }


    private void summonCircle() {
        LivingEntity target = this.boss.getTarget();
        if (target != null && target.isAlive()) {
            // Summon the spell entity
            ShatterCloneEntity spell = new ShatterCloneEntity(ModEntityTypes.SHATTER_CLONE.get(), this.boss.level);
            spell.setPos(this.boss.getX(), this.boss.getY(), this.boss.getZ());
            this.boss.level.addFreshEntity(spell);

        }
    }
}
