package xyz.bobkinn.oxidizer;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.slf4j.Logger;

import java.util.function.Supplier;

public final class OxidizerToolUtil {
    public static final NamespacedKey TOOL_MARKER = new NamespacedKey("oxidizer", "tool");
    private final Logger logger;
    private final I18n i18n;
    private final Supplier<ConfigurationSection> config;

    public OxidizerToolUtil(Logger logger, I18n i18n, Supplier<ConfigurationSection> config) {
        this.logger = logger;
        this.i18n = i18n;
        this.config = config;
    }

    private Material toolItem = Material.BREEZE_ROD;

    public void reload() {
        var key = config.get().getString("tool_item", "minecraft:breeze_rod");
        var m = Material.matchMaterial(key);
        if (m == null) {
            logger.warn("Failed to load tool material '{}'", key);
        }
        toolItem = m;
    }

    @SuppressWarnings("UnstableApiUsage")
    public ItemStack createTool() {
        var stack = new ItemStack(toolItem, 1);
        var name = i18n.get("tool.name");
        stack.setData(DataComponentTypes.ITEM_NAME, name);
        var lore = i18n.getList("tool.lore");
        if (!lore.isEmpty()) stack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));

        stack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        stack.setData(DataComponentTypes.RARITY, ItemRarity.EPIC);
        stack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

        stack.editPersistentDataContainer(pdc -> {
            pdc.set(TOOL_MARKER, PersistentDataType.BOOLEAN, true);
        });
        stack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData
                .customModelData()
                .addString(TOOL_MARKER.asString())
                .build());
        return stack;
    }

    public boolean isTool(ItemStack stack) {
        return stack.getPersistentDataContainer()
                .getOrDefault(TOOL_MARKER, PersistentDataType.BOOLEAN, false);
    }

}
