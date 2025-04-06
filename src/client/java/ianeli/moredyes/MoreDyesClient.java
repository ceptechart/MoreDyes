package ianeli.moredyes;

import ianeli.moredyes.Entity.ColoredFallingBlockEntityRenderer;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.entity.ModEntities;
import ianeli.moredyes.network.ColorUpdatePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorResolverRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class MoreDyesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CustomGlass, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CustomGlassPane, RenderLayer.getTranslucent());

		EntityRendererRegistry.register(ModEntities.ColoredFallingBlock, ColoredFallingBlockEntityRenderer::new);

		BlockColorProvider genericColorProvider = (state, view, pos, tintIndex) -> {
			if (view != null && view.getBlockEntityRenderData(pos) instanceof Integer integer) {
				if (integer != 0) {
					return integer;
				}
			}
			return 0x3F76E4;
		};

		BlockColorProvider rainbowColorProvider = (state, view, pos, tintIndex) -> {
			if (pos == null) return 0xFF0000;
			float hue = (float) (
					0.5 + 0.5 * Math.sin(pos.getX() * 0.1) +
					0.5 + 0.5 * Math.sin(pos.getZ() * 0.1)
			) % 1.0f;
			return Color.HSBtoRGB(hue, 1.0f, 1.0f);
		};

		BlockColorProvider terracottaProvider = (state, view, pos, tintIndex) -> {
			if (view != null && view.getBlockEntityRenderData(pos) instanceof Integer integer) {
				if (integer == 0) {
					return 0x3F76E4;
				}
				return ColorHandler.getTerraCotta(integer);
			}
			return 0xFFFFFF;
		};

		ClientPlayNetworking.registerGlobalReceiver(ColorUpdatePayload.ID, (payload, context) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			client.execute(()->{
				if (client.world != null) {
					BlockEntity be = client.world.getBlockEntity(payload.pos());
					if (be instanceof ColoredBlockEntity coloredBlockEntity) {
						coloredBlockEntity.setColor(payload.color());
						BlockPos pos = payload.pos();
						int radius = 1; // Adjust if needed
						client.worldRenderer.scheduleBlockRenders(
								pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
								pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
						);
					}
				}
			});
		});

		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomWool);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.DyeBasin);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomCarpet);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomConcrete);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomConcretePowder);
		ColorProviderRegistry.BLOCK.register(terracottaProvider, ModBlocks.CustomTerracotta);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomGlass);
		ColorProviderRegistry.BLOCK.register(genericColorProvider, ModBlocks.CustomGlassPane);
	}
}