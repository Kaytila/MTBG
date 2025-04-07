package net.ck.mtbg.ui.components;

import net.ck.mtbg.map.MapProperty;

import java.awt.*;
import java.util.Objects;

public interface LabeledEntryFieldFactory
{
    static LabeledEntryFieldFactory createDefault()
    {
        return property -> createDefault(property);
    }

    private static LabeledEntryField createDefault(final MapProperty property)
    {
        Objects.requireNonNull(property);

        if ((property.type() == null) || (property.name() == null) || (property.value() == null))
        {
            return null;
        }

        final var type = Objects.requireNonNull(property.type());
        final var name = Objects.requireNonNull(property.name());
        final var value = Objects.requireNonNull(property.value());

        // Point
        if (Point.class == type)
        {
            final var pointValue = (Point) value;
            return new LabeledEntryField(
                    String.valueOf(name),
                    "[x=%d,y=%d]".formatted(
                            pointValue.x,
                            pointValue.y
                    )
            );
        }

        // Enum
        if (type.isEnum())
        {
            final var enumValue = (Enum<?>) value;
            return new LabeledEntryField(
                    String.valueOf(name),
                    enumValue.name()
            );
        }

        // default
        if (int.class == type ||
                String.class == type
        )
        {
            return new LabeledEntryField(
                    String.valueOf(name),
                    String.valueOf(value)
            );
        }

        // not supported
        throw new IllegalArgumentException(
                "[%s] type not supported".formatted(type)
        );
    }

    LabeledEntryField create(
            MapProperty property);
}
