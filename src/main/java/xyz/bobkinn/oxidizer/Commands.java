package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class Commands {

    public static void register(Main main) {
        var tool = new CommandAPICommand("tool")
                .withRequirement(permissionCheck("oxidizer.command.tool"))
                .withOptionalArguments(
                        new EntitySelectorArgument.OnePlayer("target")
                                .withRequirement(permissionCheck("oxidizer.command.tool.other"))
                )
                .executes((sender, args) -> {
                    var arg = (Player) args.getOptional("target").orElse(null);
                    Player target;
                    if (arg != null) {
                        if (!permissionCheck("oxidizer.command.tool.other").test(sender)) {
                            sender.sendMessage(main.i18n.get("command.only-player"));
                            return;
                        }
                        target = arg;
                    } else if (sender instanceof Player player) {
                        target = player;
                    } else {
                        sender.sendMessage(main.i18n.get("command.only-player"));
                        return;
                    }
                    main.handler.giveTool(sender, target);
                });

        var reload = new CommandAPICommand("reload")
                .withRequirement(permissionCheck("oxidizer.command.reload"))
                .executes((sender, args) -> {
                    main.reload();
                    sender.sendMessage(main.i18n.get("command.reload.success"));
                });

        new CommandAPICommand("oxidizer")
                .withRequirement(permissionCheck("oxidizer.command"))
                .withSubcommands(tool, reload)
                .register(main);
    }

    static Predicate<CommandSender> permissionCheck(String permission) {
        return sender -> sender.isOp() || sender.hasPermission(permission);
    }
}
