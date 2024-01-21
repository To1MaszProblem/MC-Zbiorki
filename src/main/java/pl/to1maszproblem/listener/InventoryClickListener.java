package pl.to1maszproblem.listener;

import com.google.common.collect.ImmutableMultimap;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.to1maszproblem.Main;
import pl.to1maszproblem.menu.ZbiorkiMenu;
import pl.to1maszproblem.module.Collection;
import pl.to1maszproblem.utils.TextUtil;

import java.util.Collections;
import java.util.List;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null || !event.getView().getTitle().equals(TextUtil.fixColor(Main.getInstance().getConfiguration().getZbiorkiMenuTitle()))) {
            return;
        }

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        Main.getInstance().getConfiguration().getZbiorki().forEach(collection -> {
            if (clickedItem != null && clickedItem.isSimilar(collection.getItem()) && event.isLeftClick()) {
                handleLeftClick(player, collection);
            } else if (clickedItem != null && clickedItem.isSimilar(collection.getItem()) && event.isRightClick()) {
                handleRightClick(player, collection);
            }
        });
    }

    private void handleLeftClick(Player player, Collection collection) {
        ItemStack itemCollecting = collection.getItemCollecting();
        if (itemCollecting == null) {
            Main.getInstance().getMessageConfiguration().getDoesntSetedCollectionItem().send(player);
            return;
        }

        if (hasItem(player, itemCollecting)) {
            player.getInventory().removeItem(itemCollecting);
            collection.setCollected(collection.getCollected() + itemCollecting.getAmount());

            if (collection.getCollected() >= collection.getMaxCollection()) {
                completeCollection(collection, player);
            }
            Main.getInstance().getMessageConfiguration().getPayCollection()
                    .addPlaceholder(ImmutableMultimap.of(
                            "[collection-name]", collection.getName(),
                            "[amount]", "1")).send(player);
        }
    }

    private void handleRightClick(Player player, Collection collection) {
        player.closeInventory();
        new AnvilGUI.Builder()
                .text("1")
                .title("Podaj liczbę przedmiotów")
                .plugin(Main.getInstance())
                .itemLeft(new ItemStack(Material.PAPER))
                .onClick((slot1, liczba) -> {
                    if (slot1 != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    int amount = Integer.parseInt(liczba.getText());
                    ItemStack itemToCheck = collection.getItemCollecting();

                    if (hasMultipleItems(player, itemToCheck, amount)) {
                        player.getInventory().removeItem(itemToCheck);
                        collection.setCollected(collection.getCollected() + amount);
                        if (collection.getCollected() >= collection.getMaxCollection()) {
                            completeCollection(collection, player);
                        }
                        Main.getInstance().getConfiguration().save();

                        Main.getInstance().getMessageConfiguration().getPayCollection()
                                .addPlaceholder(ImmutableMultimap.of(
                                        "[collection-name]", collection.getName(),
                                        "[amount]", String.valueOf(amount))).send(player);
                    } else {
                        Main.getInstance().getMessageConfiguration().getDoesntHaveCollectionItem().send(player);
                    }

                    return List.of(AnvilGUI.ResponseAction.run(player::closeInventory));
                }).open(player);
    }

    private boolean hasItem(Player player, ItemStack itemStack) {
        return player.getInventory().containsAtLeast(itemStack, itemStack.getAmount());
    }

    private boolean hasMultipleItems(Player player, ItemStack itemToCheck, int amount) {
        int totalAmount = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.isSimilar(itemToCheck)) totalAmount += itemStack.getAmount();
        }
        return totalAmount >= amount;
    }
    //Main.getInstance().getMessageConfiguration().getDoesntHaveCollectionItem().send(player);

    private void completeCollection(Collection collection, Player player) {
        Bukkit.getOnlinePlayers().forEach(online -> {
            Main.getInstance().getMessageConfiguration().getCollectionEnd()
                    .addPlaceholder(ImmutableMultimap.of("[collection-name]", collection.getName()))
                    .send(online);
            Bukkit.getServer().dispatchCommand(online, collection.getCommand().replace("[player]", online.getName()));
        });
        collection.setCollected(0);
        Main.getInstance().getConfiguration().save();
        player.closeInventory();
        new ZbiorkiMenu().openInventory(player);
    }
}
