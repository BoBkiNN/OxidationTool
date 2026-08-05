package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Commands {

    public static void register(Main main) {
        var tool = new CommandAPICommand("tool")
                .withRequirement(Main.checkPermission("oxidizer.command.tool"))
                .withOptionalArguments(
                        new EntitySelectorArgument.OnePlayer("target")
                                .withRequirement(Main.checkPermission("oxidizer.command.tool.other"))
                )
                .executes((sender, args) -> {
                    var arg = (Player) args.getOptional("target").orElse(null);
                    Player target;
                    if (arg != null) {
                        if (!Main.checkPermission("oxidizer.command.tool.other").test(sender)) {
                            return;
                        }
                        target = arg;
                    } else if (sender instanceof Player player) {
                        target = player;
                    } else {
                        sender.sendMessage(main.getI18n().get("command.only-player"));
                        return;
                    }
                    main.giveTool(sender, target);
                });

        var reload = new CommandAPICommand("reload")
                .withRequirement(Main.checkPermission("oxidizer.command.reload"))
                .executes((sender, args) -> {
                    main.reload();
                    sender.sendMessage(main.getI18n().get("command.reload.success"));
                });

        new CommandAPICommand("oxidizer")
                .withRequirement(Main.checkPermission("oxidizer.command"))
                .withSubcommands(tool, reload)
                .register(main);
    }
}
