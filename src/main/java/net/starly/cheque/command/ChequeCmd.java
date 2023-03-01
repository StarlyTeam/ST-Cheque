package net.starly.cheque.command;

import net.starly.cheque.ChequeMain;
import net.starly.cheque.context.MessageContent;
import net.starly.cheque.util.ChequeItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ChequeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noConsole"));
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "wrongCommand"));
            return false;
        }

        if (args[0].equals("뽑기")) {

            if (!player.hasPermission("starly.cheque.create")) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noPermission"));
                return false;
            }

            if (args.length == 1) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noMoney"));
                return false;
            }

            if (args.length == 2) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noAmount"));
                return false;
            }

            if (args.length != 3) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "wrongCommand"));
                return false;
            }

            long money;
            int amount;

            try {
                money = Long.parseLong(args[1]);
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noNumber"));
                return false;
            }

            if (money * amount < 1 || amount < 1) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "wrongNumber"));
                return false;
            }

            if (ChequeMain.getEconomy().getBalance(player) < money * amount) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "enoughMoney"));
                return false;
            }

            player.getInventory().addItem(ChequeItemUtil.getChequeItem(money, amount));
            ChequeMain.getEconomy().withdrawPlayer(player,money * amount);
            player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.NORMAL, "createCheque")
                    .replace("{money}", String.valueOf(money)));
            return true;
        }

        if (args[0].equals("리로드")) {

            if (!player.hasPermission("starly.cheque.create")) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "noPermission"));
                return false;
            }

            if (args.length != 1) {
                player.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.ERROR, "wrongCommand"));
                return false;
            }

            Plugin plugin = ChequeMain.getInstance();
            if (!new File("config.yml").exists()) plugin.saveDefaultConfig();
            plugin.reloadConfig();
            MessageContent.getInstance().initializing(plugin.getConfig());
            sender.sendMessage(MessageContent.getInstance().getMessageAfterPrefix(MessageContent.MessageType.NORMAL, "reloadConfig"));
            return true;
        }
        return false;
    }
}
