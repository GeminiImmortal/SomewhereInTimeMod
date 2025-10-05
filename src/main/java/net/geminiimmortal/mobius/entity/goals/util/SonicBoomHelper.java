package net.geminiimmortal.mobius.entity.goals.util;


import net.geminiimmortal.mobius.damage.CloneShatterDamageSource;
import net.geminiimmortal.mobius.effects.ModEffects;
import net.geminiimmortal.mobius.particle.ModParticles;
import net.geminiimmortal.mobius.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SonicBoomHelper {
    /**
     * @param caster the entity casting the attack
     * @param target the target entity
     * @param world  the world in which to spawn particles
     */
    public static void doSonicBoom(LivingEntity caster, LivingEntity target, World world) {
        if (!(world instanceof ServerWorld)) return;
        ServerWorld serverWorld = (ServerWorld) world;

        Vector3d start = caster.position().add(0, caster.getEyeHeight() * 0.5, 0);
        Vector3d end = target.position().add(0, target.getEyeHeight() * 0.5, 0);
        Vector3d direction = end.subtract(start);
        double totalDist = direction.length();
        Vector3d unitDir = direction.normalize();

        int rings = 12;
        int segments = 1;
        double baseRadius = 0.1D;
        int durationTicks = 40;

        for (int i = 1; i <= rings; i++) {
            double fraction = (double) i / (rings + 1);
            Vector3d center = start.add(unitDir.scale(fraction * totalDist));
            double radius = baseRadius * i;

            for (int j = 0; j < segments; j++) {
                double angle = 1 * Math.PI * j / segments;
                double x = center.x + Math.cos(angle) * radius;
                double y = center.y;
                double z = center.z + Math.sin(angle) * radius;
                serverWorld.sendParticles(ModParticles.FAEDEER_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
            }
        }

        target.hurt(CloneShatterDamageSource.CLONE_SHATTER, 2.0F);

        target.addEffect(new EffectInstance(ModEffects.LORD_DECREE_EFFECT.get(), durationTicks, 1, false, false));
        target.level.playSound(null, target.blockPosition().getX(), target.blockPosition().getY(), target.blockPosition().getZ(), ModSounds.ARCANE_BOLT_FX.get(), SoundCategory.HOSTILE, 1.0f, 1.0f);
    }
}

