package io.fries.ioc.scanner.dependencies;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.components.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class IdentifiedDependenciesScanner implements DependenciesScanner {

    // If the are lot of constructors, and it's important to identify the specify constructor
    // @autowire annotations
    @Override
    public List<Id> findByConstructor(final Class<?> type) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Parameter[] parameters = constructor.getParameters();

        // every parameter in a constructor is seen as a dependency of that class

        // System.out.println(type.getName());
        // list can keep the order of parameter of the constructor
        List<Id> ids =  stream(parameters)
                        .map(this::extractParameterId)
                        .collect(toList());

        // System.out.println("findByConstructor " + ids);
        return ids;
    }

    Id extractParameterId(final Parameter parameter) {
        // @Identified("story.fantasy") only make alias
        if (parameter.isAnnotationPresent(Identified.class))
            return parameterAnnotationToId(parameter);

        return parameterTypeToId(parameter);
    }

    Id parameterAnnotationToId(final Parameter parameter) {
        final Identified identified = parameter.getAnnotation(Identified.class);
        return Id.of(identified.value());
    }

    Id parameterTypeToId(final Parameter parameter) {
        final String parameterType = parameter.getType().getSimpleName();
        return Id.of(parameterType);
    }
}
