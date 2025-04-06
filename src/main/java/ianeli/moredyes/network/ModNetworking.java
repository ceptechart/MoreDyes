package ianeli.moredyes.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModNetworking {
    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(
                ColorUpdatePayload.ID,
                ColorUpdatePayload.CODEC
        );
    }
}
