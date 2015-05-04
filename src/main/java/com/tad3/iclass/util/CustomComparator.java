/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tad3.iclass.util;

import com.tad3.iclass.entidad.Asignatura;
import java.util.Comparator;

/**
 *
 * @author Juanlu
 */
public class CustomComparator implements Comparator<Asignatura> {
    @Override
    public int compare(Asignatura a1, Asignatura a2) {
        return a1.getCurso().compareTo(a2.getCurso());
    }
}
