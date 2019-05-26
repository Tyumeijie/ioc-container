package io.fries.ioc.components;

import java.util.Objects;

public class Component {

    private final Id id;
    private final Object instance;

    private Component(final Id id, final Object instance) {
        this.id = id;
        this.instance = instance;
    }

    public static Component of(final Id id, final Object instance) {
        return new Component(id, instance);
    }

    public Id getId() {
        return id;
    }


    @SuppressWarnings("unchecked")
    public <T> T getInstance() {
        return (T) instance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Component that = (Component) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instance);
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                ", instance=" + instance +
                '}';
    }
}
