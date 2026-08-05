package xyz.bobkinn.oxidizer;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Player;

public class Commands {

    public static void register(Main main) {
        var tool = new CommandAPICommand("tool")
                .withRequirement(Main.checkPermission("oxidizer.command.tool"))
                .withOptionalArguments(
                        new EntitySelectorArgument.OnePlayer("target")
                                .withRequirement(Main.checkPermission("oxidizer.command.tool.other"))
                                .executes(((sender, args) -> {
                                    var target = (Player) args.get("target");
                                    if (target != null) main.giveTool(sender, target);
                        }))
                )
                .executesPlayer(((player, args) -> {
                    main.giveTool(player, player);
                }));
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
