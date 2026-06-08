package com.zeml.ripplez_diu.init.power;

import static com.github.standobyte.jojo.core.JojoRegistries.ABILITY_TYPES;

import com.github.standobyte.jojo.powersystem.ability.Ability;
import com.github.standobyte.jojo.powersystem.ability.AbilityType;

import com.zeml.ripplez_diu.jojoimp.stands.zh.EraseAbility;
import com.zeml.ripplez_diu.jojoimp.stands.zh.EraseBarrageAbility;
import com.zeml.ripplez_diu.jojoimp.stands.zh.EraseItemsAbility;
import com.zeml.ripplez_diu.jojoimp.stands.zh.EraseSpaceAbility;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class AddonStandAbilities {
	public static void load() {}

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASURE = ABILITY_TYPES.register(
			"erasure", key -> new AbilityType<>(key, EraseAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_SPACE = ABILITY_TYPES.register(
			"erase_space", key -> new AbilityType<>(key, EraseSpaceAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_BARRAGE = ABILITY_TYPES.register(
			"erase_barrage", key -> new AbilityType<>(key, EraseBarrageAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_ITEMS = ABILITY_TYPES.register(
			"erase_items", key -> new AbilityType<>(key, EraseItemsAbility::new));
}