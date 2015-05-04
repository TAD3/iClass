package com.tad3.iclass.view;

import com.tad3.iclass.dao.ProfesorDAO;
import com.tad3.iclass.entidad.Profesor;
import com.tad3.iclass.util.ExampleUtil;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ClientSideCriterion;
import com.vaadin.event.dd.acceptcriteria.SourceIs;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.BOTTOM;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.MIDDLE;
import static com.vaadin.shared.ui.dd.VerticalDropLocation.TOP;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.TargetItemAllowsChildren;
import com.vaadin.ui.Tree.TreeDragMode;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juanlu
 */
public class ProfesorView extends CustomComponent implements View {

    public static final String NAME = "profesor";
    ProfesorDAO profesor = new ProfesorDAO();
   
    //Profesor
    TextField id_profesor = new TextField("ID: ");
    ComboBox id_lugar_profesor = new ComboBox("ID lugar: ");
    TextField nombre_profesor = new TextField("Nombre: ");
    TextField apellidos_profesor = new TextField("Apellidos: ");
    TextField edad_profesor = new TextField("Edad: ");
    TextField email_profesor = new TextField("Email: ");
    TextField movil_profesor = new TextField("Móvil: ");
    TextField password_profesor = new TextField("Contraseña: ");
    TextField descripcion_profesor = new TextField("Descripción: ");
    TextField horario_profesor = new TextField("Horario: ");
    TextField evaluacion_profesor = new TextField("Evaluación: ");
    TextField votos_profesor = new TextField("Nº votos: ");
    TextField foto_profesor = new TextField("Foto: ");
    Tree tree;
    Table table;

    Button modifyme = new Button("Actualizar");
    Button saveme = new Button("Guardar");
    Button deleteme = new Button("Borrar cuenta");
    Button manageCourses = new Button("Gestionar asignaturas");

    HorizontalLayout botonesProfesor = new HorizontalLayout(saveme, deleteme);
    FormLayout datosProfesor = new FormLayout(modifyme, id_profesor, id_lugar_profesor,
            nombre_profesor, apellidos_profesor, edad_profesor, email_profesor, movil_profesor,
            descripcion_profesor, horario_profesor, evaluacion_profesor, votos_profesor,
            password_profesor, foto_profesor, manageCourses, botonesProfesor);

    VerticalLayout panelDerecho = new VerticalLayout();
    Layout layaoutArriba = new HorizontalLayout();
    MenuBar menuBar = new MenuBar();
    TextField curso = new TextField();
    TextField asignatura = new TextField();
    VerticalLayout panelPrincipal = new VerticalLayout();
    HorizontalSplitPanel panelSubPrincipal = new HorizontalSplitPanel(datosProfesor, panelDerecho);
    Panel panel = new Panel(panelSubPrincipal);

    Profesor me = new Profesor();

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
        layaoutArriba.setSizeFull();
        menuBar.setSizeFull();

        misDatos();

        panelSubPrincipal.setSplitPosition(23.0f, Unit.PERCENTAGE);
        panelSubPrincipal.setLocked(true);

        datosProfesor.setComponentAlignment(modifyme, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(id_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(id_lugar_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(nombre_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(apellidos_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(edad_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(email_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(movil_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(password_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(descripcion_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(horario_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(evaluacion_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(votos_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(foto_profesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setComponentAlignment(botonesProfesor, Alignment.MIDDLE_CENTER);
        datosProfesor.setMargin(true);

        id_profesor.setRequired(true);
        id_lugar_profesor.setRequired(true);
        nombre_profesor.setRequired(true);
        apellidos_profesor.setRequired(true);
        edad_profesor.setRequired(true);
        email_profesor.setRequired(true);
        movil_profesor.setRequired(true);
        password_profesor.setRequired(true);
        descripcion_profesor.setRequired(false);
        horario_profesor.setRequired(true);

        readOnly();

        modifyme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                id_profesor.setReadOnly(true);
                id_lugar_profesor.setReadOnly(false);
                nombre_profesor.setReadOnly(false);
                apellidos_profesor.setReadOnly(false);
                edad_profesor.setReadOnly(false);
                email_profesor.setReadOnly(true);
                movil_profesor.setReadOnly(false);
                password_profesor.setReadOnly(false);
                descripcion_profesor.setReadOnly(false);
                horario_profesor.setReadOnly(false);
                evaluacion_profesor.setReadOnly(true);
                votos_profesor.setReadOnly(true);
                foto_profesor.setReadOnly(false);
            }
        });

        saveme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Profesor p = new Profesor();
                p.setIdProfesor(id_profesor.getValue());
                p.setIdLugar((String) id_lugar_profesor.getValue());
                p.setNombre(nombre_profesor.getValue());
                p.setApellidos(apellidos_profesor.getValue());
                p.setEdad(edad_profesor.getValue());
                p.setEmail(email_profesor.getValue());
                p.setMovil(movil_profesor.getValue());
                p.setPassword(password_profesor.getValue());
                p.setDescripcion(descripcion_profesor.getValue());
                p.setHorario(horario_profesor.getValue());
                p.setEvaluacion(evaluacion_profesor.getValue());
                p.setNumVotos(votos_profesor.getValue());
                p.setFoto(foto_profesor.getValue());
                boolean encontrado = false;
                try {
                    encontrado = profesor.buscarProfesor(me.getEmail());
                    if (encontrado == true) {
                        profesor.modificar(me, p);
                        me = p;
                        readOnly();
                        Notification.show("Profesor modificado", "Se ha actualizado el "
                                + "profesor en la base de datos", Notification.Type.TRAY_NOTIFICATION);
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteme.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Profesor p = new Profesor();
                boolean encontrado = false;
                String email = email_profesor.getValue();
                try {
                    encontrado = profesor.buscarProfesor(email);
                    if (encontrado == true) {
                        profesor.borrar(email);
                        getUI().getSession().close();
                        getUI().getPage().setLocation(getLogoutPath());
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        manageCourses.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                dragAndDrop();
            }
        });

        MenuBar.Command profesorCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

            }
        };

        MenuBar.Command logoutCommand = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                getUI().getSession().close();
                getUI().getPage().setLocation(getLogoutPath());
            }
        };

        menuBar.addItem(
                "Profesor", profesorCommand);
        menuBar.addItem(
                "Log out", logoutCommand);

        layaoutArriba.addComponent(menuBar);

        panel.setWidth(String.valueOf(Page.getCurrent().getBrowserWindowWidth()) + "px");
        panel.setHeight(String.valueOf(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
        setCompositionRoot(
                new CssLayout(layaoutArriba, panel));
    }

    public final void readOnly() {
        id_profesor.setReadOnly(true);
        id_lugar_profesor.setReadOnly(true);
        nombre_profesor.setReadOnly(true);
        apellidos_profesor.setReadOnly(true);
        edad_profesor.setReadOnly(true);
        email_profesor.setReadOnly(true);
        movil_profesor.setReadOnly(true);
        password_profesor.setReadOnly(true);
        descripcion_profesor.setReadOnly(true);
        horario_profesor.setReadOnly(true);
        evaluacion_profesor.setReadOnly(true);
        votos_profesor.setReadOnly(true);
        foto_profesor.setReadOnly(true);
    }

    public void misDatos() {
        try {
            String username = "profesor@gmail.com";//String.valueOf(getSession().getAttribute("user"));
            me = profesor.profesor(username);
            id_profesor.setValue(me.getIdProfesor());
            id_lugar_profesor.setValue(me.getIdLugar());
            nombre_profesor.setValue(me.getNombre());
            apellidos_profesor.setValue(me.getApellidos());
            edad_profesor.setValue(me.getEdad());
            email_profesor.setValue(me.getEmail());
            movil_profesor.setValue(me.getMovil());
            password_profesor.setValue(me.getPassword());
            descripcion_profesor.setValue(me.getDescripcion());
            horario_profesor.setValue(me.getHorario());
            evaluacion_profesor.setValue(me.getEvaluacion());
            votos_profesor.setValue(me.getNumVotos());
            foto_profesor.setValue(me.getFoto());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ProfesorView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // START -> DRAG AND DROP
    public static class Asignatura implements Serializable {

        private String curso;
        private String nombre;

        public Asignatura(String curso, String nombre) {
            this.curso = curso;
            this.nombre = nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setCurso(String curso) {
            this.curso = curso;
        }

        public String getCurso() {
            return curso;
        }
    }

    public void dragAndDrop() {
        panelDerecho.setSpacing(true);

        // First create the components to be able to refer to them as allowed
        // drag sources
        tree = new Tree("Drag from tree to table");
        table = new Table("Drag from table to tree");
        table.setWidth("100%");

        // Populate the tree and set up drag & drop
        initializeTree(new SourceIs(table));

        // Populate the table and set up drag & drop
        initializeTable(new SourceIs(tree));

        // Add components
        panelDerecho.addComponent(tree);
        panelDerecho.addComponent(table);
    }

    private void initializeTree(final ClientSideCriterion acceptCriterion) {
        tree.setContainerDataSource(ExampleUtil.getAsignaturaContainer());
        tree.setItemCaptionPropertyId(ExampleUtil.as_PROPERTY_NAME);
        // Expand all nodes
        for (Iterator<?> it = tree.rootItemIds().iterator(); it.hasNext();) {
            tree.expandItemsRecursively(it.next());
        }
        tree.setDragMode(TreeDragMode.NODE);
        tree.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent dropEvent) {
                // criteria verify that this is safe
                DataBoundTransferable t = (DataBoundTransferable) dropEvent
                        .getTransferable();
                Container sourceContainer = t.getSourceContainer();
                Object sourceItemId = t.getItemId();
                Item sourceItem = sourceContainer.getItem(sourceItemId);
                String nombre = sourceItem.getItemProperty("nombre").toString();
                String curso = sourceItem.getItemProperty("curso").toString();

                AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent
                        .getTargetDetails());
                Object targetItemId = dropData.getItemIdOver();

                // find curso in target: the target node itself or its parent
                if (targetItemId != null && nombre != null && curso != null) {
                    String treeCurso = getTreeNodeName(tree, targetItemId);
                    if (curso.equals(treeCurso)) {
                        // move item from table to curso'
                        Object newItemId = tree.addItem();
                        tree.getItem(newItemId)
                                .getItemProperty(ExampleUtil.as_PROPERTY_NAME)
                                .setValue(nombre);
                        tree.setParent(newItemId, targetItemId);
                        tree.setChildrenAllowed(newItemId, false);

                        sourceContainer.removeItem(sourceItemId);
                    }
                }
            }

            public AcceptCriterion getAcceptCriterion() {
                // Only allow dropping of data bound transferables within
                // folders.
                // In this example, checking for the correct curso in drop()
                // rather than in the criteria.
                return new com.vaadin.event.dd.acceptcriteria.And(acceptCriterion, TargetItemAllowsChildren.get(), AcceptItem.ALL);
            }
        });
    }

    private void initializeTable(final ClientSideCriterion acceptCriterion) {
        final BeanItemContainer<Asignatura> tableContainer = new BeanItemContainer<>(
                Asignatura.class);        
        // lista asignaturas del profesor
        table.setContainerDataSource(tableContainer);
        table.setVisibleColumns(new Object[]{"curso", "asignatura"});

        // Handle drop in table: move hardware item or subtree to the table
        table.setDragMode(TableDragMode.ROW);
        table.setDropHandler(new DropHandler() {
            public void drop(DragAndDropEvent dropEvent) {
                // criteria verify that this is safe
                DataBoundTransferable t = (DataBoundTransferable) dropEvent
                        .getTransferable();
                if (!(t.getSourceContainer() instanceof Container.Hierarchical)) {
                    return;
                }
                Container.Hierarchical source = (Container.Hierarchical) t
                        .getSourceContainer();

                Object sourceItemId = t.getItemId();

                // find and convert the item(s) to move
                Object parentItemId = source.getParent(sourceItemId);
                // map from moved source item Id to the corresponding Asignatura
                LinkedHashMap<Object, Asignatura> asignaturaMap = new LinkedHashMap<Object, Asignatura>();
                if (parentItemId == null) {
                    // move the whole subtree
                    String curso = getTreeNodeName(source, sourceItemId);
                    Collection<?> children = source.getChildren(sourceItemId);
                    if (children != null) {
                        for (Object childId : children) {
                            String nombre = getTreeNodeName(source, childId);
                            asignaturaMap.put(childId, new Asignatura(curso,
                                    nombre));
                        }
                    }
                } else {
                    // move a single hardware item
                    String curso = getTreeNodeName(source, parentItemId);
                    String name = getTreeNodeName(source, sourceItemId);
                    asignaturaMap.put(sourceItemId, new Asignatura(name, curso));
                }

                // move item(s) to the correct location in the table
                AbstractSelectTargetDetails dropData = ((AbstractSelectTargetDetails) dropEvent.getTargetDetails());
                Object targetItemId = dropData.getItemIdOver();

                for (Object sourceId : asignaturaMap.keySet()) {
                    Asignatura asignatura = asignaturaMap.get(sourceId);
                    if (targetItemId != null) {
                        switch (dropData.getDropLocation()) {
                            case BOTTOM:
                                tableContainer.addItemAfter(targetItemId, asignatura);
                                break;
                            case MIDDLE:
                            case TOP:
                                Object prevItemId = tableContainer
                                        .prevItemId(targetItemId);
                                tableContainer.addItemAfter(prevItemId, asignatura);
                                break;
                        }
                    } else {
                        tableContainer.addItem(asignatura);
                    }
                    source.removeItem(sourceId);
                }
            }

            public AcceptCriterion getAcceptCriterion() {
                return new com.vaadin.event.dd.acceptcriteria.And(acceptCriterion, TargetItemAllowsChildren.get(), AcceptItem.ALL);
            }
        });
    }

    private static String getTreeNodeName(Container.Hierarchical source,
            Object sourceId) {
        return (String) source.getItem(sourceId)
                .getItemProperty(ExampleUtil.as_PROPERTY_NAME).getValue();
    }

    // END -> DRAG AND DROP
    @Override
    public void enter(ViewChangeEvent event) {
        String username = String.valueOf(getSession().getAttribute("user"));
    }
}
