package ianeli.moredyes.blockEntity;

import ianeli.moredyes.MoreDyes;
import ianeli.moredyes.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static <T extends BlockEntityType<?>> T register(String name, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MoreDyes.MOD_ID, name), blockEntityType);
    }

    public static final BlockEntityType<ColoredBlockEntity> GenericColoredBlock = register(
            "colorblock_generic",
            FabricBlockEntityTypeBuilder.create(ColoredBlockEntity::new, ModBlocks.CustomWool, ModBlocks.DyeBasin, ModBlocks.CustomCarpet,
                    ModBlocks.CustomConcrete, ModBlocks.CustomConcretePowder, ModBlocks.CustomTerracotta, ModBlocks.CustomGlass,
                    ModBlocks.CustomGlassPane).build()
    );

    public static void initialize() {

    }
}
