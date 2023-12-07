package me.tanluc.starfarm.command;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.ui.Gui;
import me.tanluc.starfarm.data.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cmd implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length >= 1) {
      if ((sender.isOp() || sender.hasPermission("starsfarm.reload")) && args[0].equals("reload")) {
        StarsFarm.getInstance().reloadConfig();
        sender.sendMessage(StarsFarm.messageManager.getReloadMsg());
      } else if (sender instanceof Player && args[0].equals("open")) {
        // Ensure the command has the necessary arguments
        if (args.length >= 2) {
          String guiName = args[1]; // Assuming args[1] contains the GUI name
          Gui.OpenMenu((Player) sender, guiName); // Pass the Player and GUI name to open the menu
        } else {
          // Handle if the command is missing arguments
          sender.sendMessage("Usage: /stf open <guiName>");
        }
      } else if (sender instanceof Player && args[0].equals("addHave")) {
        if (args.length >= 3) {
          String itemName = args[1]; // Assuming args[1] contains the item name
          int amount = Integer.parseInt(args[2]); // Assuming args[2] contains the amount

          Player player = (Player) sender;
          User user = User.of(player);

          user.addHave(itemName, amount);
          sender.sendMessage("Added " + amount + " " + itemName + " to your inventory.");
        } else {
          sender.sendMessage("Usage: /stf addHave <itemName> <amount>");
        }
      } else if (sender instanceof Player && args[0].equals("addSell")) {
        if (args.length >= 3) {
          String itemName = args[1]; // Assuming args[1] contains the item name
          int amount = Integer.parseInt(args[2]); // Assuming args[2] contains the amount

          Player player = (Player) sender;
          User user = User.of(player);

          user.addSell(itemName, amount);
          sender.sendMessage("Added " + amount + " " + itemName + " to your sell count.");
        } else {
          sender.sendMessage("Usage: /stf addSell <itemName> <amount>");
        }
      }
    }
    return true;
  }
}
