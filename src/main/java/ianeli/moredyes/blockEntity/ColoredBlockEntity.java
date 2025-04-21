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
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
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
    public ColoredBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GenericColoredBlock, pos, state);
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
        return getColor();
    }

    public int getColor() {
        ComponentMap cmap = getComponents();
        DyedColorComponent dc =  cmap.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(0));
        return dc.rgb();
    }
    public void setColor(int col) {
        ComponentMap cmap = getComponents();
        ComponentMap.Builder builder = ComponentMap.builder();

        builder.addAll(cmap);
        builder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(col));
        setComponents(builder.build());

        markDirty();
        syncColorToClients();
    }

    public void syncColorToClients() {
        if (world instanceof ServerWorld serverWorld) {
            ColorUpdatePayload payload = new ColorUpdatePayload(pos, getColor());

            for (ServerPlayerEntity player : PlayerLookup.tracking(this)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }
}

