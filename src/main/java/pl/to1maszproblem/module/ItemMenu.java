package pl.to1maszproblem.module;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

@Getter
@Setter
public class ItemMenu implements Serializable {
    private ItemStack itemStack;

    private int slot;

    public ItemMenu(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

}
