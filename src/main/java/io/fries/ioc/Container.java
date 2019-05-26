package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.Registry;
import io.fries.ioc.scanner.ComponentsScanner;
import io.fries.ioc.scanner.registrable.RegistrableScanner;

import java.util.Objects;

import static java.util.Arrays.stream;

public class Container {

    private final Components components;
    static private Registry registry;
    static private Instantiator instantiator;

    @SuppressWarnings("WeakerAccess")
    public static void initialize() {
        registry = Registry.empty();
        instantiator = new DefaultInstantiator();
    }

    public static void clean() {
        registry = null;
        instantiator = null;
    }

    @SuppressWarnings("WeakerAccess")
    public static void register(final Registrable registrable) {
        registry = registry.add(registrable);
    }

    public static Container instantiate() {
        final Components components = registry.instantiate(instantiator);
        // pass the components to the container
        clean();
        return Container.of(components);
    }

    static Container of(final Components components) {
        return new Container(components);
    }

    @SuppressWarnings("WeakerAccess")
    public static void using(final Instantiator instantiator) {
        Objects.requireNonNull(instantiator);
        Container.instantiator = instantiator;
    }

    @SuppressWarnings("WeakerAccess")
    public static Container scan(final Class<?> entryPoint,
                                 final RegistrableScanner... scanners) {
        final ComponentsScanner componentsScanner = ComponentsScanner.withDefault(entryPoint);
        // initialize
        if (Container.instantiator == null){
            initialize();
        }

        stream(scanners).forEach(componentsScanner::use);

        return componentsScanner.scan();
    }

    private Container(final Components components) {
        this.components = components;
    }

    @SuppressWarnings("WeakerAccess")
    public <T, ID> T provide(final ID id) {
        final Id componentId = Id.of(id);
        return components.getInstance(componentId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Container container = (Container) o;
        return Objects.equals(components, container.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    @Override
    public String toString() {
        return "Container{" +
                "components=" + components +
                '}';
    }
}
