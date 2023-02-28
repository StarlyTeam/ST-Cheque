package net.starly.cheque.command.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChequeTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            if (player.hasPermission("starly.cheque.create")) tab.add("뽑기");
            if (player.hasPermission("starly.cheque.reload")) tab.add("리로드");
            return tab;
        }
        return Collections.emptyList();
    }
}
