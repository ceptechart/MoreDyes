package ianeli.moredyes.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.HashMap;

public class DyeVial extends Item {

    public static HashMap<DyedColorComponent, String> PureColors = new HashMap<>();

    public DyeVial(Item.Settings settings) {
        super(settings.recipeRemainder(ModItems.DyeVial));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        DyedColorComponent c = stack.get(DataComponentTypes.DYED_COLOR);
        String name = PureColors.get(c);
        return name != null;
    }


    public Text getName(ItemStack stack) {
        DyedColorComponent c = stack.get(DataComponentTypes.DYED_COLOR);
        String name = PureColors.get(c);
        if (name != null) {
            return Text.of(name);
        } else {
            return super.getName(stack);
        }
    }

    static {
        PureColors.put(new DyedColorComponent(0xFF0000), "§r§cRed Dye");
        PureColors.put(new DyedColorComponent(0xFFFF00), "§r§eYellow Dye");
        PureColors.put(new DyedColorComponent(0x00FF00), "§r§aGreen Dye");
        PureColors.put(new DyedColorComponent(0x00FFFF), "§r§bCyan Dye");
        PureColors.put(new DyedColorComponent(0x0000FF), "§r§9Blue Dye");
        PureColors.put(new DyedColorComponent(0xFF00FF), "§r§dMagenta Dye");
        PureColors.put(new DyedColorComponent(0xFFFFFF), "§r§fWhite Dye");
        PureColors.put(new DyedColorComponent(0x101010), "§r§8Black Dye");
    }
}
