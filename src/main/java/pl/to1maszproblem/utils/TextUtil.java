package pl.to1maszproblem.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
    public static String fixColor(String string) {
        Pattern pattern = Pattern.compile("&(#[a-fA-F0-9]{6})");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start() + 1, matcher.end());
            string = string.replace("&" + color, String.valueOf(ChatColor.of(color)));
        }
        return ChatColor.translateAlternateColorCodes('&', string)
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("->", "→")
                .replace("<-", "←")
                .replace("**", "•");
    }

    public static void sendMessage(CommandSender player, String message) {
        player.sendMessage(fixColor(message));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(fixColor(title), fixColor(subtitle), 10, 70, 20);
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(fixColor(message)));
    }

    public static void sendLogger(String message) {
        Bukkit.getLogger().info(fixColor(message));
    }
}