package testable;

import io.fries.ioc.Container;
import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;
import testable.stories.plots.Plot;
import testable.stories.protagonists.Protagonist;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Configuration
public class TestableApplication {

    @Register(id = "plot.outcome")
    String plotOutcome() {
        return "Outcome";
    }

    String nonRegisteredOutcome() {
        return "Meh.";
    }

    public static void main(String args[]) {
        final Container container = Container.scan(Library.class);
        final Book book = container.provide("NovelBook");

        System.out.println(book.readBook());

        final Plot plot = container.provide("Plot");
        System.out.println(plot.getComplication());

//        Class<?> clazz = Demo.class;
//        Constructor<?> constructors[] = clazz.getDeclaredConstructors();
//    System.out.println(constructors[0].getParameterCount());
//        System.out.println(constructors[1].getParameterCount());
//        List objs = new ArrayList();
//        objs.add(1);
//        objs.add("name");
//        try {
//          Demo d = (Demo) constructors[0].newInstance(objs.toArray());
//          System.out.println(d);
//        } catch (Exception e) {
//          System.out.println(e);
//        }
//
    }

}


//class Demo {
//    private int age;
//    private String name;
//
//    public Demo(Object ...args) {
//
//        this.age = age;
//        this.name = name;
//    }
//
//    public Demo(int age) {
//        this.age = age;
//        this.name = "name";
//    }
//
//
//
//    @Override
//    public String toString() {
//        return "name" + name + " age: " + age;
//    }
//}