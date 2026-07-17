package com.zeml.ripplez.init.power;

import static com.github.standobyte.jojo.core.JojoRegistries.ABILITY_TYPES;

import com.github.standobyte.jojo.entityattachment.custom_effect.EntityCustomEffectType;
import com.github.standobyte.jojo.init.power.ModStandAbilities;
import com.github.standobyte.jojo.powersystem.ability.Ability;
import com.github.standobyte.jojo.powersystem.ability.AbilityType;

import com.zeml.ripplez.jojoimp.stands.white_snake.effect.DiscOutEffect;
import com.zeml.ripplez.jojoimp.stands.white_snake.effect.MemorylessEffect;
import com.zeml.ripplez.jojoimp.stands.white_snake.effect.MusicDisckedEffect;
import com.zeml.ripplez.jojoimp.stands.zh.*;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class AddonStandAbilities {
	public static void load() {}

	//Za Hando
	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASURE = ABILITY_TYPES.register(
			"erasure", key -> new AbilityType<>(key, EraseAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_FINISHER = ABILITY_TYPES.register(
			"erase_finisher", key -> new AbilityType<>(key, EraseFinisherAbility::new));
	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_SPACE = ABILITY_TYPES.register(
			"erase_space", key -> new AbilityType<>(key, EraseSpaceAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_BARRAGE = ABILITY_TYPES.register(
			"erase_barrage", key -> new AbilityType<>(key, EraseBarrageAbility::new));

	public static final DeferredHolder<AbilityType<?>, AbilityType<Ability>> ERASE_ITEMS = ABILITY_TYPES.register(
			"erase_items", key -> new AbilityType<>(key, EraseItemsAbility::new));

	//White Snake


	public static final DeferredHolder<EntityCustomEffectType<?>, EntityCustomEffectType<MusicDisckedEffect>> MUSIC_DISC_EFFECT = ModStandAbilities.STAND_EFFECT_TYPES.register(
			"music_disc", key -> new EntityCustomEffectType<>(key, MusicDisckedEffect::new));

	public static final DeferredHolder<EntityCustomEffectType<?>, EntityCustomEffectType<DiscOutEffect>> DISC_OUT_EFFECT = ModStandAbilities.STAND_EFFECT_TYPES.register(
			"disc_out", key -> new EntityCustomEffectType<>(key, DiscOutEffect::new));

	public static final DeferredHolder<EntityCustomEffectType<?>, EntityCustomEffectType<MemorylessEffect>> NO_MEMORY_EFFECT = ModStandAbilities.STAND_EFFECT_TYPES.register(
			"no_memory", key -> new EntityCustomEffectType<>(key, MemorylessEffect::new));
}