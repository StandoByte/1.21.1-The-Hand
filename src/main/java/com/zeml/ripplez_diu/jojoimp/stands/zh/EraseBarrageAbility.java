package com.zeml.ripplez_diu.jojoimp.stands.zh;

import com.github.standobyte.jojo.customobjects.DamageSourceModified;
import com.github.standobyte.jojo.init.ModSoundEvents;
import com.github.standobyte.jojo.powersystem.Power;
import com.github.standobyte.jojo.powersystem.PowerClass;
import com.github.standobyte.jojo.powersystem.ability.Ability;
import com.github.standobyte.jojo.powersystem.ability.AbilityId;
import com.github.standobyte.jojo.powersystem.ability.AbilityType;
import com.github.standobyte.jojo.powersystem.ability.AbilityUsageGroup;
import com.github.standobyte.jojo.powersystem.ability.condition.AvailableAbilities;
import com.github.standobyte.jojo.powersystem.entityaction.ActionOBB;
import com.github.standobyte.jojo.powersystem.entityaction.ActionPhase;
import com.github.standobyte.jojo.powersystem.entityaction.EntityActionInstance;
import com.github.standobyte.jojo.powersystem.entityaction.type.EntityActionType;
import com.github.standobyte.jojo.powersystem.standpower.StandPower;
import com.github.standobyte.jojo.powersystem.standpower.StandUtil;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandEntity;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandEntityAbility;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandStatFormulas;
import com.github.standobyte.jojo.subsystems.entity_grab.LivingComponentGrab;
import com.github.standobyte.jojo.subsystems.hitboxes.ExtendableOBB;
import com.github.standobyte.jojo.subsystems.hitboxes.OBBCollisionUtil;
import com.github.standobyte.jojo.subsystems.hitboxes.OrientedBoundingBox;
import com.github.standobyte.jojo.subsystems.target.ActionTarget;
import com.github.standobyte.jojo.util.functions.DamageUtil;
import com.github.standobyte.jojo.util.functions.MathUtil;
import com.github.standobyte.jojoimpl.stands._entitybase.StandEntityBarrageAbility;
import com.zeml.ripplez_diu.RipplesAddon;
import com.zeml.ripplez_diu.init.AddonDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;

public class EraseBarrageAbility extends StandEntityBarrageAbility {

    public EraseBarrageAbility(AbilityType<?> abilityType, AbilityId abilityId) {
        super(abilityType, abilityId);
        this.createActionObj = EraseBarrage::new;
        usageGroup = AbilityUsageGroup.COMBAT;
        setDefaultPhaseLength(ActionPhase.PERFORM, StandStatFormulas.getBarrageMaxDuration(8));
        setDefaultPhaseLength(ActionPhase.RECOVERY, 10);
        noFinisherBarDecay = true;
    }




    public static class EraseBarrage extends StandEntityBarrage implements ActionOBB {
        ExtendableOBB botox;
        public EraseBarrage(EntityActionType ability) {
            super(ability);
        }

        @Override
        public void actionPerformStart() {
            OrientedBoundingBox obb = new OrientedBoundingBox(new Vec3(0,performer.getBbHeight()/2,0), 3d, 3d, 3d, getPerformer().getYRot(), getPerformer().getXRot());
            this.botox =  new ExtendableOBB(obb,0,phasesLength.get(ActionPhase.PERFORM).intValue(),phasesLength.get(ActionPhase.PERFORM).intValue(),new Vec3(0, performer.getBbHeight()/2, 0));
        }

        @Override
        public void actionPerformEnd() {
            this.botox = null;
        }

        @Override
        public void actionTick() {
            super.actionTick();
            if(getPhase() == ActionPhase.PERFORM && this.extendableOBB() != null){
                this.extendableOBB().tick();
                Vec3 pos = getPerformer().position();
                Vec3 offset = new Vec3(0.0, 1.5, 0.2)
                        .yRot(-getPerformer().yBodyRot * MathUtil.DEG_TO_RAD);
                this.extendableOBB().updatePosition(level(), pos, offset, getPerformer().getXRot(), getPerformer().getYRot());
                List<? extends Entity> projectiles = OBBCollisionUtil.getEntitiesInOBB(level(), this.extendableOBB().rotatableHitbox(), entity ->  entity instanceof Projectile);
                for (Entity projectile: projectiles){
                    if(!level().isClientSide){
                        if(performer instanceof StandEntity stand){
                            StandUtil.broadcastSound((ServerLevel) level(), projectile.position(),
                                    ModSoundEvents.STAND_PUNCH_BARRAGE, true, stand.getUserPower(),
                                    stand.getSoundSource(),
                                    1F,
                                    1.8F - (float) stand.getAttackDamage() * 0.05F + stand.getRandom().nextFloat() * 0.2F);

                        }
                        projectile.remove(Entity.RemovalReason.KILLED);
                    }
                }

            }
        }

        @Override
        protected void hitEntity(ActionTarget target, Level level, StandEntity stand) {
            Entity targetEntity = target.getMainEntity();
            if (targetEntity instanceof LivingEntity targetLiving) {
                if(targetEntity instanceof StandEntity standEntity && standEntity.getUser() != null){
                    targetLiving = standEntity.getUser();
                }
                DamageSource dmgSource = makePunchDamageSource();
                ((DamageSourceModified) dmgSource).jojo_ripples$modifyKnockback(0, 0.1f);
                standEntityAttack(stand, targetLiving, dmgSource, erasureAmount(targetLiving,stand));
            }
        }

        @Override
        protected void hitBlock(ActionTarget target, Level level, StandEntity stand) {
            level.destroyBlock(target.getBlockPos(),false);
        }

        protected float erasureAmount(LivingEntity target, LivingEntity stand){
            float percent = (float) Math.clamp((stand.getBoundingBox().getSize())/(target.getBoundingBox().getSize()*20),0.005F,.04F);
            return percent*target.getMaxHealth();
        }

        @Override
        public DamageSource makePunchDamageSource() {
            var damageType = DamageUtil.type(performer.level(), AddonDamageTypes.ERASE);
            DamageSource dmgSource = new DamageSource(damageType, performer);
            return dmgSource;
        }

        @Override
        protected float getHitsPerTick(StandEntity stand) {
            float hitsPerSec = StandStatFormulas.getBarrageHitsPerSecond(stand.getAttackSpeed());
            float hitsPerTick = hitsPerSec / 20;
            int curTick = (curPhaseTick - 1) % 20 + 1; // 1~20
            float value = (hitsPerTick * curTick) - (int) (hitsPerTick * (curTick - 1));
            value *= 0.5f;
            return value;
        }

        @Override
        public ExtendableOBB extendableOBB() {
            return this.botox;
        }
    }

}
