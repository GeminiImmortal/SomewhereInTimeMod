package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.CloneEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.entity.custom.GovernorCloneEntity;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class CloneSpellGoal extends Goal {
    private LivingEntity target;
    private GovernorEntity boss;
    private int cooldown;
    private final int ATTACK_INTERVAL;

    public CloneSpellGoal(GovernorEntity boss, int cooldownInTicks) {
        super();
        this.boss = boss;
        this.cooldown = 0;
        this.target = boss.getTarget();
        this.ATTACK_INTERVAL = cooldownInTicks;
    }


    @Override
    public boolean canUse() {
        return this.boss != null
                && this.boss.getHealth() <= this.boss.getMaxHealth() * 0.5
                && this.cooldown <= 0;
    }

    @Override
    public void start() {
        this.cooldown = this.ATTACK_INTERVAL;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.boss != null) {
            return super.canContinueToUse();
        }

        return false;
    }

    @Override
    public void tick() {
        System.out.println("Clone goal ticking...");
        super.tick();

        this.cooldown--;
        if (this.cooldown == 0) {
            // Summon clones around the player
            summonClones(target);
            summonCircle();
        }
        if (this.cooldown == 0) {
            this.cooldown = this.ATTACK_INTERVAL;
        }
    }

    private void summonClones(LivingEntity target) {
        Vector3d targetPos = target.position();
        double distance = 5.0;

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
            GovernorCloneEntity spell = new GovernorCloneEntity(ModEntityTypes.GOVERNOR_CLONE.get(), this.boss.level);
            spell.setPos(this.boss.getX(), this.boss.getY(), this.boss.getZ());
            this.boss.level.addFreshEntity(spell);

        }
    }

}
