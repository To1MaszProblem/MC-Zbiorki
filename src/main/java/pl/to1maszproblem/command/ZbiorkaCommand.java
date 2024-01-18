package pl.to1maszproblem.command;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.to1maszproblem.Main;
import pl.to1maszproblem.menu.ZbiorkiMenu;
import pl.to1maszproblem.module.Collection;
import pl.to1maszproblem.utils.TextUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ZbiorkaCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) (new ZbiorkiMenu()).openInventory(player);

            if (!player.hasPermission("collection.list")) {
                TextUtil.sendMessage(player, "&8>> &cNie posiadasz uprawnień do tej komendy! &8(&4collection.list&8)");
                return false;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    TextUtil.sendMessage(player, "&fAktualne zbiory na serwerze:");
                    Main.getInstance().getConfiguration().getZbiorki().forEach(collection -> TextUtil.sendMessage(player, "&7- &f" + collection.getName() + " &7" + collection.getCollected() + "/" + collection.getMaxCollection()));
                }
                if (!player.hasPermission("collection.reload")) {
                    TextUtil.sendMessage(player, "&8>> &cNie posiadasz uprawnień do tej komendy! &8(&4collection.reload&8)");
                    return false;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    TextUtil.sendMessage(player, "&aPrzeładowano config!");
                    Main.getInstance().getConfiguration().load();
                }
            } else if (args.length == 2) {
                if (!player.hasPermission("collection.delete")) {
                    TextUtil.sendMessage(player, "&8>> &cNie posiadasz uprawnień do tej komendy! &8(&4collection.delete&8)");
                    return false;
                }

                if(args[0].equalsIgnoreCase("delete")) {
                    String name = args[1];
                    List<Collection> zbiorki = Main.getInstance().getConfiguration().getZbiorki();

                    Optional<Collection> collectionToRemove = zbiorki.stream()
                            .filter(collection -> collection.getName().equalsIgnoreCase(name))
                            .findFirst();
                    if (collectionToRemove.isPresent()) {
                        zbiorki.remove(collectionToRemove.get());
                        Main.getInstance().getConfiguration().save();
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getDeletedCollection().replace("[collection-name]", name));
                    } else {
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getCollectionDoesntExist());
                    }
                }
                if (!player.hasPermission("collection.setshard")) {
                    TextUtil.sendMessage(player, "&8>> &cNie posiadasz uprawnień do tej komendy! &8(&4collection.setshard&8)");
                    return false;
                }
                if (args[0].equalsIgnoreCase("setshard")) {
                    String name = args[1];
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() == Material.AIR) {
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getHoldItemToCreate());
                        return false;
                    }
                    if (Main.getInstance().getConfiguration().getZbiorki().stream().noneMatch(collection -> collection.getName().toLowerCase().equalsIgnoreCase(args[1].toLowerCase()))) {
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getCollectionDoesntExist());
                        return false;
                    }

                    Optional<Collection> foundCollection = Main.getInstance().getConfiguration().getZbiorki().stream()
                            .filter(collection -> collection.getName().equalsIgnoreCase(name))
                            .findFirst();

                    if (foundCollection.isPresent()) {
                        Collection collection = foundCollection.get();
                        collection.setItemCollecting(item);
                        TextUtil.sendMessage(player, "&7Ustawiono przedmiot do zbierania dla zbiurki &f" + collection.getName());
                    }
                }
            } else if (args.length == 3) {
                if (!player.hasPermission("collection.create")) {
                    TextUtil.sendMessage(player, "&8>> &cNie posiadasz uprawnień do tej komendy! &8(&4collection.create&8)");
                    return false;
                }

                if(args[0].equalsIgnoreCase("create")) {
                    String name = args[1];
                    ItemStack item = player.getInventory().getItemInMainHand();
                    int maxCollection = Integer.parseInt(args[2]);
                    if (item.getType() == Material.AIR) {
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getHoldItemToCreate());
                        return false;
                    }
                    if (Main.getInstance().getConfiguration().getZbiorki().stream().anyMatch(collection -> collection.getName().toLowerCase().equalsIgnoreCase(name))) {
                        TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getCollectionDoesntExist());
                        return false;
                    }

                    int slot = findSlot(ZbiorkiMenu.getInventory(player));
                    new AnvilGUI.Builder()
                            .text("say [player] test")
                            .title("Podaj komendę")
                            .plugin(Main.getInstance())
                            .itemLeft(new ItemStack(Material.PAPER))
                            .onClick((slot1, cmd) -> {
                                if (slot1 != AnvilGUI.Slot.OUTPUT) {
                                    return Collections.emptyList();
                                }

                                Main.getInstance().getConfiguration().getZbiorki().add(new Collection(name, maxCollection, 0, cmd.getText(), null, item, slot));
                                Main.getInstance().getConfiguration().save();
                                return List.of(AnvilGUI.ResponseAction.run(player::closeInventory));
                            }).open(player);
                    TextUtil.sendMessage(player, Main.getInstance().getMessageConfiguration().getCreatedCollection().replace("[collection-name]", name));
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("collection.admin")) return List.of("list", "reload", "delete", "setshard", "create");
        if (args.length == 2 && args[0].equalsIgnoreCase("setshard") && sender.hasPermission("collection.admin")) return Main.getInstance().getConfiguration().getZbiorki().stream().map(collection -> collection.getName().toLowerCase()).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("delete") && sender.hasPermission("collection.admin")) return Main.getInstance().getConfiguration().getZbiorki().stream().map(collection -> collection.getName().toLowerCase()).toList();
        return null;
    }
    private int findSlot(Inventory inventory) {
        return inventory.firstEmpty();
    }
}
