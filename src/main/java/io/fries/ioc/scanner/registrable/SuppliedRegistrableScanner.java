package io.fries.ioc.scanner.registrable;

import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;
import io.fries.ioc.components.Id;
import io.fries.ioc.instantiator.ComponentInstantiationException;
import io.fries.ioc.instantiator.Instantiator;
import io.fries.ioc.registry.Registrable;
import io.fries.ioc.registry.supplied.SuppliedRegistrable;
import io.fries.ioc.scanner.type.TypeScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class SuppliedRegistrableScanner implements RegistrableScanner {

    private static final int NO_PARAMETERS = 0;

    private final TypeScanner typeScanner;
    private final Instantiator instantiator;

    public SuppliedRegistrableScanner(final TypeScanner typeScanner, final Instantiator instantiator) {
        this.typeScanner = typeScanner;
        this.instantiator = instantiator;
    }

    @Override
    public List<Registrable> findAll() {
        //@ Configuration
        return typeScanner
                .findAnnotatedBy(Configuration.class)
                .stream()
                .flatMap(type -> fromConfigurationInstance(type).stream())
                .collect(toList());
    }

    List<Registrable> fromConfigurationInstance(final Class<?> type) {
        if (type.getDeclaredConstructors()[0].getParameters().length != NO_PARAMETERS)
            throw new IllegalStateException("Invalid configuration class: " + type.getName() + ". An empty constructor is required");

        // why not supply dependency
        final Object configuration = instantiator.createInstance(type, emptyList());

        // A configuration class will not be registered as a component itself.
        // Rather, it exposes its methods annotated with @Register as supplied
        // components inside the container.
        return stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Register.class))
                //
                .map(method -> createRegistrable(configuration, method, method.getAnnotation(Register.class)))
                .collect(toList());
    }

    private Registrable createRegistrable(final Object configuration, final Method method, final Register register) {
        final Id id = extractSupplierId(method, register);
        final Supplier<?> instanceSupplier = createSupplier(configuration, method);

        return SuppliedRegistrable.of(id, instanceSupplier);
    }

    // return a function
    // id=Id{value='plot.outcome'},
    Supplier<?> createSupplier(final Object configuration, final Method method) {
        if (method.getParameters().length != NO_PARAMETERS)
            throw new IllegalStateException("No parameters allowed on a supplied method");

        method.setAccessible(true);

        // io.fries.ioc.scanner.registrable.SuppliedRegistrableScanner$$Lambda$40/0x000000080117a840@3d82c5f3
        return () -> {
            try {
                // invoke without parameters?
                return method.invoke(configuration);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new ComponentInstantiationException(e);
            }
        };
    }

    /**
     *
     * @Configuration
     * class Library {
     *
     *     // As no identifier is specified with the annotation,
     *     // the method name will be used instead: "plotComplication".
     *     @Register
     *     String plotComplication() {
     *         return "Some cliffhanger";
     *     }
     * }
     */
    // if Register has id set, then use it, otherwise use the method name as supplier id
    Id extractSupplierId(final Method method, final Register register) {
        if (register.id().isEmpty())
            return Id.of(method.getName());

        return Id.of(register.id());
    }
}
