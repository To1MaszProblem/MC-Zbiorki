package pl.to1maszproblem.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.to1maszproblem.Main;
import pl.to1maszproblem.utils.ItemBuilder;
import pl.to1maszproblem.utils.TextUtil;

public class ZbiorkiMenu {

    public void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, Main.getInstance().getConfiguration().getMenuRows() * 9,
                TextUtil.fixColor(Main.getInstance().getConfiguration().getZbiorkiMenuTitle()));

        Main.getInstance().getConfiguration().getZbiorki().forEach(collection -> {
            int slot = collection.getSlot();
            if (slot >= 0) {
                ItemStack colItem = new ItemBuilder(collection.getItem())
                        .addLores(" ",
                                "&7Ilość zebranych przedmiotów: &f[collected]/[max-collection]",
                                " ",
                                "&7Kliknij &flewym&7 aby dodać jeden przedmiot!",
                                "&7Kliknij &fprawym&7 aby dodać więcej przedmiotów")
                        .replacePlaceholders("[collected]", String.valueOf(collection.getCollected()))
                        .replacePlaceholders("[max-collection]", String.valueOf(collection.getMaxCollection()))
                        .setName("&a" + collection.getName()).build();
                inventory.setItem(slot, colItem);
            }
        });

        Main.getInstance().getConfiguration().getItems().forEach(itemMenu -> {
            int slot = itemMenu.getSlot();
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, itemMenu.getItemStack());
            }
        });

        player.openInventory(inventory);
    }


    public static Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, Main.getInstance().getConfiguration().getMenuRows() * 9,
                TextUtil.fixColor(Main.getInstance().getConfiguration().getZbiorkiMenuTitle()));

        Main.getInstance().getConfiguration().getZbiorki().forEach(warp -> {
            inventory.setItem(warp.getSlot(), new ItemBuilder(warp.getItem().getType()).setName(warp.getName()).build());
        });

        Main.getInstance().getConfiguration().getItems().forEach(itemMenu ->
                inventory.setItem(itemMenu.getSlot(), itemMenu.getItemStack()));

        return inventory;
    }
}