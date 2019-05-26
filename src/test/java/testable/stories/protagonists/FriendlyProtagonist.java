package testable.stories.protagonists;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Proxy;

// id=Id{value='knights.perceval'}
// interfaceType=interface testable.stories.protagonists.Protagonist
// type=class testable.stories.protagonists.FriendlyProtagonist
// components=[Id{value='knights.karadoc'}] --> dependencies
@Proxy(id = "knights.perceval")
public class FriendlyProtagonist implements Protagonist {

    private final Protagonist friend;

    public FriendlyProtagonist(@Identified("knights.karadoc") final Protagonist friend) {
        this.friend = friend;
    }

    @Override
    public String toString() {
        return "FriendlyProtagonist(" + friend.getClass().getSimpleName() + ")";
    }
}
