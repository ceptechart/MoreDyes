package ianeli.moredyes.entity;

import ianeli.moredyes.MoreDyes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ColoredFallingBlockEntity> ColoredFallingBlock = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MoreDyes.MOD_ID, "colored_falling_block"),
            EntityType.Builder.create(ColoredFallingBlockEntity::new, SpawnGroup.MISC).dropsNothing()
                    .dimensions(0.98F, 0.98F).maxTrackingRange(10).trackingTickInterval(20)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MoreDyes.MOD_ID, "colored_falling_block")))
    );

    public static void initialize() {

    }
}