package xyz.bobkinn.oxidizer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.function.Supplier;

public record Translator(Supplier<ConfigurationSection> configSupplier) {

    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer
            .builder().character('&').extractUrls().hexColors().build();

    private static Component deserializeText(String text) {
        return SERIALIZER.deserialize(text);
    }

    private ConfigurationSection getConfig() {
        return configSupplier.get();
    }

    public Component getTranslationOrNull(String key) {
        var raw = getConfig().getString(key);
        if (raw == null || raw.isEmpty()) return null;
        return Translator.deserializeText(raw);
    }

    public List<Component> getTranslationList(String key) {
        if (getConfig().isString(key)) {
            var t = getTranslationOrNull(key);
            return t != null ? List.of(t) : List.of();
        }
        return getConfig().getStringList(key)
                .stream().map(Translator::deserializeText).toList();
    }

    public Component getTranslation(String key) {
        var c = getTranslationOrNull(key);
        return c != null ? c : Component.text(key);
    }
}
