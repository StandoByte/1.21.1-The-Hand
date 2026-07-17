package com.zeml.ripplez.init;

import com.zeml.ripplez.RipplesAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AddonSoundEvents {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, RipplesAddon.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> ERASURE = SOUNDS.register("erasure", SoundEvent::createVariableRangeEvent);
}
