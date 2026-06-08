package com.zeml.ripplez_diu.init.power;

import com.github.standobyte.jojo.core.JojoRegistries;
import com.github.standobyte.jojo.powersystem.playerpower.PlayerPowerType;
import com.zeml.ripplez_diu.RipplesAddon;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AddonPlayerPowers {
	public static final DeferredRegister<PlayerPowerType<?>> PLAYER_POWERS = DeferredRegister.create(JojoRegistries.PLAYER_POWER_TYPES_REG, RipplesAddon.MOD_ID);
	
//	public static final Supplier<HmmmPowerType> HMMM = HmmmPowerType.HMMM;
}
