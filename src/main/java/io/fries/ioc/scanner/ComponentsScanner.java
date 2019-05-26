package io.fries.ioc.scanner;

import io.fries.ioc.Container;
import io.fries.ioc.instantiator.DefaultInstantiator;
import io.fries.ioc.scanner.dependencies.DependenciesScanner;
import io.fries.ioc.scanner.dependencies.IdentifiedDependenciesScanner;
import io.fries.ioc.scanner.registrable.ManagedRegistrableScanner;
import io.fries.ioc.scanner.registrable.ProxyRegistrableScanner;
import io.fries.ioc.scanner.registrable.RegistrableScanner;
import io.fries.ioc.scanner.registrable.SuppliedRegistrableScanner;
import io.fries.ioc.scanner.type.ReflectionTypeScanner;
import io.fries.ioc.scanner.type.TypeScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentsScanner {
    private final List<RegistrableScanner> scanners;

    private ComponentsScanner() {
        this.scanners = new ArrayList<>();
    }

    // new a instance
    public static ComponentsScanner of() {
        return new ComponentsScanner();
    }

    // provide @ComponentScan annotation
    public static ComponentsScanner withDefault(final Class<?> entryPoint) {
        final TypeScanner typeScanner = new ReflectionTypeScanner(entryPoint);
        final DependenciesScanner dependenciesScanner = new IdentifiedDependenciesScanner();

        // call three types scanner need type scanner
        return of()
                .use(new ManagedRegistrableScanner(typeScanner, dependenciesScanner))
                .use(new SuppliedRegistrableScanner(typeScanner, new DefaultInstantiator()))
                .use(new ProxyRegistrableScanner(typeScanner, dependenciesScanner));
    }

    public ComponentsScanner use(final RegistrableScanner registrableScanner) {
        scanners.add(registrableScanner);
        return this;
    }

    public Container scan() {
    // add three types of scanner
    System.out.println(
        scanners.stream()
            .flatMap(scanner -> scanner.findAll().stream())
            .collect(Collectors.toList()));

        scanners.stream()
                .flatMap(scanner -> scanner.findAll().stream())
                .forEach(Container::register);

        return Container.instantiate();
    }
}
