package dev.kalonic.randomitems.commands;

import dev.kalonic.randomitems.Main;
import dev.kalonic.randomitems.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RandomItems implements CommandExecutor {

    private Main plugin;

    public RandomItems(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("randomitems").setExecutor(this);
    }


    private void invalidCommand(CommandSender sender) {
        sender.sendMessage(Utils.chat("&cInvalid Usage. Use /randomitems <start/stop>"));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(args.length == 1)) {
            invalidCommand(sender);
        } else if(args[0].equalsIgnoreCase("start")) {
            plugin.startTask();
            sender.sendMessage(Utils.chat("&aRandomItems Started."));
        } else if(args[0].equalsIgnoreCase("stop")) {
            plugin.stopTask();
            sender.sendMessage(Utils.chat("&cRandomItems Stopped."));
        } else {
            invalidCommand(sender);
        }

        return true;
    }
}
