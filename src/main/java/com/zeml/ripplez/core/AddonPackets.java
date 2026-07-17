package com.zeml.ripplez.core;

import com.zeml.ripplez.RipplesAddon;
import com.zeml.ripplez.core.packets.server.DiscOutDataPacket;
import com.zeml.ripplez.core.packets.server.WhiteSnakeMusicDataPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class AddonPackets {
    public static void register(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");
        registerPacket(registrar,PayloadRegistrar::playToClient, new WhiteSnakeMusicDataPacket.Handler(RipplesAddon.resLoc("music")));
        registerPacket(registrar,PayloadRegistrar::playToClient, new DiscOutDataPacket.Handler(RipplesAddon.resLoc("disc_out")));


    }

    public static interface PacketHandler<T extends CustomPacketPayload> {
        CustomPacketPayload.Type<T> type();
        void handle(T payload, IPayloadContext context);
    }

    public static interface PacketOGHandler<T extends CustomPacketPayload> extends PacketHandler<T> {
        void encode(T packet, RegistryFriendlyByteBuf buf);
        T decode(RegistryFriendlyByteBuf buf);
    }

    public static interface PacketCodecHandler<T extends CustomPacketPayload> extends PacketHandler<T> {
        StreamCodec<? super RegistryFriendlyByteBuf, T> reader();
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
