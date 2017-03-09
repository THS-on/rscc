package ch.imedias.rsccfx;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class ViewController extends StackPane {

  private HashMap<String, Node> views = new HashMap<>();
  private HashMap<String, ControlledView> presenters = new HashMap<>();

  private void addScreen(String name, Node view, ControlledView presenter) {
    // Put view and presenter into HashMap
    views.put(name, view);
    presenters.put(name, presenter);
  }

  public boolean loadScreen(String name, Node view, ControlledView presenter) {
    // properly initialize view and presenter and put into HashMap
    presenter.setViewParent(this);
    addScreen(name, view, presenter);
    return true;
  }

  public boolean setScreen(final String name) {
    if (views.get(name) != null) { // view is loaded
      // If at least one view is already being displayed
      if (!getChildren().isEmpty()) {
        //remove displayed view
        getChildren().remove(0);
        //add new view
        getChildren().add(0, views.get(name));
      } else {
        // No view is currently being displayed, so just add it
        getChildren().add(views.get(name));
      }
      return true;
    } else {
      System.out.println("View " + name + " hasn't been loaded!\n");
      return false;
    }
  }

  public boolean unloadScreen(String name) {
    if (views.remove(name) == null || presenters.remove(name) == null) {
      System.out.println("View " + name + " doesn't exist");
      return false;
    } else {
      return true;
    }
  }

}
