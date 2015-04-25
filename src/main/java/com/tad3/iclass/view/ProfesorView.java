package com.tad3.iclass.view;

import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Profesor;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanlu
 */
public class ProfesorView extends CustomComponent implements View {

    public static final String NAME = "profesor";
    ProfesorDAO profesor = new ProfesorDAO();

    VerticalLayout panelPrincipal = new VerticalLayout();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel();
    VerticalLayout panelIzquierdo = new VerticalLayout();
    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    TextField curso = new TextField();
    TextField asignatura = new TextField();

    Label text = new Label();

    Button logoutButton = new Button("Logout", new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            getUI().getSession().close();
            getUI().getPage().setLocation(getLogoutPath());
        }
    });

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public ProfesorView() {

        /*
         Menu Bar
         */
        menuBar.setSizeFull();

        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                panelIzquierdo.removeAllComponents();

                final Table table = new Table();
                table.setPageLength(table.size());
                table.setSelectable(true);
                table.setImmediate(true);
                table.addContainerProperty("idProfesor", String.class, null);
                table.addContainerProperty("idLugar", String.class, null);
                table.addContainerProperty("nombre", String.class, null);
                table.addContainerProperty("apellidos", String.class, null);
                table.addContainerProperty("edad", String.class, null);
                table.addContainerProperty("curso", String.class, null);
                table.addContainerProperty("email", String.class, null);
                table.addContainerProperty("password", String.class, null);
                table.addContainerProperty("foto", String.class, null);

                try {
                    Profesor p = profesor.profesor((String) getSession().getAttribute("user"));
                    table.addItem(new Object[]{p.getIdProfesor(), p.getIdLugar(), p.getNombre(), p.getApellidos(),
                        p.getEdad(), p.getEmail(), p.getMovil(), p.getPassword(), p.getHorario(),
                        p.getDescripcion(), p.getEvaluacion(), p.getNumVotos(), p.getFoto()}, 1);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }

                table.addItemClickListener(new ItemClickEvent.ItemClickListener() {

                    @Override
                    public void itemClick(ItemClickEvent event) {
                        panelDerecho.removeAllComponents();
                        final TextField idProfesor = new TextField("IdProfesor");
                        final TextField idLugar = new TextField("IdLugar");
                        final TextField nombre = new TextField("Nombre");
                        final TextField apellidos = new TextField("Apellidos");
                        final TextField edad = new TextField("Edad");
                        final TextField email = new TextField();
                        final TextField movil = new TextField();
                        final PasswordField pass = new PasswordField();
                        final PasswordField pass2 = new PasswordField();
                        final Calendar horario = new Calendar();
                        final TextField descripcion = new TextField();
                        final TextField foto = new TextField();
                        try {

                            Profesor p = profesor.profesor((String) getSession().getAttribute("user"));
                            idProfesor.setValue(p.getIdProfesor());
                            idLugar.setValue(p.getIdLugar());
                            nombre.setValue(p.getNombre());
                            apellidos.setValue(p.getApellidos());
                            edad.setValue(p.getEdad());
                            email.setValue(p.getEmail());
                            pass.setValue(p.getPassword());
                            pass2.setValue(p.getPassword());
                            descripcion.setValue(p.getDescripcion());
                            foto.setValue(p.getFoto());

                        } catch (UnknownHostException ex) {
                            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        Button modificar = new Button("Guardar", new Button.ClickListener() {
                            @Override
                            public void buttonClick(ClickEvent event) {
                                Profesor p2 = new Profesor();
                                p2.setIdProfesor(idProfesor.getValue());
                                p2.setIdLugar(idLugar.getValue());
                                p2.setNombre(nombre.getValue());
                                p2.setApellidos(apellidos.getValue());
                                p2.setEdad(edad.getValue());
                                p2.setMovil(movil.getValue());
                                p2.setPassword(pass.getValue());
                                p2.setHorario(horario.toString());
                                p2.setEvaluacion("0.0");
                                p2.setNumVotos("0");
                                p2.setFoto(foto.getValue());
                                if (pass.getValue().equals(pass2.getValue())) {
                                    p2.setPassword(pass.getValue());
                                    try {
                                        Profesor a1 = profesor.profesor((String) getSession().getAttribute("user"));
                                        profesor.modificar(a1, p2);
                                        panelDerecho.removeAllComponents();
                                        panelIzquierdo.removeAllComponents();
                                        panelIzquierdo.addComponent(new Label("Actualizado"));
                                    } catch (UnknownHostException ex) {
                                        Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    pass2.setValue("no son iguales");
                                }

                            }
                        });

                        Button borrar = new Button("Borrar", new Button.ClickListener() {

                            @Override
                            public void buttonClick(ClickEvent event) {
                                try {
                                    Profesor a3 = profesor.profesor((String) getSession().getAttribute("user"));
                                    profesor.borrar(a3.getEmail());
                                    getUI().getSession().close();
                                    getUI().getPage().setLocation(getLogoutPath());
                                } catch (UnknownHostException ex) {
                                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        panelDerecho.addComponent(idProfesor);
                        panelDerecho.addComponent(idLugar);
                        panelDerecho.addComponent(nombre);
                        panelDerecho.addComponent(apellidos);
                        panelDerecho.addComponent(edad);
                        panelDerecho.addComponent(email);
                        panelDerecho.addComponent(pass);
                        panelDerecho.addComponent(pass2);
                        panelDerecho.addComponent(horario);
                        panelDerecho.addComponent(descripcion);
                        panelDerecho.addComponent(foto);
                        panelDerecho.addComponent(modificar);
                        panelDerecho.addComponent(borrar);
                    }
                });

                panelIzquierdo.addComponent(table);

            }
        };
        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        menuBar.addItem("Log out", logoutCommand);

        layaoutArriba.addComponent(menuBar);
        panelPrincipal.addComponent(layaoutArriba);
        panelPrincipal.addComponent(panelSubPrincipal);

        panelSubPrincipal.setFirstComponent(panelIzquierdo);
        panelSubPrincipal.setSecondComponent(panelDerecho);
        //panelPrincipal.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        //panelPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
        panelSubPrincipal.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");

        setCompositionRoot(new CssLayout(panelPrincipal));
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello profesor " + username);
    }
}
