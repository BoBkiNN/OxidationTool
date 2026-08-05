package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class Commands {

    public static void register(Main main) {
        var give = new CommandAPICommand("give")
                .withRequirement(permissionCheck("oxidizer.command.give"))
                .withOptionalArguments(
                        new EntitySelectorArgument.OnePlayer("target")
                                .withRequirement(permissionCheck("oxidizer.command.give.other"))
                )
                .executes((sender, args) -> {
                    var arg = (Player) args.getOptional("target").orElse(null);
                    Player target;
                    if (arg != null) {
                        if (!permissionCheck("oxidizer.command.give.other").test(sender)) {
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
                .withSubcommands(give, reload)
                .register(main);
    }

    @Contract(pure = true)
    public static @NotNull Predicate<CommandSender> permissionCheck(String permission) {
        return sender -> sender.isOp() || sender.hasPermission(permission);
    }
}
