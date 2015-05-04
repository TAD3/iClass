package com.tad3.iclass.util;

import com.tad3.iclass.dao.AsignaturaDAO;
import com.tad3.iclass.entidad.Asignatura;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExampleUtil {

    public static final Object as_PROPERTY_NAME = "curso";
    private static final AsignaturaDAO asignaturas = new AsignaturaDAO();

    private static String[][] asignatura = null;

    public static HierarchicalContainer getAsignaturaContainer() {
        ArrayList<Asignatura> arrAsignaturas = new ArrayList();
        Item item;
        int itemId = 0; // Increasing numbering for itemId:s
        try {            
            arrAsignaturas = (ArrayList) asignaturas.listaAsignaturas();
            asignatura = new String[arrAsignaturas.size()][arrAsignaturas.size() + 1];            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ExampleUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        Asignatura a;
        String curso;
        int k = 1;
        int pos = -1;
        ArrayList<String> listaCursos = new ArrayList();
        for (Asignatura arrAsignatura : arrAsignaturas) {
            a = arrAsignatura;
            curso = a.getCurso();
            if (!listaCursos.contains(curso)) {
                listaCursos.add(curso);
                k = 1;
                pos++;
                asignatura[pos][0] = curso;
                asignatura[pos][k] = a.getNombre();
            } else {
                k++;
                asignatura[pos][k] = a.getNombre();                
            }
        }

        // Create new container
        HierarchicalContainer asContainer = new HierarchicalContainer();
        // Create containerproperty for name
        boolean vacio1 = false;
        boolean vacio2;
        asContainer.addContainerProperty(as_PROPERTY_NAME, String.class, null);
        for (int i = 0; (i < asignatura.length && !vacio1); i++) {
            vacio2 = false;
            // Add new item
            item = asContainer.addItem(itemId);
            // Add name property for item
            if (asignatura[i][0] == null || asignatura[i][0] == "" ) {
                vacio1 = true;
            } else {
                item.getItemProperty(as_PROPERTY_NAME).setValue(asignatura[i][0]);
                // Allow children
                asContainer.setChildrenAllowed(itemId, true);
                itemId++;
                for (int j = 1; (j < asignatura[i].length && !vacio2); j++) {
                    // Add child items
                    item = asContainer.addItem(itemId);
                 /*   if (asignatura[i][j] == null || asignatura[i][j] == "" ) {
                        vacio2 = true;
                    } else {*/
                        item.getItemProperty(as_PROPERTY_NAME).setValue(asignatura[i][j]);
                        asContainer.setParent(itemId, itemId - j);
                        asContainer.setChildrenAllowed(itemId, false);
                        itemId++;
                  //  }
                }
            }
        }
        return asContainer;
    }
}
