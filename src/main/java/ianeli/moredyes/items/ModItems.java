package ianeli.moredyes.items;

import ianeli.moredyes.MoreDyes;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static Item registerNewItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemRegistryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MoreDyes.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemRegistryKey));
        Registry.register(Registries.ITEM, itemRegistryKey, item);
        return item;
    }

    public static final Item DyeVial = registerNewItem("dyevial", Item::new, new Item.Settings().maxCount(16));
    public static final Item DyeVialFilled = registerNewItem("dyevial_filled", ianeli.moredyes.items.DyeVial::new, new Item.Settings().maxCount(16));

    public static void initialize() {

    }
}