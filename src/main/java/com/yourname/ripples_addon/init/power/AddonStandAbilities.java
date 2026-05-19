package com.yourname.ripples_addon.init.power;

import static com.github.standobyte.jojo.core.JojoRegistries.ABILITY_TYPES;

import com.github.standobyte.jojo.powersystem.ability.Ability;
import com.github.standobyte.jojo.powersystem.ability.AbilityType;

import net.neoforged.neoforge.registries.DeferredHolder;

public final class AddonStandAbilities {
	public static void load() {}

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> _PLACEHOLDER = ABILITY_TYPES.register(
			"test_placeholder", key -> new AbilityType<>(key, Ability::new));
}