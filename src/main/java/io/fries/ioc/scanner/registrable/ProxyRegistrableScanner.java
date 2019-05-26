package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Proxy;
import io.fries.ioc.components.Id;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.proxy.ProxyRegistrable;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.type.TypeScanner;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProxyRegistrableScanner implements RegistrableScanner {

    private final TypeScanner typeScanner;
    private final DependenciesScanner dependenciesScanner;

    public ProxyRegistrableScanner(final TypeScanner typeScanner, final DependenciesScanner dependenciesScanner) {
        this.typeScanner = typeScanner;
        this.dependenciesScanner = dependenciesScanner;
    }

    @Override
    public List<Registrable> findAll() {
        return typeScanner
                .findAnnotatedBy(Proxy.class)
                .stream()
                .map(type -> createRegistrable(type, type.getAnnotation(Proxy.class)))
                .collect(toList());
    }

    private Registrable createRegistrable(final Class<?> type, final Proxy proxy) {
        final Id id = extractComponentId(type, proxy);
        final Class<?> interfaceType = extractInterface(type, proxy);
        final List<Id> dependencies = dependenciesScanner.findByConstructor(type);

        return ProxyRegistrable.of(id, interfaceType, type, dependencies);
    }

    private Class<?> extractInterface(final Class<?> type, final Proxy proxy) {
        if (proxy.type().equals(Proxy.class))
            return findFirstImplementedInterface(type);

        return proxy.type();
    }

    public static Class<?> findFirstImplementedInterface(final Class<?> type) {
        if (type.getInterfaces().length > 0)
            return type.getInterfaces()[0];

        if (type.getSuperclass() != null)
            return findFirstImplementedInterface(type.getSuperclass());

        throw new IllegalArgumentException("The provided type does not implement any interface");
    }

    private Id extractComponentId(final Class<?> type, final Proxy proxy) {
        if (proxy.id().isEmpty())
            return Id.of(type.getSimpleName());

        return Id.of(proxy.id());
    }
}
