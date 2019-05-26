package testable;


import io.fries.ioc.annotations.Configuration;
import io.fries.ioc.annotations.Register;

// A configuration class will not be registered as a component itself.
// Rather, it exposes its methods annotated with @Register as supplied
// components inside the container.
@Configuration
class Library {

   // As no identifier is specified with the annotation,
   // the method name will be used instead: "plotComplication".
   @Register
   String plotComplication() {
      return "Some cliffhanger";
   }
}