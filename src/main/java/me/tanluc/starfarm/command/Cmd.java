package me.tanluc.starfarm.command;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.fileManager.MessageManager;
import me.tanluc.starfarm.ui.Gui;
import me.tanluc.starfarm.data.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Cmd implements CommandExecutor, TabCompleter {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1) {
      for (String message : StarsFarm.messageManager.helpCmd()) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
      }
      return false;
    }

    if (args[0].equals("reload") && (sender.isOp() || sender.hasPermission("starsfarm.reload"))) {
      StarsFarm.getInstance().reloadConfig();
      MessageManager.reloadMessagesConfig();
      sender.sendMessage(StarsFarm.messageManager.getReloadMsg());
    } else if (sender instanceof Player) {
      Player player = (Player) sender;
      User user = User.of(player);

      if (args[0].equals("open")) {
        if (args.length >= 2) {
          String guiName = args[1];
          Gui.OpenMenu(player, guiName);
        } else {
          sender.sendMessage("Usage: /stf open <guiName>");
        }
      } else if (args[0].equals("add")) {
        if (args.length >= 4) {
          String itemName = args[2];
          int amount = Integer.parseInt(args[3]);

          if (args[1].equals("have")) {
            user.addHave(itemName, amount);
            sender.sendMessage(StarsFarm.messageManager.addHaveCmd().replace("{amount}", String.valueOf(amount)).replace("{item}", itemName));
          } else if (args[1].equals("sell")) {
            user.addSell(itemName, amount);
            sender.sendMessage(StarsFarm.messageManager.addSellCmd().replace("{amount}", String.valueOf(amount)).replace("{item}", itemName));
          } else {
            sender.sendMessage(StarsFarm.messageManager.addUsageCmd());
            return false;
          }
        } else {
          sender.sendMessage(StarsFarm.messageManager.addUsageCmd());
          return false;
        }
      }
      else if (args[0].equals("help")) {
        for (String message : StarsFarm.messageManager.helpCmd()) {
          sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
      }
    }

    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (args.length == 1) {
      List<String> completions = new ArrayList<>();
      completions.add("reload");
      completions.add("open");
      completions.add("add");
      completions.add("help");
      return completions;
    } else if (args.length == 2 && args[0].equals("add")) {
      List<String> completions = new ArrayList<>();
      completions.add("have");
      completions.add("sell");
      return completions;
    } else if (args.length == 3 && args[0].equals("add") && (args[1].equals("have") || args[1].equals("sell"))) {
      List<String> completions = new ArrayList<>();
      for (Material material : StarsFarm.materials) {
        completions.add(material.name());
      }
      return completions;
    } else if (args.length == 4 && args[0].equals("add") && (args[1].equals("have") || args[1].equals("sell"))) {
      List<String> completions = new ArrayList<>();
      for (int i = 1; i <= 5; i++) {
        completions.add(String.valueOf(i));
      }
      return completions;
    }
    return null;
  }
}
