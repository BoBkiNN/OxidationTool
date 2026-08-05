package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public final class Main extends JavaPlugin implements Listener {
    private final Translator i18n;
    private final OxidizerToolUtil toolUtil;

    public Main() {
        i18n = new Translator(this::getMessagesSection);
        toolUtil = new OxidizerToolUtil(getSLF4JLogger(), i18n, this::getConfig);
    }

    static Predicate<CommandSender> checkPermission(String permission) {
        return sender -> sender.isOp() || sender.hasPermission(permission);
    }

    public Translator getI18n() {
        return i18n;
    }

    private ConfigurationSection getMessagesSection() {
        return getConfig().getConfigurationSection("messages");
    }


    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        var dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getSLF4JLogger().warn("Failed to create data folder");
        }
        toolUtil.reload();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        reload();
        Commands.register(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll((Plugin) this);
        CommandAPI.unregister("oxidizer");
    }

    public void giveTool(CommandSender sender, @NotNull Player player) {
        var tool = toolUtil.createTool();
        var failed = player.getInventory().addItem(tool);
        if (!failed.isEmpty()) {
            sender.sendMessage(i18n.getTranslation("command.tool.failed"));
        } else {
            sender.sendMessage(i18n.getTranslation("command.tool.success"));
        }
    }


    @EventHandler
    public void onBlockInteract(@NotNull PlayerInteractEvent event) {
        if (!checkPermission("oxidizer.use").test(event.getPlayer())) {
            return;
        }
       var item = event.getItem();
       if (item == null) return;
       var block = event.getClickedBlock();
       if (block == null) return;
       if (!toolUtil.isTool(item)) return;

       var deoxidizer = getConfig().getBoolean("deoxidizer", true);
       boolean forward = !deoxidizer || event.getAction().isRightClick();

        boolean success;
        try {
            success = oxidize(block, forward);
        } catch (Exception e) {
            getSLF4JLogger().error("Failed to oxidize block {}; forward: {}", block.getLocation().toVector(), forward, e);
            var msg = i18n.getTranslationOrNull("on-fail");
            if (msg != null) event.getPlayer().sendMessage(msg);
            return;
        }
        if (!success) return;
        if (event.getAction().isRightClick()) {
            var hand = event.getHand();
            event.getPlayer().swingHand(hand != null ? hand : EquipmentSlot.HAND);
        }
        event.setCancelled(true);
    }

    private @Nullable Material nextType(Material material, boolean forward) {
        var delta = forward ? 1 : -1;
        var cycle = getConfig().getBoolean("cycle", false);
        for (List<Material> stages : CopperStages.TYPES) {
            var idx = stages.indexOf(material);
            if (idx < 0) continue;

            int nextIdx;
            if (cycle) {
                nextIdx = Math.floorMod(idx + delta, stages.size());
            } else {
                nextIdx = Math.max(Math.min(idx + delta, stages.size()-1), 0);
            }
            return stages.get(nextIdx);
        }
        return null;
    }

    /**
     *
     * @param block target block
     * @param forward true if next oxidation stage, else previous
     * @return if changed
     */
    public boolean oxidize0(Block block, boolean forward) {
        var cur = block.getType();
        var next = nextType(cur, forward);
        if (next == null) return false;

        var newData = next.createBlockData();
        var prevData = block.getState().getBlockData();
        prevData.copyTo(newData);
        block.setBlockData(newData, false);
        return true;
    }

    public boolean oxidize(Block block, boolean forward) {
        var o1 = oxidize0(block, forward);
        var state = block.getBlockData();
        // handle doors
        if (state instanceof Bisected bis) {
            var d = bis.getHalf() == Bisected.Half.BOTTOM ? 1 : -1;
            var other = block.getRelative(0, d, 0);
            var o2 = oxidize0(other, forward);
            return o2 || o1;
        }
        return o1;
    }
}
