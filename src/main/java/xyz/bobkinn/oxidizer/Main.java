package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public final I18n i18n;
    public final OxidationToolHandler handler;

    public Main() {
        i18n = new I18n(this::getMessagesSection);
        handler = new OxidationToolHandler(getSLF4JLogger(), i18n);
    }

    private ConfigurationSection getMessagesSection() {
        return getConfig().getConfigurationSection("messages");
    }

    public void reload() {
        var dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getSLF4JLogger().warn("Failed to create data folder");
        }
        saveDefaultConfig();
        reloadConfig();
        handler.reload(getConfig());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        reload();
        getServer().getPluginManager().registerEvents(handler, this);
        Commands.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(handler);
        CommandAPI.unregister("oxidizer");
    }

}
