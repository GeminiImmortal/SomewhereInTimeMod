package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.entity.custom.SorcererEntity;
import net.geminiimmortal.mobius.entity.custom.SorcererObliteratorEntity;
import net.geminiimmortal.mobius.entity.goals.util.TrackingLaserBeam;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.List;

public class ArcaneBeamStrikeGoal extends Goal {

    private final double summonHeight = 15.0;
    private final int summonCooldown = 100;
    private final int obliteratorDuration = 60;
    private final int beamInterval = 10;
    private LivingEntity cachedTarget;
    private SorcererEntity boss;
    private LivingEntity target;
    private int chargeTime;
    private BlockPos strikePos;
    private boolean hasSummonedCircle;

    private static final int MAX_CHARGE_TIME = 60; // 3 seconds at 20 ticks per second
    private static final float DAMAGE_RADIUS = 5.0F;
    private static final float DAMAGE_AMOUNT = 100.0F;

    public ArcaneBeamStrikeGoal(SorcererEntity boss) {
        this.boss = boss;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.boss.getTarget();
        return livingentity != null && livingentity.isAlive();
    }

    @Override
    public void start() {
        this.target = this.boss.getTarget();
        this.chargeTime = 250;
        this.hasSummonedCircle = false;


        this.strikePos = this.target.blockPosition().above(12);
    }

    @Override
    public void stop() {
        this.target = null;
        this.chargeTime = 0;
        this.hasSummonedCircle = true;
    }

    @Override
    public void tick() {
        SorcererObliteratorEntity obliterator = new SorcererObliteratorEntity(ModEntityTypes.OBLITERATOR.get(), boss.level);


        if (target == null || !target.isAlive()) {
            this.stop();
            return;
        }

        // Face the target
        this.boss.getLookControl().setLookAt(target.position());

        if (!hasSummonedCircle && chargeTime == 250) {
            // Render your arcane circle here

            // Example: ArcaneRenderer.spawnArcaneCircleAbove(strikePos);
            obliterator.setPos(target.getX(), target.getY() + 12, target.getZ());
            this.boss.level.addFreshEntity(obliterator); // your existing renderer
            hasSummonedCircle = true;
        }

        chargeTime++;

        if (chargeTime >= MAX_CHARGE_TIME) {
            // Fire the beam
            // Example: ArcaneRenderer.spawnBeamDown(strikePos);
            this.cachedTarget = this.boss.getTarget(); // Store target when attack starts

            if (cachedTarget != null && cachedTarget.isAlive()) {
                TrackingLaserBeam currentBeam = new TrackingLaserBeam(
                        this.boss.level,
                        obliterator,
                        () -> cachedTarget, // Use cached target instead of dynamic lookup
                        20,
                        20f
                );
            }

            // Damage everything in radius
            AxisAlignedBB area = new AxisAlignedBB(strikePos).inflate(DAMAGE_RADIUS);
            List<LivingEntity> entities = boss.level.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entities) {
                if (!entity.isInvulnerable() && entity != boss) {
                    entity.hurt(DamageSource.indirectMagic(boss, boss), DAMAGE_AMOUNT);
                }
            }

            // Reset so it only fires once per use
            this.stop();
        }
    }
}


