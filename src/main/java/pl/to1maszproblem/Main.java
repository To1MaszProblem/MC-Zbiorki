package pl.to1maszproblem;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.to1maszproblem.command.ZbiorkaCommand;
import pl.to1maszproblem.configuration.Configuration;
import pl.to1maszproblem.configuration.MessageConfiguration;
import pl.to1maszproblem.listener.InventoryClickListener;
import pl.to1maszproblem.module.serializer.CollectionSerializer;
import pl.to1maszproblem.notice.NoticeSerializer;

public final class Main extends JavaPlugin {

    @Getter public static Main instance;
    @Getter private Configuration configuration;
    @Getter private MessageConfiguration messageConfiguration;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getCommand("zbiorki").setExecutor(new ZbiorkaCommand());
        configuration = ConfigManager.create(Configuration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(this.getDataFolder() + "/config.yml");
            it.withSerdesPack(registry -> registry.register(new CollectionSerializer()));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        messageConfiguration = ConfigManager.create(MessageConfiguration.class, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit());
            it.withBindFile(this.getDataFolder() + "/messages.yml");
            it.withSerdesPack(registry -> registry.register(new NoticeSerializer()));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }
}
