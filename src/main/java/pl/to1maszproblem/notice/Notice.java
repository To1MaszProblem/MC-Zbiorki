package pl.to1maszproblem.notice;

import com.google.common.collect.ImmutableMultimap;
import lombok.Getter;
import org.bukkit.entity.Player;
import pl.to1maszproblem.utils.TextUtil;

import java.util.Map;

@Getter
public class Notice {

    private final NoticeType type;
    private final String message;
    private final boolean replaceCapitals = false;
    private ImmutableMultimap<Object, Object> placeholders;

    public Notice(NoticeType type, String message) {
        this.type = type;
        this.message = message;
    }

    public void send(Player player) {
        String message = replaceString(this.message);
        switch (type) {
            case TITLE:
                TextUtil.sendTitle(player, message, " ");
                break;

            case SUBTITLE:
                TextUtil.sendTitle(player, " ", message);
                break;
            case MESSAGE:
                TextUtil.sendMessage(player, message);
                break;
            case ACTIONBAR:
                TextUtil.sendActionBar(player, message);
                break;
        }
    }

    public Notice addPlaceholder(ImmutableMultimap<Object, Object> placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    private String replaceString(String string) {
        if (placeholders == null) return string;

        for (Map.Entry<Object, Object> entry : placeholders.entries())
            string = string.replace(entry.getKey().toString(), entry.getValue().toString());
        return string;
    }
}