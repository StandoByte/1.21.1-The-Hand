package com.zeml.ripplez_diu.jojoimp.stands.zh;

import com.github.standobyte.jojo.customobjects.DamageSourceModified;
import com.github.standobyte.jojo.init.ModDamageTypes;
import com.github.standobyte.jojo.init.ModSoundEvents;
import com.github.standobyte.jojo.powersystem.ability.AbilityId;
import com.github.standobyte.jojo.powersystem.ability.AbilityType;
import com.github.standobyte.jojo.powersystem.ability.AbilityUsageGroup;
import com.github.standobyte.jojo.powersystem.entityaction.ActionOBB;
import com.github.standobyte.jojo.powersystem.entityaction.ActionPhase;
import com.github.standobyte.jojo.powersystem.entityaction.EntityActionInstance;
import com.github.standobyte.jojo.powersystem.entityaction.type.EntityActionType;
import com.github.standobyte.jojo.powersystem.standpower.StandPower;
import com.github.standobyte.jojo.powersystem.standpower.StandUtil;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandEntity;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandEntityAbility;
import com.github.standobyte.jojo.powersystem.standpower.entity.StandStatFormulas;
import com.github.standobyte.jojo.subsystems.hitboxes.ExtendableOBB;
import com.github.standobyte.jojo.subsystems.hitboxes.OBBCollisionUtil;
import com.github.standobyte.jojo.subsystems.hitboxes.OrientedBoundingBox;
import com.github.standobyte.jojo.subsystems.target.ActionTarget;
import com.github.standobyte.jojo.subsystems.target.AimingEntity;
import com.github.standobyte.jojo.subsystems.target.HitResultUtil;
import com.github.standobyte.jojo.util.functions.DamageUtil;
import com.github.standobyte.jojo.util.functions.MathUtil;
import com.github.standobyte.jojoimpl.stands._entitybase.StandEntityHeavyPunchAbility;
import com.github.standobyte.jojoimpl.stands._entitybase.StandEntityHeavyPunchChargedAbility;
import com.github.standobyte.jojoimpl.stands._entitybase.StandEntityPunchAbility;
import com.zeml.ripplez_diu.RipplesAddon;
import com.zeml.ripplez_diu.init.AddonDamageTypes;
import com.zeml.ripplez_diu.init.AddonSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;

public class EraseAbility extends StandEntityHeavyPunchChargedAbility {

    public EraseAbility(AbilityType<?> abilityType, AbilityId abilityId) {
        super(abilityType, abilityId);
        this.createActionObj = ErasingAbility::new;
        usageGroup = AbilityUsageGroup.COMBAT;
        setDefaultPhaseLength(ActionPhase.BUTTON_CHARGE, 21);
        setButtonHoldPhase(ActionPhase.WINDUP);
        setDefaultPhaseLength(ActionPhase.PERFORM, 7);
        setDefaultPhaseLength(ActionPhase.RECOVERY, 12);

    }

    @Override
    public void initActionFromConfig(EntityActionInstance action, Level level,
                                     LivingEntity powerUser, LivingEntity performer) {
        super.initActionFromConfig(action, level, powerUser, performer);
        if (!level.isClientSide() && performer instanceof StandEntity stand) {
            action.phasesLength.put(ActionPhase.BUTTON_CHARGE, StandStatFormulas.getChargedHeavyButtonWindup(
                    stand.getAttackSpeed(), stand.getFinisherMeter()));
            action.phasesLength.put(ActionPhase.PERFORM, StandStatFormulas.getChargedHeavyPunchWindup(
                    stand.getAttackSpeed(), stand.getFinisherMeter()));
        }
    }

    public static class ErasingAbility extends StandEntityChargedHeavy implements ActionOBB {
        private ExtendableOBB eobb;

        public ErasingAbility(EntityActionType ability) {
            super(ability);
        }

        @Override
        public void actionTick() {
            super.actionTick();
            if(this.extendableOBB() != null){
                this.extendableOBB().tick();
                Vec3 pos = getPerformer().position();
                Vec3 offset = new Vec3(0.0, 1.5, 0.2)
                        .yRot(-getPerformer().yBodyRot * MathUtil.DEG_TO_RAD);
                this.extendableOBB().updatePosition(level(), pos, offset, getPerformer().getXRot(), getPerformer().getYRot());
            }
        }

        @Override
        public void actionPerformEnd() {
            Level level = level();
            if (performer instanceof StandEntity stand) {
                ActionTarget target = HitResultUtil.clipEntityLook(stand, entity -> StandEntityPunchAbility.canStandHit(stand, entity), 0);
                if (!level.isClientSide()) {
                    StandPower standPower = StandPower.get(getPowerUser());

                    StandUtil.broadcastSound((ServerLevel) level, stand.position(),
                            AddonSoundEvents.ERASURE, true, standPower,
                            stand.getSoundSource(), 1, 1);
                    DamageSource dmgSource = makePunchDamageSource();
                    float dmgAmount = StandStatFormulas.getChargedHeavyAttackDamage(stand.getAttackDamage());
                    if(target.getType() == ActionTarget.TargetType.ENTITY &&  target.getMainEntity() instanceof  LivingEntity livingEntity){
                        dmgAmount = erasureAmount(livingEntity,stand);
                    }
                    float explRadius = 0;


                    switch (target.getType()) {
                        case ENTITY -> hitEntity(target, level, stand, dmgSource, dmgAmount, explRadius);
                        case BLOCK -> hitBlock(target, level, stand, dmgSource, dmgAmount, explRadius);
                        default -> {
                            LivingEntity relative = stand.getUser() != null? stand.getUser():stand;
                            if(standPower.getCurTypeData() != null && standPower.getCurTypeData().isSkillUnlocked("erase_space")){
                                List<? extends Entity> entities = OBBCollisionUtil.getEntitiesInOBB(level(), eobb.rotatableHitbox(), entity -> entity != getPerformer() && entity != getPowerUser() && entity instanceof LivingEntity);

                                if(entities.isEmpty()){
                                    //TODO bring falling sand, rn not in the mood
                                    /*
                                    Vec3 finalPos = this.extendableOBB().rotatableHitbox().center.add(getPerformer().getLookAngle().scale(extendableOBB().rotatableHitbox().extent.length()));
                                    BlockHitResult result = level().clip(new ClipContext(extendableOBB().rotatableHitbox().center, finalPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));
                                    BlockState blockCollision = OBBCollisionUtil.getCollidingBlock(level(), result.getBlockPos());
                                    if (blockCollision != null){
                                        FallingBlockEntity block = FallingBlockEntity.fall(level,new BlockPos(result.getBlockPos()),blockCollision);
                                        Vec3 move = block.position().subtract(relative.position()).normalize();
                                        block.noPhysics = true;
                                        block.setDeltaMovement(move.scale(3));
                                        if(!level.isClientSide){
                                            level.addFreshEntity(block);
                                        }

                                    }*/
                                }else {
                                    Entity closest = entities.getFirst();
                                    for (Entity entity: entities){
                                        if(entity.distanceTo(relative)<closest.distanceTo(relative)){
                                            closest = entity;
                                        }
                                    }
                                    Vec3 position = eobb.rotatableHitbox().center.add(relative.getLookAngle().scale(0.5));
                                    if(!level.isClientSide){
                                        closest.teleportTo(position.x,stand.getUser().getY(),position.z);
                                    }
                                    eobb = null;
                                }
                            }
                        }
                    }

                    //punchedTarget = target;
                    standPower.consumeStamina(100);
                    stand.consumeFinisherMeter(1.0001f);
                }
                if (target.getType() == ActionTarget.TargetType.ENTITY) {
                    standRotationTarget = target;
                }
                else {
                    aimAs = AimingEntity.CAMERA_ENTITY;
                }
            }
        }

        protected void addKnockback(DamageSource dmgSource) {
            DamageSourceModified knockback = (DamageSourceModified) dmgSource;
            knockback.jojo_ripples$modifyKnockback(0, 0);
        }

        @Override
        public void actionPerformStart() {
            super.actionPerformStart();
            LivingEntity relative = performer;
            if(performer instanceof StandEntity stand && stand.getUser() != null){
                relative = stand.getUser();
            }
            OrientedBoundingBox obb = new OrientedBoundingBox(new Vec3(0,relative.getBbHeight()/2,0), 1.5d, 2d, 13d, relative.getYRot(), relative.getXRot());
            this.eobb = new ExtendableOBB(obb,0,phasesLength.get(ActionPhase.PERFORM).intValue(),phasesLength.get(ActionPhase.PERFORM).intValue(),new Vec3(0, relative.getBbHeight()/2, 0));

        }

        @Override
        public DamageSource makePunchDamageSource() {
            var damageType = DamageUtil.type(performer.level(), AddonDamageTypes.ERASE);
            DamageSource dmgSource = new DamageSource(damageType, performer);
            return dmgSource;
        }

        @Override
        protected void hitEntity(ActionTarget target, Level level, StandEntity stand, DamageSource dmgSource, float dmgAmount, float explRadius) {
            Entity targetEntity = target.getMainEntity();
            if (targetEntity instanceof LivingEntity targetLiving) {
                if(targetEntity instanceof StandEntity standEntity && standEntity.getUser() != null){
                    targetLiving = standEntity.getUser();
                }
                addKnockback(dmgSource);
                standEntityAttack(stand, targetLiving, dmgSource, dmgAmount);
                if(!level.isClientSide){
                    if(stand.getUserPower() != null){
                        StandUtil.broadcastSound((ServerLevel) level, stand.position(),
                                AddonSoundEvents.ERASURE, true, stand.getUserPower(),
                                stand.getSoundSource(), 1, 1);
                        stand.getUserPower().addExp(10);
                    }
                }
            }
        }

        @Override
        protected void hitBlock(ActionTarget target, Level level, StandEntity stand, DamageSource dmgSource, float dmgAmount, float explRadius) {
            level.destroyBlock(target.getBlockPos(),false);
            if(!level.isClientSide){
                if(stand.getUserPower() != null){
                    StandUtil.broadcastSound((ServerLevel) level, stand.position(),
                            AddonSoundEvents.ERASURE, true, stand.getUserPower(),
                            stand.getSoundSource(), 1, 1);
                    stand.getUserPower().addExp(10);
                }
            }
        }

        public static float erasureAmount(LivingEntity target, LivingEntity stand){
            if(target.getBoundingBox().getSize()<stand.getBoundingBox().getSize() && target.getMaxHealth() < stand.getMaxHealth()){
                return target.getMaxHealth();
            }
            float percent = (float) Math.clamp((stand.getBoundingBox().getSize())/(target.getBoundingBox().getSize()),0.1F,.8F);
            return percent*target.getMaxHealth();
        }

        @Override
        public ExtendableOBB extendableOBB() {
            return eobb;
        }
    }

}
