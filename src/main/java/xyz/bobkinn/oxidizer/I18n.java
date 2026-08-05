package xyz.bobkinn.oxidizer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.function.Supplier;

public record I18n(Supplier<ConfigurationSection> config) {

    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer
            .builder().character('&').extractUrls().hexColors().build();

    private static Component deserializeText(String text) {
        return SERIALIZER.deserialize(text);
    }

    public Component getOrNull(String key) {
        var raw = config.get().getString(key);
        if (raw == null || raw.isEmpty()) return null;
        return I18n.deserializeText(raw);
    }

    public List<Component> getList(String key) {
        if (config.get().isString(key)) {
            var t = getOrNull(key);
            return t != null ? List.of(t) : List.of();
        }
        return config.get().getStringList(key)
                .stream().map(I18n::deserializeText).toList();
    }

    public Component get(String key) {
        var c = getOrNull(key);
        return c != null ? c : Component.text(key);
    }
}
