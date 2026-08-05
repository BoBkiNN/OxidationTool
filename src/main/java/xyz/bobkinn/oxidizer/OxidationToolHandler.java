package xyz.bobkinn.oxidizer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class OxidationToolHandler implements Listener {
    private final Logger logger;
    private final I18n i18n;

    private boolean cycle = false;
    private boolean deoxidize = true;
    private Material toolMaterial = Material.BREEZE_ROD;

    public OxidationToolHandler(Logger logger, I18n i18n) {
        this.logger = logger;
        this.i18n = i18n;
    }

    public void reload(ConfigurationSection config) {
        this.cycle = config.getBoolean("tool.cycle_oxidation", false);
        this.deoxidize = config.getBoolean("tool.deoxidize", true);

        var mKey = config.getString("tool.material", "minecraft:breeze_rod");
        var m = Material.matchMaterial(mKey);
        if (m != null) {
            toolMaterial = m;
        } else {
            logger.warn("Failed to find tool material '{}'", mKey);
            toolMaterial = Material.BREEZE_ROD;
        }
    }

    public void giveTool(CommandSender sender, @NotNull Player player) {
        var tool = OxidationToolItem.create(toolMaterial, i18n);
        var failed = player.getInventory().addItem(tool);
        if (!failed.isEmpty()) {
            sender.sendMessage(i18n.get("command.tool.failed"));
        } else {
            sender.sendMessage(i18n.get("command.tool.success"));
        }
    }

    private boolean canUseTool(Player player) {
        return player.isOp() || player.hasPermission("oxidizer.use");
    }


    @EventHandler
    public void onBlockInteract(@NotNull PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (!canUseTool(player)) {
            return;
        }
        var item = event.getItem();
        if (item == null) return;
        var block = event.getClickedBlock();
        if (block == null) return;
        if (!OxidationToolItem.is(item)) return;

        boolean forward = !deoxidize || event.getAction().isRightClick();

        boolean success;
        try {
            success = oxidize(block, forward);
        } catch (Exception e) {
            logger.error("Failed to oxidize block {}; forward: {}", block.getLocation().toVector(), forward, e);
            var msg = i18n.getOrNull("on-fail");
            if (msg != null) player.sendMessage(msg);
            return;
        }
        if (!success) return;
        if (event.getAction().isRightClick()) {
            var hand = event.getHand();
            player.swingHand(hand != null ? hand : EquipmentSlot.HAND);
        }
        event.setCancelled(true);
    }

    private @Nullable Material nextType(Material material, boolean forward) {
        var delta = forward ? 1 : -1;
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
     * @param block target block
     * @param forward true if next oxidation phase, else previous
     * @return if changed
     */
    private boolean changeOxidation(Block block, boolean forward) {
        var cur = block.getType();
        var next = nextType(cur, forward);
        if (next == null) return false;

        var newData = next.createBlockData();
        var prevData = block.getState().getBlockData();
        prevData.copyTo(newData);
        block.setBlockData(newData, false);
        return true;
    }

    /**
     * Changes oxidation phase of block, or multiple blocks if target block is only part
     * @param block target block
     * @param forward true if next oxidation phase, else previous
     * @return true if any block changed
     */
    public boolean oxidize(Block block, boolean forward) {
        var o1 = changeOxidation(block, forward);
        var state = block.getBlockData();
        // handle doors
        if (state instanceof Bisected bis) {
            var dy = bis.getHalf() == Bisected.Half.BOTTOM ? 1 : -1;
            var other = block.getRelative(0, dy, 0);
            var o2 = changeOxidation(other, forward);
            return o2 || o1;
        }
        return o1;
    }
}
