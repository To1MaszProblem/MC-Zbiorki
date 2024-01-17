package pl.to1maszproblem.module.serializer;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import pl.to1maszproblem.module.Collection;

public class CollectionSerializer implements ObjectSerializer<Collection> {
    @Override
    public boolean supports(@NonNull Class<? super Collection> type) {
        return Collection.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull Collection object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("maxCollection", object.getMaxCollection());
        data.add("collected", object.getCollected());
        data.add("command", object.getCommand());
        data.add("itemCollecting", object.getItemCollecting());
        data.add("item", object.getItem());
        data.add("slot", object.getSlot());
    }

    @Override
    public Collection deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        String name = data.get("name", String.class);
        Integer maxCollection = data.get("maxCollection", Integer.class);
        Integer collected = data.get("collected", Integer.class);
        String command = data.get("command", String.class);
        ItemStack itemCollecting = data.get("itemCollecting", ItemStack.class);
        ItemStack item = data.get("item", ItemStack.class);
        Integer slot = data.get("slot", Integer.class);

        return new Collection(name, maxCollection, collected, command, itemCollecting, item, slot);
    }
}