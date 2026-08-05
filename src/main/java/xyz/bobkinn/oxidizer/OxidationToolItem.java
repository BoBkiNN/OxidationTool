package xyz.bobkinn.oxidizer;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public final class OxidationToolItem {
    public static final NamespacedKey TOOL_MARKER = new NamespacedKey("oxidizer", "tool");

    @SuppressWarnings("UnstableApiUsage")
    public static ItemStack create(Material base, I18n i18n) {
        var stack = new ItemStack(base, 1);
        var name = i18n.get("tool.name");
        stack.setData(DataComponentTypes.ITEM_NAME, name);
        var lore = i18n.getList("tool.lore");
        if (!lore.isEmpty()) stack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));

        stack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        stack.setData(DataComponentTypes.RARITY, ItemRarity.EPIC);
        stack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

        stack.editPersistentDataContainer(pdc ->
                pdc.set(TOOL_MARKER, PersistentDataType.BOOLEAN, true));
        stack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData
                .customModelData()
                .addString(TOOL_MARKER.asString())
                .build());
        return stack;
    }

    public static boolean is(ItemStack stack) {
        return stack.getPersistentDataContainer()
                .getOrDefault(TOOL_MARKER, PersistentDataType.BOOLEAN, false);
    }

}
