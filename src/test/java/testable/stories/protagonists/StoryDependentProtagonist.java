package testable.stories.protagonists;

import io.fries.ioc.annotations.Identified;
import io.fries.ioc.annotations.Proxy;
import testable.stories.Story;


@Proxy(id = "Protagonist", type = Protagonist.class)
class StoryDependentProtagonist implements Protagonist {
   private final Story story;

   StoryDependentProtagonist(@Identified("story.fantasy") final Story story) {
      this.story = story;
   }
}