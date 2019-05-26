package testable.stories.plots;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Register;


// id=Id{value='PredictablePlot'},
// type=class testable.stories.plots.PredictablePlot
// components=[Id{value='plot.outcome'}]
@Register
public class PredictablePlot implements Plot {

    private final String outcome;

    public PredictablePlot(@Identified("plot.outcome") final String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "PredictablePlot('" + outcome + "')";
    }


    @Override
    public String getComplication() {
        return "PredictablePlot";
    }
}
