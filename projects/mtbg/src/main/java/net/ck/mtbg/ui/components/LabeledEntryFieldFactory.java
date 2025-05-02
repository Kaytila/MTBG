package net.ck.mtbg.ui.components;

import net.ck.mtbg.backend.entities.entities.NPCProperty;
import net.ck.mtbg.map.MapProperty;

import java.awt.*;
import java.util.Objects;

public interface LabeledEntryFieldFactory
{
    static LabeledEntryField createDefault(Object prop)
    {
        if (prop instanceof MapProperty)
        {
            return createDefault((MapProperty) prop);
        }
        if (prop instanceof NPCProperty)
        {
            return createDefault((NPCProperty) prop);
        }
        return null;
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

    private static LabeledEntryField createDefault(final NPCProperty property)
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

        if (boolean.class == type)
        {
            return new LabeledEntryField(
                    String.valueOf(name),
                    type.toString()
            );
        }


        // not supported
        throw new IllegalArgumentException(
                "[%s] type not supported".formatted(type)
        );
    }

    LabeledEntryField create(MapProperty property);

    LabeledEntryField create(NPCProperty property);
}
