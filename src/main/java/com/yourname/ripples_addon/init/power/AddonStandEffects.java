package com.yourname.ripples_addon.init.power;

import com.github.standobyte.jojo.core.JojoRegistries;

import com.github.standobyte.jojo.powersystem.standpower.effect.StandEffectType;
import com.yourname.ripples_addon.RipplesAddon;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AddonStandEffects {
	public static final DeferredRegister<StandEffectType<?>> STAND_EFFECT_TYPES = DeferredRegister.create(JojoRegistries.STAND_EFFECTS_REG, RipplesAddon.MOD_ID);

}