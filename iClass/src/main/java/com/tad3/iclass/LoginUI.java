package com.tad3.iclass;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 *
 * @author Laura
 */
public class LoginUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        WrappedSession session = request.getWrappedSession();
        //
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);

        //
        // The initial log view where the user can login to the application
        //
        getNavigator().addView(LoginView.NAME, LoginView.class);//

        //
        // Add the main view of the application
        //
        getNavigator().addView(AdminView.NAME, AdminView.class);
        //Para que vaya a profesor o alumno cambiar la vista, hay que corregirlo
        
        /*String priv = (String) session.getAttribute("privilegio");

        switch (priv) {
            case "1":
                //getUI().getNavigator().navigateTo(AdminView.NAME);
                getNavigator().addView(AdminView.NAME, AdminView.class);
                break;
            case "2":
                //getUI().getNavigator().navigateTo(ProfesorView.NAME);
                getNavigator().addView(ProfesorView.NAME, ProfesorView.class);
                break;
            case "3":
                //getUI().getNavigator().navigateTo(AlumnoView.NAME);
                getNavigator().addView(AlumnoView.NAME, AlumnoView.class);
                break;
        }*/

        //
        // We use a view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
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
