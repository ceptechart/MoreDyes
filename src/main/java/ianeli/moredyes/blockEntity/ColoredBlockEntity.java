package ianeli.moredyes.blockEntity;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.network.ColorUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ColoredBlockEntity extends BlockEntity {
    public int color = 0;

    public ColoredBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GenericColoredBlock, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        nbt.putInt("color", color);
        super.writeNbt(nbt, wrapper);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapper) {
        super.readNbt(nbt, wrapper);
        color = nbt.getInt("color").orElse(0);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public @Nullable Object getRenderData() {
        return color;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int col) {
        color = col;
        markDirty();
        syncColorToClients();
    }

    private void syncColorToClients() {
        if (world instanceof ServerWorld serverWorld) {
            ColorUpdatePayload payload = new ColorUpdatePayload(pos, color);

            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }
}

