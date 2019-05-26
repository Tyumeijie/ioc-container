package io.fries.ioc.instantiator;

import io.fries.ioc.components.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultInstantiator implements Instantiator {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createInstance(final Class<T> type, final List<Component> dependencies) {
        try {
            final List<?> parameterInstances = mapParameterInstances(dependencies);

            final Constructor<?> constructor = type.getDeclaredConstructors()[0];
            constructor.setAccessible(true);

      if (parameterInstances.toArray().length > 1) {
        System.out.println(
            "create instance "
                + constructor.getName()
                    + "    "
                + parameterInstances.toArray()[0].getClass().getName()
                    + " : "
                + parameterInstances.toArray()[1].getClass().getName());
        // how to guarantee the order of parameters
      }

            return (T) constructor.newInstance(parameterInstances.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new ComponentInstantiationException(e);
        }
    }

    private List<?> mapParameterInstances(final List<Component> dependencies) {
        return dependencies
                .stream()
                // will not create a brand new instance of the same type
                .map(Component::getInstance)
                .collect(toList());
    }
}
