package com.tad3.iclass.view.admin;

import com.tad3.iclass.dao.AsignaturaDAO;
import com.tad3.iclass.dao.LugarDAO;
import com.tad3.iclass.entidad.Asignatura;
import com.tad3.iclass.entidad.Lugar;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalSplitPanel;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laura
 */
public class AdminView extends CustomComponent implements View {

    public static final String NAME = "admin";

    Label text = new Label();
    Layout layout = new HorizontalLayout();
    Panel panel = new Panel("Bienvenido a la página de administración");

    //Asignatura
    AsignaturaDAO asignatura = new AsignaturaDAO();
    BeanItemContainer<Asignatura> bAsignatura = new BeanItemContainer(Asignatura.class);
    Table tAsignaturas = new Table("Asignaturas", bAsignatura);
    ComboBox buscadorAsignaturas = new ComboBox("Asignatura: ");

    TextField id_asig = new TextField("ID: ");
    TextField nombre_asig = new TextField("Nombre: ");
    ComboBox curso_asig = new ComboBox("Curso: ");
    TextArea descripcion_asig = new TextArea("Descripción: ");

    Button allAsignatura = new Button("Todas las asignaturas");
    Button nuevaAsignatura = new Button("Nueva asignatura");
    Button addAsignatura = new Button("Añadir");
    Button deleteAsignatura = new Button("Borrar");

    Asignatura asigTabla = new Asignatura();
    List<Asignatura> listaAsignaturas = new ArrayList<>();

    HorizontalLayout botones = new HorizontalLayout(addAsignatura, deleteAsignatura);
    FormLayout asignaturaNueva = new FormLayout(id_asig, nombre_asig, curso_asig, descripcion_asig, botones);
    HorizontalLayout hlasignatura = new HorizontalLayout(buscadorAsignaturas, allAsignatura, nuevaAsignatura);
    VerticalSplitPanel vspAsignatura = new VerticalSplitPanel(hlasignatura, tAsignaturas);
    HorizontalSplitPanel hspAsignatura = new HorizontalSplitPanel(vspAsignatura, asignaturaNueva);

    //Lugar
    LugarDAO lugar = new LugarDAO();
    BeanItemContainer<Lugar> bLugar = new BeanItemContainer(Lugar.class);
    Table tLugares = new Table("Lugares", bLugar);
    ComboBox buscadorLugares = new ComboBox("Lugar: ");

    TextField id_lugar = new TextField("ID: ");
    TextField codigo_postal_lugar = new TextField("Código postal: ");
    TextField barrio_lugar = new TextField("Barrio: ");
    TextField ciudad_lugar = new TextField("Ciudad: ");

    Button allLugar = new Button("Todas los lugares");
    Button nuevoLugar = new Button("Nuevo lugar");
    Button addLugar = new Button("Añadir");
    Button deleteLugar = new Button("Borrar");

    Lugar lugarTabla = new Lugar();
    List<Lugar> listaLugares = new ArrayList<>();

    HorizontalLayout botonesLugar = new HorizontalLayout(addLugar, deleteLugar);
    FormLayout lugarNuevo = new FormLayout(id_lugar, codigo_postal_lugar, barrio_lugar, ciudad_lugar, botonesLugar);
    HorizontalLayout hlLugar = new HorizontalLayout(buscadorLugares, allLugar, nuevoLugar);
    VerticalSplitPanel vspLugar = new VerticalSplitPanel(hlLugar, tLugares);
    HorizontalSplitPanel hspLugar = new HorizontalSplitPanel(vspLugar, lugarNuevo);

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

    public AdminView() {

        layout.setSizeFull();

        MenuBar menu = new MenuBar();
        menu.setSizeFull();

        layout.addComponent(menu);

        // Define a common menu command for all the menu items.
        MenuBar.Command mycommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                switch (selectedItem.getText()) {
                    case "Asignaturas":
                        panel.setContent(hspAsignatura);
                        break;
                    case "Lugares":
                        panel.setContent(hspLugar);
                        break;
                }
            }
        };

        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        MenuItem profesores = menu.addItem("Profesores", null, mycommand);
        MenuItem alumnos = menu.addItem("Alumnos", null, mycommand);
        MenuItem asignaturas = menu.addItem("Asignaturas", null, mycommand);
        MenuItem lugares = menu.addItem("Lugares", null, mycommand);
        lugares.addSeparator();
        menu.addItem("Log out", logoutCommand);

        //Asignatura
        String[] columnHeadersAsig = {"Curso", "Descripción", "ID", "Asignatura"};

        tAsignaturas.setColumnHeaders(columnHeadersAsig);
        tAsignaturas.setSizeFull();
        tAsignaturas.setPageLength(tAsignaturas.size());
        tAsignaturas.setSelectable(true);
        tAsignaturas.setImmediate(true);

        llenarTablaAsig();

        Collection<String> cursos = new ArrayList<>();
        cursos.add("4º Primaria");
        cursos.add("5º Primaria");
        cursos.add("6º Primaria");
        cursos.add("1º ESO");
        cursos.add("2º ESO");
        cursos.add("3º ESO");
        cursos.add("4º ESO");
        cursos.add("1º Bachillerato");
        cursos.add("2º Bachillerato");
        curso_asig.addItems(cursos);

        buscadorAsignaturas.setInputPrompt("Ninguna asignatura seleccionada");

        buscadorAsignaturas.setWidth(100.0f, Unit.PERCENTAGE);
        buscadorAsignaturas.setSizeFull();

        buscadorAsignaturas.setFilteringMode(FilteringMode.CONTAINS);
        buscadorAsignaturas.setImmediate(true);

        buscadorAsignaturas.setNullSelectionAllowed(false);

        for (Asignatura asig : listaAsignaturas) {
            buscadorAsignaturas.addItem(asig.getIdAsignatura());
            buscadorAsignaturas.setItemCaption(asig.getIdAsignatura(), asig.toString());
        }

        buscadorAsignaturas.addValueChangeListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String select = (String) event.getProperty().getValue();
                bAsignatura.removeAllItems();

                try {
                    bAsignatura.addItem(asignatura.asignatura(select));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        hlasignatura.setMargin(true);
        hlasignatura.setSizeFull();
        hlasignatura.setComponentAlignment(buscadorAsignaturas, Alignment.MIDDLE_LEFT);
        hlasignatura.setComponentAlignment(allAsignatura, Alignment.MIDDLE_RIGHT);
        hlasignatura.setComponentAlignment(nuevaAsignatura, Alignment.MIDDLE_RIGHT);

        id_asig.setRequired(true);
        id_asig.setInputPrompt("3 INICIALES + CURSO");
        nombre_asig.setRequired(true);
        nombre_asig.setInputPrompt("Nombre de la asignatura");
        curso_asig.setRequired(true);
        curso_asig.setInputPrompt("Curso de la asignatura");
        descripcion_asig.setRequired(false);
        descripcion_asig.setInputPrompt("Descripcion de la asignatura");

        asignaturaNueva.setComponentAlignment(id_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(nombre_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(curso_asig, Alignment.MIDDLE_CENTER);
        asignaturaNueva.setComponentAlignment(descripcion_asig, Alignment.MIDDLE_CENTER);
        botones.setComponentAlignment(addAsignatura, Alignment.MIDDLE_LEFT);
        botones.setComponentAlignment(deleteAsignatura, Alignment.MIDDLE_RIGHT);
        asignaturaNueva.setCaption("Introduzca los datos de la nueva asignatura");
        asignaturaNueva.setMargin(true);
        asignaturaNueva.setSizeFull();

        vspAsignatura.setSplitPosition(17.5f, Unit.PERCENTAGE);
        vspAsignatura.setLocked(true);
        hspAsignatura.setSplitPosition(75.0f, Unit.PERCENTAGE);
        hspAsignatura.setLocked(true);

        //Datos Asignatura
        tAsignaturas.addItemClickListener(new ItemClickEvent.ItemClickListener() {

            @Override

            public void itemClick(ItemClickEvent event) {
                BeanItem<Asignatura> bi = bAsignatura.getItem(event.getItemId());
                asigTabla = bi.getBean();
                id_asig.setValue(asigTabla.getIdAsignatura());
                nombre_asig.setValue(asigTabla.getNombre());
                curso_asig.setValue(asigTabla.getCurso());
                descripcion_asig.setValue(asigTabla.getDescripcion());
            }
        });

        allAsignatura.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                llenarTablaAsig();
            }
        });

        nuevaAsignatura.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                id_asig.setValue("");
                nombre_asig.setValue("");
                curso_asig.select(null);
                descripcion_asig.setValue("");
            }
        });

        addAsignatura.addClickListener(new Button.ClickListener() {
            AsignaturaDAO asignatura = new AsignaturaDAO();

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Asignatura a = new Asignatura();
                boolean encontrado = false;
                a.setIdAsignatura(id_asig.getValue());
                a.setNombre(nombre_asig.getValue());
                a.setCurso((String) curso_asig.getValue());
                a.setDescripcion(descripcion_asig.getValue());
                try {
                    encontrado = asignatura.buscarAsignatura(a.getIdAsignatura());
                    if (encontrado == true) {
                        if (asignatura.modificarAsignatura(asigTabla, a)) {
                            llenarTablaAsig();
                            for (Asignatura asig : listaAsignaturas) {
                                buscadorAsignaturas.addItem(asig.getIdAsignatura());
                                buscadorAsignaturas.setItemCaption(asig.getIdAsignatura(), asig.toString());
                            }
                            Notification.show("Asignatura modificada", "Se ha actualizado la "
                                    + "asignatura en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido actualizar la "
                                    + "asignatura en la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        if (asignatura.crearAsignatura(a)) {
                            llenarTablaAsig();
                            for (Asignatura asig : listaAsignaturas) {
                                buscadorAsignaturas.addItem(asig.getIdAsignatura());
                                buscadorAsignaturas.setItemCaption(asig.getIdAsignatura(), asig.toString());
                            }
                            Notification.show("Asignatura creada", "Se ha añadido una nueva "
                                    + "asignatura a la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido añadir una nueva "
                                    + "asignatura a la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        deleteAsignatura.addClickListener(new Button.ClickListener() {
            AsignaturaDAO asignatura = new AsignaturaDAO();

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Asignatura a = new Asignatura();
                boolean encontrado = false;
                String id = id_asig.getValue();
                try {
                    encontrado = asignatura.buscarAsignatura(id);
                    if (encontrado == true) {
                        if (asignatura.borrarAsignatura(id)) {
                            llenarTablaAsig();
                            id_asig.setValue("");
                            nombre_asig.setValue("");
                            curso_asig.select(null);
                            for (Asignatura asig : listaAsignaturas) {
                                buscadorAsignaturas.addItem(asig.getIdAsignatura());
                                buscadorAsignaturas.setItemCaption(asig.getIdAsignatura(), asig.toString());
                            }
                            descripcion_asig.setValue("");
                            Notification.show("Asignatura eliminada", "Se ha borrado la "
                                    + "asignatura de la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido borrar la "
                                    + "asignatura de la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        Notification.show("Seleccione una asignatura", "Debe seleccionar una"
                                + "asignatura para poder eliminarla de la base de datos", Notification.Type.TRAY_NOTIFICATION);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //Lugar
        String[] columnHeadersLugar = {"Barrio", "Ciudad", "Codigo Postal", "ID"};

        tLugares.setColumnHeaders(columnHeadersLugar);
        tLugares.setSizeFull();
        tLugares.setPageLength(tLugares.size());
        tLugares.setSelectable(true);
        tLugares.setImmediate(true);

        llenarTablaLugar();

        buscadorLugares.setInputPrompt("Ningún lugar seleccionado");

        buscadorLugares.setWidth(100.0f, Unit.PERCENTAGE);
        buscadorLugares.setSizeFull();

        buscadorLugares.setFilteringMode(FilteringMode.CONTAINS);
        buscadorLugares.setImmediate(true);

        buscadorLugares.setNullSelectionAllowed(false);

        for (Lugar lug : listaLugares) {
            buscadorLugares.addItem(lug.getIdLugar());
            buscadorLugares.setItemCaption(lug.getIdLugar(), lug.toString());
        }

        buscadorLugares.addValueChangeListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String select = (String) event.getProperty().getValue();
                bLugar.removeAllItems();

                try {
                    bLugar.addItem(lugar.lugar(select));
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        hlLugar.setMargin(true);
        hlLugar.setSizeFull();
        hlLugar.setComponentAlignment(buscadorLugares, Alignment.MIDDLE_LEFT);
        hlLugar.setComponentAlignment(allLugar, Alignment.MIDDLE_RIGHT);
        hlLugar.setComponentAlignment(nuevoLugar, Alignment.MIDDLE_RIGHT);

        id_lugar.setRequired(true);
        id_lugar.setInputPrompt("CP + 3BARRIO + 3CIUDAD");
        codigo_postal_lugar.setRequired(true);
        codigo_postal_lugar.setInputPrompt("Código postal");
        barrio_lugar.setRequired(true);
        barrio_lugar.setInputPrompt("Barrio");
        ciudad_lugar.setRequired(true);
        ciudad_lugar.setInputPrompt("Ciudad");

        lugarNuevo.setComponentAlignment(id_lugar, Alignment.MIDDLE_CENTER);
        lugarNuevo.setComponentAlignment(codigo_postal_lugar, Alignment.MIDDLE_CENTER);
        lugarNuevo.setComponentAlignment(barrio_lugar, Alignment.MIDDLE_CENTER);
        lugarNuevo.setComponentAlignment(ciudad_lugar, Alignment.MIDDLE_CENTER);
        botonesLugar.setComponentAlignment(addLugar, Alignment.MIDDLE_LEFT);
        botonesLugar.setComponentAlignment(deleteLugar, Alignment.MIDDLE_RIGHT);
        lugarNuevo.setCaption("Introduzca los datos del nuevo lugar");
        lugarNuevo.setMargin(true);
        lugarNuevo.setSizeFull();

        vspLugar.setSplitPosition(17.5f, Unit.PERCENTAGE);
        vspLugar.setLocked(true);
        hspLugar.setSplitPosition(75.0f, Unit.PERCENTAGE);
        hspLugar.setLocked(true);

        //Datos Lugar
        tLugares.addItemClickListener(new ItemClickEvent.ItemClickListener() {

            @Override

            public void itemClick(ItemClickEvent event) {
                BeanItem<Lugar> bil = bLugar.getItem(event.getItemId());
                lugarTabla = bil.getBean();
                id_lugar.setValue(lugarTabla.getIdLugar());
                codigo_postal_lugar.setValue(lugarTabla.getCodigoPostal());
                barrio_lugar.setValue(lugarTabla.getBarrio());
                ciudad_lugar.setValue(lugarTabla.getCiudad());
            }
        });

        allLugar.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                llenarTablaLugar();
            }
        });

        nuevoLugar.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                id_lugar.setValue("");
                codigo_postal_lugar.setValue("");
                barrio_lugar.setValue("");
                ciudad_lugar.setValue("");
            }
        });

        addLugar.addClickListener(new Button.ClickListener() {
            LugarDAO lugar = new LugarDAO();

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Lugar l = new Lugar();
                boolean encontrado = false;
                l.setIdLugar(id_lugar.getValue());
                l.setCodigoPostal(codigo_postal_lugar.getValue());
                l.setBarrio(barrio_lugar.getValue());
                l.setCiudad(ciudad_lugar.getValue());
                try {
                    encontrado = lugar.buscarLugar(l.getIdLugar());
                    if (encontrado == true) {
                        if (lugar.modificarLugar(lugarTabla, l)) {
                            llenarTablaLugar();
                            for (Lugar lug : listaLugares) {
                                buscadorLugares.addItem(lug.getIdLugar());
                                buscadorLugares.setItemCaption(lug.getIdLugar(), lug.toString());
                            }
                            Notification.show("Lugar modificado", "Se ha actualizado el "
                                    + "lugar en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido actualizar el "
                                    + "lugar en la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        if (lugar.crearLugar(l)) {
                            llenarTablaLugar();
                            for (Lugar lug : listaLugares) {
                                buscadorLugares.addItem(lug.getIdLugar());
                                buscadorLugares.setItemCaption(lug.getIdLugar(), lug.toString());
                            }
                            Notification.show("Lugar creado", "Se ha añadido un nuevo "
                                    + "lugar a la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido añadir un nuevo "
                                    + "lugar a la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        deleteLugar.addClickListener(new Button.ClickListener() {
            LugarDAO lugar = new LugarDAO();

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Lugar l = new Lugar();
                boolean encontrado = false;
                String id = id_lugar.getValue();
                try {
                    encontrado = lugar.buscarLugar(id);
                    if (encontrado == true) {
                        if (lugar.borrarLugar(id)) {
                            llenarTablaLugar();
                            id_lugar.setValue("");
                            codigo_postal_lugar.setValue("");
                            barrio_lugar.setValue("");
                            ciudad_lugar.setValue("");
                            for (Lugar lug : listaLugares) {
                                buscadorLugares.addItem(lug.getIdLugar());
                                buscadorLugares.setItemCaption(lug.getIdLugar(), lug.toString());
                            }
                            Notification.show("Lugar eliminado", "Se ha borrado el "
                                    + "lugar de la base de datos", Notification.Type.TRAY_NOTIFICATION);
                        } else {
                            Notification.show("ERROR", "No se ha podido borrar el "
                                    + "lugar de la base de datos", Notification.Type.ERROR_MESSAGE);
                        }
                    } else {
                        Notification.show("Seleccione un lugar", "Debe seleccionar un "
                                + "lugar para poder eliminarlo de la base de datos", Notification.Type.TRAY_NOTIFICATION);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        panel.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
        panel.setContent(new Label("Este es el panel de administración de la "
                + "aplicación iClass. Aquí podrá gestionar las entidades Profesor,"
                + " Alumno, Asignatura y Lugar."));

        setCompositionRoot(new CssLayout(layout, panel));
    }

    public final void llenarTablaAsig() {
        bAsignatura.removeAllItems();

        try {
            listaAsignaturas = asignatura.listaAsignaturas();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
        }

        bAsignatura.addAll(listaAsignaturas);
    }

    public final void llenarTablaLugar() {
        bLugar.removeAllItems();

        try {
            listaLugares = lugar.listaLugares();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AdminView.class.getName()).log(Level.SEVERE, null, ex);
        }

        bLugar.addAll(listaLugares);
    }

    @Override

    public void enter(ViewChangeEvent event) {
        // Get the user name from the session
        String username = String.valueOf(getSession().getAttribute("user"));

        // And show the username
        text.setValue("Hello admin " + username);
    }
}
