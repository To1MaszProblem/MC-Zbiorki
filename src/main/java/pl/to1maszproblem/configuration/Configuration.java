package pl.to1maszproblem.configuration;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.to1maszproblem.module.Collection;
import pl.to1maszproblem.module.ItemMenu;
import pl.to1maszproblem.utils.ItemBuilder;

import java.util.Arrays;
import java.util.List;

@Getter
public class Configuration extends OkaeriConfig {

    private int menuRows = 3;
    private String zbiorkiMenuTitle = "&7Zbiorki Menu";

    private List<Collection> zbiorki = Arrays.asList(
            new Collection("klucze",
                    200,
                    0,
                    "say [player] dostał klucze",
                    new ItemStack(Material.IRON_INGOT),
                    new ItemBuilder(Material.TRIPWIRE_HOOK).setName("&aKlucze")
                            .addLores(" ", "&7Ilość zebranych przedmiotów: &f[collected]/[max-collection]", " ", "&7Kliknij &flewym&7 aby dodać jeden przedmiot!", "&7Kliknij &fprawym&7 aby dodać więcej przedmiotów").build(),
                    11),
            new Collection("turbodrop",
                    300,
                    0,
                    "say [player] dostał turbodrop",
                    new ItemStack(Material.DIAMOND),
                    new ItemBuilder(Material.DIAMOND_PICKAXE).setName("&cTurbo&fDrop")
                            .addLores(" ", "&7Ilość zebranych przedmiotów: &f[collected]/[max-collection]", " ", "&7Kliknij &flewym&7 aby dodać jeden przedmiot!", "&7Kliknij &fprawym&7 aby dodać więcej przedmiotów").build(),
                    15));
    private List<ItemMenu> items = Arrays.asList(
            new ItemMenu(new ItemBuilder(Material.PAPER)
                    .setName("&cJak działają zbiorki?")
                    .addLores(" ", "&7Wybierz zbiórkę i wpłać na nią itemy a cały serwer dostanie nagrodę!").build(), 13));
}
