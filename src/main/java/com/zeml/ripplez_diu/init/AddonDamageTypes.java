package com.zeml.ripplez_diu.init;

import com.github.standobyte.jojo.core.JojoMod;
import com.github.standobyte.jojo.init.TagHelper;
import com.zeml.ripplez_diu.RipplesAddon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;


@EventBusSubscriber
public class AddonDamageTypes {
    public static final TagHelper<DamageType> DAMAGE_TYPES = new TagHelper<>(RipplesAddon.MOD_ID, Registries.DAMAGE_TYPE);

    public static final TagKey<DamageType> CAN_HURT_STANDS = TagKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("jojo", "can_hurt_stands"));
    public static final TagKey<DamageType> ARMOR_BREAK_COOLDOWN = TagKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("jojo_ripples", "armor_break_cooldown"));
    public static final TagKey<DamageType> ADDS_RESOLVE = TagKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("jojo_ripples", "adds_resolve"));


    public static final ResourceKey<DamageType> ERASE = DAMAGE_TYPES.withTags(RipplesAddon.resLoc("erase"),
            CAN_HURT_STANDS,
            ARMOR_BREAK_COOLDOWN,
            ADDS_RESOLVE,
            DamageTypeTags.BYPASSES_COOLDOWN,
            DamageTypeTags.BYPASSES_ENCHANTMENTS,
            DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS,
            DamageTypeTags.PANIC_CAUSES,
            DamageTypeTags.BYPASSES_EFFECTS,
            DamageTypeTags.BYPASSES_RESISTANCE,
            DamageTypeTags.BYPASSES_ARMOR,
            Tags.DamageTypes.IS_PHYSICAL);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), DAMAGE_TYPES.makeTagsDatagenProvider(event));
    }

}
