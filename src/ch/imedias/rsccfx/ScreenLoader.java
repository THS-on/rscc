package ch.imedias.rsccfx;

import ch.imedias.rsccfx.model.Rscc;
import ch.imedias.rsccfx.view.HeaderView;
import ch.imedias.rsccfx.view.RsccHomeView;
import ch.imedias.rsccfx.view.RsccRequestHelpView;
import ch.imedias.rsccfx.view.RsccSupporterView;
import ch.imedias.rsccfx.view.View;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

/**
 * Created by Jennifer Müller on 09.03.2017.
 */
// TODO: Move class to where it makes sense.
public class ScreenLoader {
    private HashMap <String, Parent> screens = new HashMap<>();
    private Rscc model;
    public static final String HOME_VIEW = "homeView";
    public static final String REQUEST_HELP_VIEW = "requestHelpView";
    public static final String SUPPORTER_VIEW = "supporterView";
    public static final String HEADER_VIEW = "headerView";

    public ScreenLoader(Rscc model){
        this.model = model;
        addInitialScreens();
    }

    public void addInitialScreens(){
        RsccRequestHelpView requestHelpView = new RsccRequestHelpView(model);
        RsccSupporterView supporterView     = new RsccSupporterView(model);
        RsccHomeView homeView               = new RsccHomeView(model);
        HeaderView headerView               = new HeaderView(model);
        screens.put(HOME_VIEW, homeView);
        screens.put(REQUEST_HELP_VIEW, requestHelpView);
        screens.put(SUPPORTER_VIEW, supporterView);
        screens.put(HEADER_VIEW, headerView);
    }

    public Scene getScene  (String screenName){
        return new Scene(screens.get(screenName));
    }

    public View getView(String viewName){
        return (View) screens.get(viewName);
    }
}
