package pl.to1maszproblem.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

@Getter
@Setter
public class ItemBuilder {
    private List lore;
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(ItemStack item) {
        this.lore = new ArrayList();
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material mat) {
        this(mat, (int)1);
    }

    public ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short)0);
    }

    public ItemBuilder(Material mat, short data) {
        this(mat, 1, data);
    }

    public ItemBuilder(Material mat, int amount, short data) {
        this.lore = new ArrayList();
        this.item = new ItemStack(mat, amount, data);
        this.meta = this.item.getItemMeta();
    }
    public ItemBuilder setPlayerHead(String playerName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        item.setItemMeta(meta);
        return new ItemBuilder(item);
    }
    public ItemBuilder addLores(String... lores) {
        for (String lore : lores) this.lore.add(TextUtil.fixColor(lore));
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add(TextUtil.fixColor(lore));
        return this;
    }
    public ItemBuilder replacePlaceholders(String placeholder, String replacement) {
        List<String> newLore = new ArrayList<>();
        Iterator<String> iterator = this.lore.iterator();
        while (iterator.hasNext()) {
            String lore = iterator.next();
            newLore.add(lore.replace(placeholder, replacement));
        }
        this.lore = newLore;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        this.meta.removeEnchant(enchant);
        this.meta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String title) {
        this.meta.setDisplayName(TextUtil.fixColor(title));
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (!this.item.getType().name().contains("LEATHER_")) {
            throw new IllegalArgumentException("Can only dye leather armor!");
        } else {
            ((LeatherArmorMeta)this.meta).setColor(color);
            return this;
        }
    }

    public ItemBuilder setGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        return this;
    }

    public ItemStack build() {
        if (!this.lore.isEmpty()) {
            this.meta.setLore(this.lore);
        }

        this.item.setItemMeta(this.meta);
        return this.item;
    }

    public Object getItemMeta() {
        return null;
    }
}
