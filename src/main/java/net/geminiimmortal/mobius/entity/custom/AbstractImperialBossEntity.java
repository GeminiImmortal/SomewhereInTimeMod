package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.hook.MusicTickerHook;
import net.geminiimmortal.mobius.faction.FactionType;
import net.geminiimmortal.mobius.faction.IFactionCarrier;
import net.geminiimmortal.mobius.faction.IRankedImperial;
import net.geminiimmortal.mobius.world.dimension.ModDimensions;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class AbstractImperialBossEntity extends CreatureEntity implements IFactionCarrier, IRankedImperial {
    IFormattableTextComponent bossTitle;
    String bossName;

    protected AbstractImperialBossEntity(EntityType<? extends CreatureEntity> entityType, World worldIn) {
        super(entityType, worldIn);
        this.setPersistenceRequired();
        this.maxUpStep = 1;
        if (this.level.isClientSide() && this.isAlive()) {
            playBossMusic();
        }
    }

    private final ServerBossInfo bossInfo = new ServerBossInfo(
            getBossTitle(bossName),  // Boss name
            BossInfo.Color.PURPLE,
            BossInfo.Overlay.NOTCHED_20
    );

    private static final String[] FIRST_NAMES = {
            "Vorak", "Kel'dan", "Zaroth", "Morthas", "Draven", "Syrix", "Velkor", "Belethor", "Nazeem", "Alaric", "Oswin", "Berengar", "Wulfric", "Cedric"
    };

    private static final String[] LAST_NAMES = {
            "Skullsplitter", "Ironfist", "Blackthorn", "Duskwalker", "Stormrage", "Dawnfist", "Grimshaw", "Ravenshield", "Ironhand", "Redwyne", "Hollowmere", "Ashdown", "Stonehelm", "Steelbringer"
    };

    public static String generateBossName(Random rand) {
        String first = FIRST_NAMES[rand.nextInt(FIRST_NAMES.length)];
        String last = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];

        return first + " " + last;
    }

    @Nonnull
    public IFormattableTextComponent getBossTitle(String bossName) {
        if (this instanceof GovernorEntity) {
            return this.bossTitle = new StringTextComponent("Governor Cassian Moxley").setStyle(Style.EMPTY).withStyle(TextFormatting.GOLD);
        }
        return this.bossTitle = new StringTextComponent(bossName + " " + generateBossName(new Random())).setStyle(Style.EMPTY).withStyle(TextFormatting.DARK_RED);
    }

    @OnlyIn(Dist.CLIENT)
    private void playBossMusic() {
        if (this.level.dimension().equals(ModDimensions.MOBIUS_WORLD)) {
            MusicTickerHook.setMusicOverride(new BackgroundMusicSelector(setBossMusic(), 0, 0, true));
        }
    }

    public abstract SoundEvent setBossMusic();

    @OnlyIn(Dist.CLIENT)
    public void stopBossMusic() {
        MusicTickerHook.clearMusicOverride();
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        if (this.level.isClientSide()) {
            playBossMusic();
        }

        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void die(DamageSource source) {

        super.die(source);

        if (!this.level.isClientSide()) {
            int experiencePoints = this.getXpToDrop();

            while (experiencePoints > 0) {
                int experienceToDrop = experiencePoints;
                experiencePoints -= experienceToDrop;
                this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), experienceToDrop));
            }
        }
    }

    @Override
    public void tick() {

        super.tick();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

    }

    protected int getXpToDrop() {
        int baseXp = 50 + random.nextInt(20);
        return baseXp;
    }

    @Override
    public FactionType getFaction() {
        return FactionType.IMPERIAL;
    }

    @Override
    public Rank getRank() {
        return Rank.CHAMPION;
    }
}
