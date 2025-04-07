package net.ck.mtbg.map;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Example with pure reflection and type comfort ( not recommended )
 */
public class MapAnnotationVariantWithComfort
{
    public static void main(
            final String[] args)
    {
        System.out.println("MapAnnotationVariantWithComfort.main");

        final var gameMap = createGameMap();
        final var entryFieldFactory = createEntryFieldFactory();

        propertiesOf(gameMap).stream()
                .map(entryFieldFactory::create)
                .forEach(lef -> System.out.printf(
                        "%s -> %s%n",
                        lef.getName(),
                        lef.getValue()
                ));
    }

    private static EntryFieldFactory createEntryFieldFactory()
    {
        return EntryFieldFactory.createDefault();
    }

    private static Object createGameMap()
    {
        final var gameMap = new GameMap();

        gameMap.weatherRandomness = 2;
        gameMap.visibilityRange = 42;
        gameMap.timePerTurn = new TimePerTurn(4, TimeUnit.MINUTES);
        gameMap.weather = Weather.CLOUDY;
        gameMap.size = new Point(42, 69);

        return gameMap;
    }

    private static Collection<GameMapProperty> propertiesOf(
            final Object gameMap)
    {
        final var result = new ArrayList<GameMapProperty>();

        try
        {

            // if a faster invoke is required consider using ...
            // final var lookup = java.lang.invoke.MethodHandles.lookup();
            // final var varHandle = lookup.unreflectVarHandle(field);
            // varHandle...

            for (final var field : gameMap.getClass().getDeclaredFields())
            {

                final var propertyField = field.getAnnotation(MapPropertyField.class);
                if (propertyField == null)
                {
                    continue;
                }

                final var hasNoAccess = !field.canAccess(gameMap);

                if (hasNoAccess)
                {
                    field.setAccessible(true);
                }

                result.add(new GameMapPropertyImpl(
                        field.getName(),
                        field.get(gameMap),
                        field.getType()
                ));

                if (hasNoAccess)
                {
                    field.setAccessible(false);
                }
            }
        }
        catch (final Throwable throwable)
        {
            if (throwable instanceof RuntimeException rte)
            {
                throw rte;
            }
            throw new RuntimeException(throwable);
        }

        return result;
    }

    private enum Weather
    {
        SHINY,
        CLOUDY,
        RAINY,
        NORMAL
    }

    private interface EntryFieldFactory
    {
        static EntryFieldFactory createDefault()
        {
            return EntryFieldFactory::createDefault;
        }

        private static LabeledEntryField createDefault(
                final GameMapProperty property)
        {
            Objects.requireNonNull(property);

            final var type = Objects.requireNonNull(property.type());
            final var name = Objects.requireNonNull(property.name());
            final var value = Objects.requireNonNull(property.value());

            // Point
            if (Point.class == type)
            {
                final var aPoint = (Point) value;
                return new LabeledEntryField(
                        String.valueOf(name),
                        "[x=%d,y=%d]".formatted(
                                aPoint.x,
                                aPoint.y
                        )
                );
            }

            // Enum
            if (type.isEnum())
            {
                final var anEnum = (Enum<?>) value;
                return new LabeledEntryField(
                        String.valueOf(name),
                        anEnum.name()
                );
            }

            // TimePerTurn
            if (type == TimePerTurn.class)
            {
                final var timePerTurn = (TimePerTurn) value;
                final var unit = timePerTurn.unit();

                return new LabeledEntryField(
                        String.valueOf(name),
                        "%d [%s]".formatted(
                                timePerTurn.value(),
                                switch (unit)
                                {
                                    case DAYS -> "days";
                                    case HOURS -> "hours";
                                    case MINUTES -> "min";
                                    case SECONDS -> "sec";
                                    case MILLISECONDS -> "ms";
                                    case MICROSECONDS -> "mi";
                                    case NANOSECONDS -> "ns";
                                }
                        )
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
                    "[%s] type not supported".formatted(property.type())
            );
        }

        LabeledEntryField create(
                GameMapProperty property);
    }

    private interface GameMapProperty
    {
        Object name();

        Class<?> type();

        Object value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface MapPropertyField
    {

    }

    private static final class GameMap
    {
        @MapPropertyField
        private int weatherRandomness;
        @MapPropertyField
        private int visibilityRange;
        @MapPropertyField
        private TimePerTurn timePerTurn;
        @MapPropertyField
        private Weather weather;
        @MapPropertyField
        private Point size;
    }

    private record GameMapPropertyImpl(
            Object name,
            Object value,
            Class<?> type
    )
            implements GameMapProperty
    {
    }

    private static final class LabeledEntryField
    {
        private String name;
        private String value;

        public LabeledEntryField()
        {
        }

        public LabeledEntryField(
                final String name,
                final String value)
        {
            setName(name);
            setValue(value);
        }

        public String getName()
        {
            return name;
        }

        public void setName(
                final String name)
        {
            this.name = name;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(
                final String value)
        {
            this.value = value;
        }
    }

    private record TimePerTurn(
            long value,
            TimeUnit unit)
    {
        private TimePerTurn
        {
            Objects.requireNonNull(unit);
            if (value < 0)
            {
                throw new IllegalArgumentException(
                        "value have to be non-negative"
                );
            }
        }
    }
}
