package com.tad3.iclass;

import com.tad3.iclass.view.AlumnoView;
import com.tad3.iclass.view.ProfesorView;
import com.tad3.iclass.view.admin.AdminView;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Laura
 */

@Widgetset("com.tad3.iclass.MyAppWidgetset")
public class LoginUI extends UI {

    public Navigator navigator;

    public static final String LOGINVIEW = "login";
    public static final String ADMINVIEW = "admin";
    public static final String ALUMNOVIEW = "alumno";
    public static final String PROFESORVIEW = "profesor";

    @Override
    protected void init(VaadinRequest request) {
        
        final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(layout);
		navigator = new Navigator(UI.getCurrent(), viewDisplay);
		navigator.addView("", new LoginView());
		navigator.addView(ADMINVIEW, new AdminView());
		navigator.addView(ALUMNOVIEW, new AlumnoView());
		navigator.addView(PROFESORVIEW, new ProfesorView());
                
        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }
}
