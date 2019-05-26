package io.fries.ioc.scanner.type;

import io.fries.reflection.Reflection;
import io.fries.reflection.filters.PackageFilter;
import io.fries.reflection.scanners.ClassPathScanner;
import io.fries.reflection.scanners.Scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.lang.Thread.currentThread;

public class ReflectionTypeScanner implements TypeScanner {

    private final Reflection reflection;

    public ReflectionTypeScanner(final Class<?> entryPoint) {
        this.reflection = createReflectionEngine(entryPoint);
    }

  // ComponentScan(value="com.github.gyumeijie",useDefaultFilters=true,
  //    includeFilters={
  //        @Filter(type=FilterType.ANNOTATION,classes={Controller.class}),
  //        @Filter(type=FilterType.CUSTOM,classes={MyTypeFilter.class})
  //    })
  Reflection createReflectionEngine(final Class<?> entryPoint) {
        final String rootPackage = entryPoint.getPackage().getName();
        final ClassLoader classLoader = currentThread().getContextClassLoader();
        final String slashPath = rootPackage.replaceAll("\\.", "/");
        final Scanner scanner = ClassPathScanner
                .of(classLoader, slashPath)
                .filter(PackageFilter.withSubpackages(rootPackage));

        return Reflection.of(scanner);
    }

    @Override
    public Set<Class<?>> findAnnotatedBy(final Class<? extends Annotation> annotationType) {
        return reflection.getAnnotatedTypes(annotationType);
    }
}
