package com.zeml.ripplez_diu.init.power.stands;

import com.github.standobyte.jojo.init.power.ModStandAbilities;
import com.github.standobyte.jojo.powersystem.MovesetBuilder;
import com.github.standobyte.jojo.powersystem.ability.controls.InputKey;
import com.github.standobyte.jojo.powersystem.ability.controls.InputMethod;
import com.github.standobyte.jojo.powersystem.entityaction.ActionPhase;
import com.github.standobyte.jojo.powersystem.standpower.StandStats;
import com.github.standobyte.jojo.powersystem.standpower.StandUnlockableSkill;
import com.github.standobyte.jojo.powersystem.standpower.entity.EntityStandType;
import com.zeml.ripplez_diu.init.power.AddonStandAbilities;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import static com.github.standobyte.jojo.init.power.ModStands.SWITCH_SPECIAL;
import static com.github.standobyte.jojo.init.power.ModStands.USE_SPECIAL;

public class StandInitZaHando {

    @ApiStatus.Internal
    public static EntityStandType create(ResourceLocation id){
        return new EntityStandType(
                new StandStats.Builder()
                        .power(13.5)
                        .speed(12)
                        .range(3,7.5)
                        .durability(9)
                        .precision(8.5)
                        .build(),
                new MovesetBuilder()

                        .addHumanoidStandStuff()


                        .addAbility("punch", ModStandAbilities.PUNCH)
                        .addAbility("punch2", ModStandAbilities.PUNCH)
                        .addAbility("punch3", ModStandAbilities.PUNCH)
                        .addAbility("punch4", ModStandAbilities.PUNCH, punch -> {
                            punch.setDefaultPhaseLength(ActionPhase.WINDUP, 5);
                        })
                        .addAbility("heavy_punch", ModStandAbilities.HEAVY_PUNCH)
                        .addAbility("erasure", AddonStandAbilities.ERASURE)
                        .addAbility("erase_finisher",AddonStandAbilities.ERASE_FINISHER, punch->{
                            punch.initIsFinisher();
                        })
                        .addAbility("erase_space", AddonStandAbilities.ERASE_SPACE)

                        .addAbility("barrage", AddonStandAbilities.ERASE_BARRAGE)

                        .addAbility("grab_barrage", ModStandAbilities.BARRAGE)

                        .addAbility("erase_items",AddonStandAbilities.ERASE_ITEMS.get())

                        .makeControlScheme("hotbar")
                        .bind("punch", InputMethod.CLICK, InputKey.LMB)
                        .bind("barrage", InputMethod.HOLD, InputKey.LMB)
                        .bind("heavy_punch", InputMethod.CLICK, InputKey.RMB)
                        .bind("erasure",InputMethod.HOLD,InputKey.RMB)
                        .bind("erase_space",InputMethod.HOLD,InputKey.RMB.withModifier(InputKey.Modifier.CONTROL))

                        .bind("erase_items",InputMethod.HOLD,InputKey.C)



                        .finalizeControlScheme()


                        .addSkill(StandUnlockableSkill.startingAbility("punch"))
                        .addSkill(StandUnlockableSkill.unlockableAbility("barrage",500).prerequisiteSkill("erasure"))
                        .addSkill(StandUnlockableSkill.startingAbility("heavy_punch"))
                        .addSkill(StandUnlockableSkill.unlockableAbility("erase_finisher",100).prerequisiteSkill("erasure","erase_items"))
                        .addSkill(StandUnlockableSkill.unlockableAbility("erase_items",10))
                        .addSkill(StandUnlockableSkill.unlockableAbility("erasure",300))
                        .addSkill(StandUnlockableSkill.unlockableAbility("erase_space",200).prerequisiteSkill("erasure"))

                , id);

    }
}
