package net.geminiimmortal.mobius.item.custom;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VorpalSword extends SwordItem {
    public VorpalSword(IItemTier itemTier, int damage, float speed, Properties properties) {
        super(itemTier, damage, speed, properties);
    }

    IFormattableTextComponent vorpalSwordAbility = new TranslationTextComponent("tooltip.mobius.vorpal_sword").setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.GOLD));

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(vorpalSwordAbility);
    }
}

