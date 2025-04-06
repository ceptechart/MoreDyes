package ianeli.moredyes.network;

import ianeli.moredyes.MoreDyes;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ColorUpdatePayload(BlockPos pos, int color) implements CustomPayload {
    public static final CustomPayload.Id<ColorUpdatePayload> ID = new CustomPayload.Id<>(Identifier.of(MoreDyes.MOD_ID, "color_update"));

    public ColorUpdatePayload(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt());
    }

    public static final PacketCodec<RegistryByteBuf, ColorUpdatePayload> CODEC =
            PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, // Serializer for BlockPos
                    ColorUpdatePayload::pos,
                    PacketCodecs.INTEGER,  // Serializer for int (color)
                    ColorUpdatePayload::color,
                    ColorUpdatePayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
