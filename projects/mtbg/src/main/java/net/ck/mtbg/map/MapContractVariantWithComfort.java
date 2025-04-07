package net.ck.mtbg.map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j2
@Getter
@Setter
public class MapContractVariantWithComfort
{
    public static void main(
            final String[] args)
    {
        System.out.println("MapContractVariantWithComfort.main");

        final var gameMap = createGameMap();
        final var entryFieldFactory = createEntryFieldFactory();

        gameMap
                .getProperties().stream()
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

    private static GameMapPropertySupport createGameMap()
    {
        final var gameMap = new GameMap();

        gameMap.weatherRandomness = 2;
        gameMap.visibilityRange = 42;
        gameMap.timePerTurn = new TimePerTurn(4, TimeUnit.MINUTES);
        gameMap.weather = Weather.CLOUDY;
        gameMap.size = new Point(42, 69);

        return gameMap;
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
                    "[%s] type not supported".formatted(type)
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

    private interface GameMapPropertySupport
    {
        Collection<GameMapProperty> getProperties();
    }

    private static final class GameMap
            implements GameMapPropertySupport
    {
        private int weatherRandomness;
        private int visibilityRange;
        private TimePerTurn timePerTurn;
        private Weather weather;
        private Point size;

        @Override
        public Collection<GameMapProperty> getProperties()
        {
            return List.of(
                    new GameMapPropertyImpl("weatherRandomness", weatherRandomness, int.class),
                    new GameMapPropertyImpl("visibilityRange", visibilityRange, int.class),
                    new GameMapPropertyImpl("timePerTurn", timePerTurn, TimePerTurn.class),
                    new GameMapPropertyImpl("weather", weather, Weather.class),
                    new GameMapPropertyImpl("size", size, Point.class)
            );
        }
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
