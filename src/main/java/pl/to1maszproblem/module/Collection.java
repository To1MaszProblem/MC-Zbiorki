package pl.to1maszproblem.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Collection implements Serializable {
    private String name;

    private int maxCollection;

    private int collected;

    private String command;

    private ItemStack itemCollecting;

    private ItemStack item;

    private int slot;

}