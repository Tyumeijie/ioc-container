package io.fries.ioc;

import io.fries.ioc.components.Components;
import io.fries.ioc.components.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("Container should")
class ContainerTest {

    @Test
    @DisplayName("throw when using a null Instantiator")
    void should_throw_when_using_a_null_instantiator() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> Container.using(null));
    }

    @Test
    @DisplayName("provide a component instance using its identifier value")
    void should_provide_a_component_instance_using_its_id_value() {
        final Object identifierValue = mock(Object.class);
        final Id id = Id.of(identifierValue);
        final Components components = mock(Components.class);
        final Object instance = mock(Object.class);

        final Container container = Container.of(components);

        when(components.getInstance(id)).thenReturn(instance);
        final Object providedInstance = container.provide(identifierValue);

        verify(components).getInstance(id);
        assertThat(providedInstance).isEqualTo(instance);
    }

    @Test
    @DisplayName("be equal")
    void should_be_equal() {
        final Components components = mock(Components.class);
        final Container firstContainer = Container.of(components);
        final Container secondContainer = Container.of(components);

        assertThat(firstContainer).isEqualTo(secondContainer);
        assertThat(firstContainer.hashCode()).isEqualTo(secondContainer.hashCode());
    }

    @Test
    @DisplayName("not be equal")
    void should_not_be_equal() {
        final Container firstContainer = Container.of(mock(Components.class));
        final Container secondContainer = Container.of(mock(Components.class));

        assertThat(firstContainer).isNotEqualTo(secondContainer);
        assertThat(firstContainer.hashCode()).isNotEqualTo(secondContainer.hashCode());
    }

    @Test
    @DisplayName("be formatted as a string")
    void should_be_formatted_as_a_string() {
        final Components components = mock(Components.class);
        final Container container = Container.of(components);

        when(components.toString()).thenReturn("Components");
        final String result = container.toString();

        assertThat(result).isEqualTo("Container{components=Components}");
    }
}