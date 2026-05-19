package com.yourname.ripples_addon;

import com.github.standobyte.jojo.core.PacketsRegister.PacketCodecHandler;
import com.github.standobyte.jojo.core.PacketsRegister.PacketOGHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Register custom packets here.
 * You can see examples in {@link com.github.standobyte.jojo.core.PacketsRegister} from the base mod.
 */
public class PacketsAddonRegister {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Some examples from the base mod, uncomment and modify as needed:

        // Server packets:
        // registerPacket(registrar, PayloadRegistrar::playToServer, new ClAbilityInputPacket.Handler(JojoMod.resLoc("clkey")));

        // Client packets:
        // registerPacket(registrar, PayloadRegistrar::playToClient, new DatapackStandsPacket.Handler(JojoMod.resLoc("datastands")));
    }

    private static <T extends CustomPacketPayload> void registerPacket(PayloadRegistrar registrar, PacketType packetType, PacketOGHandler<T> handler) {
        packetType.register(registrar, handler.type(), StreamCodec.ofMember(handler::encode, handler::decode), handler::handle);
    }

    private static <T extends CustomPacketPayload> void registerPacket(PayloadRegistrar registrar, PacketType packetType, PacketCodecHandler<T> handler) {
        packetType.register(registrar, handler.type(), handler.reader(), handler::handle);
    }

    @FunctionalInterface
    private static interface PacketType {
        <T extends CustomPacketPayload> void register(PayloadRegistrar registrar,
              CustomPacketPayload.Type<T> type,
              StreamCodec<? super RegistryFriendlyByteBuf, T> reader,
              IPayloadHandler<T> handler);
    }
}
