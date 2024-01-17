package pl.to1maszproblem.notice;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class NoticeSerializer implements ObjectSerializer<Notice> {
    public boolean supports(Class<? super Notice> type) {
        return Notice.class.isAssignableFrom(type);
    }

    public void serialize(@NonNull Notice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getType());
        data.add("message", object.getMessage());
    }

    public Notice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new Notice(
                data.get("type", NoticeType.class),
                data.get("message", String.class)
        );
    }
}