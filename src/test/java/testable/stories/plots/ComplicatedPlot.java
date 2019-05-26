package testable.stories.plots;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;

@Register(id = "Plot")
class ComplicatedPlot implements Plot {
   private final String complication;

   // This component depends on a primitive type, which can only be registered
   // using a supplied registrable.
   ComplicatedPlot(@Identified("plotComplication") final String complication) {
      this.complication = complication;
   }


   @Override
   public String getComplication() {
      return this.complication;
   }
}

