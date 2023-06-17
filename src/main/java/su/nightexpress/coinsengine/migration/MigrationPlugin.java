package su.nightexpress.coinsengine.migration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.hooks.Hooks;
import su.nightexpress.coinsengine.CoinsEngine;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.migration.impl.AbstractDataConverter;
import su.nightexpress.coinsengine.migration.impl.PlayerPointsConverter;

import java.util.stream.Stream;

public enum MigrationPlugin {

    PLAYER_POINTS("PlayerPoints", PlayerPointsConverter.class);

    private final String                                 pluginName;
    private final Class<? extends AbstractDataConverter> clazz;

    MigrationPlugin(@NotNull String pluginName, @NotNull Class<? extends AbstractDataConverter> clazz) {
        this.pluginName = pluginName;
        this.clazz = clazz;
    }

    @NotNull
    public String getPluginName() {
        return pluginName;
    }

    @Nullable
    public AbstractDataConverter getConverter() {
        try {
            return this.clazz.getConstructor(CoinsEngine.class).newInstance(CoinsEngineAPI.PLUGIN);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static AbstractDataConverter getConverter(@NotNull String pluginName) {
        return Stream.of(values())
            .filter(type -> Hooks.hasPlugin(type.getPluginName()))
            .filter(type -> type.getPluginName().equalsIgnoreCase(pluginName))
            .map(MigrationPlugin::getConverter).findFirst().orElse(null);
    }
}
