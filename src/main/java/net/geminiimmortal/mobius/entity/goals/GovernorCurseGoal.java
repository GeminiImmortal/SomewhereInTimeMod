package net.geminiimmortal.mobius.entity.goals;

import net.geminiimmortal.mobius.entity.custom.GovernorEntity;
import net.geminiimmortal.mobius.effects.ModEffects;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.geminiimmortal.mobius.network.ModNetwork;
import net.geminiimmortal.mobius.network.GovernorCursePacket;

import java.util.EnumSet;
import java.util.List;

public class GovernorCurseGoal extends Goal {

    private final GovernorEntity governor;
    private int cooldown = 0;

    public GovernorCurseGoal(GovernorEntity governor) {
        this.governor = governor;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.cooldown-- <= 0 && this.governor.getTarget() != null && (this.governor.getHealth() < (this.governor.getMaxHealth() * 0.75));
    }

    @Override
    public void start() {
        this.cooldown = 400 + this.governor.getRandom().nextInt(100);
        ServerWorld world = (ServerWorld) this.governor.level;

        List<ServerPlayerEntity> players = world.getEntitiesOfClass(ServerPlayerEntity.class,
                this.governor.getBoundingBox().inflate(20.0D));

        for (ServerPlayerEntity player : players) {
            player.addEffect(new EffectInstance(ModEffects.EXPOSED_EFFECT.get(), 400, 1, false, true, true));
            player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 1, false, true, true));
            player.addEffect(new EffectInstance(ModEffects.LORD_DECREE_EFFECT.get(), 80, 1, false, true, true));

            world.playSound(null, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0F, 1.0F);

            ModNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new GovernorCursePacket(this.governor.getId()));
        }
    }
}
