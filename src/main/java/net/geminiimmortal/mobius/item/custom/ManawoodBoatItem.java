package net.geminiimmortal.mobius.item.custom;

import net.geminiimmortal.mobius.entity.custom.ManawoodBoatEntity;
import net.geminiimmortal.mobius.entity.custom.ModBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ManawoodBoatItem extends BoatItem {
    private static final Predicate<Entity> ENTITY_PREDICATE;
    private final String woodType;

    public ManawoodBoatItem(Properties p_i48526_2_, String woodType) {
        super(null, p_i48526_2_);
        this.woodType = woodType;
    }

    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        ItemStack lvt_4_1_ = p_77659_2_.getItemInHand(p_77659_3_);
        RayTraceResult lvt_5_1_ = getPlayerPOVHitResult(p_77659_1_, p_77659_2_, RayTraceContext.FluidMode.ANY);
        if (((RayTraceResult)lvt_5_1_).getType() == RayTraceResult.Type.MISS) {
            return ActionResult.pass(lvt_4_1_);
        } else {
            Vector3d lvt_6_1_ = p_77659_2_.getViewVector(1.0F);
            double lvt_7_1_ = 5.0;
            List<Entity> lvt_9_1_ = p_77659_1_.getEntities(p_77659_2_, p_77659_2_.getBoundingBox().expandTowards(lvt_6_1_.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!lvt_9_1_.isEmpty()) {
                Vector3d lvt_10_1_ = p_77659_2_.getEyePosition(1.0F);
                Iterator var11 = lvt_9_1_.iterator();

                while(var11.hasNext()) {
                    Entity lvt_12_1_ = (Entity)var11.next();
                    AxisAlignedBB lvt_13_1_ = lvt_12_1_.getBoundingBox().inflate((double)lvt_12_1_.getPickRadius());
                    if (lvt_13_1_.contains(lvt_10_1_)) {
                        return ActionResult.pass(lvt_4_1_);
                    }
                }
            }

            if (((RayTraceResult)lvt_5_1_).getType() == RayTraceResult.Type.BLOCK) {
                ManawoodBoatEntity boatEntity = new ManawoodBoatEntity(p_77659_1_, ((RayTraceResult)lvt_5_1_).getLocation().x, ((RayTraceResult)lvt_5_1_).getLocation().y, ((RayTraceResult)lvt_5_1_).getLocation().z);
                boatEntity.setType(woodType);
                boatEntity.yRot = p_77659_2_.yRot;
                if (!p_77659_1_.noCollision(boatEntity, boatEntity.getBoundingBox().inflate(-0.1))) {
                    return ActionResult.fail(lvt_4_1_);
                } else {
                    if (!p_77659_1_.isClientSide) {
                        p_77659_1_.addFreshEntity(boatEntity);
                        if (!p_77659_2_.abilities.instabuild) {
                            lvt_4_1_.shrink(1);
                        }
                    }

                    p_77659_2_.awardStat(Stats.ITEM_USED.get(this));
                    return ActionResult.sidedSuccess(lvt_4_1_, p_77659_1_.isClientSide());
                }
            } else {
                return ActionResult.pass(lvt_4_1_);
            }
        }
    }

    static {
        ENTITY_PREDICATE = EntityPredicates.NO_SPECTATORS.and(Entity::isPickable);
    }
}

