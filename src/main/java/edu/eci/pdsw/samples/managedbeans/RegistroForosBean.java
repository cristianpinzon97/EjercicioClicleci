/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.managedbeans;


import edu.eci.pdsw.samples.entities.Comentario;
import edu.eci.pdsw.samples.entities.EntradaForo;
import edu.eci.pdsw.samples.entities.Usuario;
import edu.eci.pdsw.samples.services.ExcepcionServiciosForos;
import edu.eci.pdsw.samples.services.ServiciosForo;
import edu.eci.pdsw.samples.services.ServiciosForoDAOStub;
import edu.eci.pdsw.samples.services.ServiciosForoStub;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hcadavid
 */
@ManagedBean (name= "Foros")
@SessionScoped
public class RegistroForosBean implements Serializable{

    
    

    private String emailUs;
    private String nombreUs,CommentUs,Title;
    private Comentario comentarioTemporal;
    private Usuario usuarioTemporal;
    private int cont=0;
    ServiciosForo foros=ServiciosForo.getInstance();
    EntradaForo foroSelection;
    
    
    public RegistroForosBean() {
        usuarioTemporal= new Usuario("", "");
        comentarioTemporal = new Comentario(usuarioTemporal, "",new Date(new java.util.Date().getTime()));
    }
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getCommentUs() {
        return CommentUs;
    }

    public void setCommentUs(String CommentUs) {
        this.CommentUs = CommentUs;
    }

    public String getEmailUs() {
        return emailUs;
    }

    public void setEmailUs(String emailUs) {
        this.emailUs = emailUs;
    }

    public String getNombreUs() {
        return nombreUs;
    }

    public void setNombreUs(String nombreUs) {
        this.nombreUs = nombreUs;
    }
    
    public EntradaForo getForoSelection() {
        return foroSelection;
    }

    public void setForoSelection(EntradaForo foroSelection) {
        this.foroSelection = foroSelection;
    }
    
    
    public List<EntradaForo> getForos() throws ExcepcionServiciosForos{
        return  foros.consultarEntradasForo();
    }
    
    public Comentario getComentarioTemporal() {
        return comentarioTemporal;
    }

    public void setComentarioTemporal(Comentario comentarioTemporal) {
        this.comentarioTemporal = comentarioTemporal;
    }

    public Usuario getUsuarioTemporal() {
        return usuarioTemporal;
    }

    public void setUsuarioTemporal(Usuario usuarioTemporal) {
        this.usuarioTemporal = usuarioTemporal;
    }
    
    public void limpiar(){
        foroSelection=null;
    }
           
    public void registrarNuevoComentarioForo(int idForo) throws ExcepcionServiciosForos{
        if(!(comentarioTemporal.getAutor().getEmail().equals("") && comentarioTemporal.getAutor().getNombre().equals("") && comentarioTemporal.getContenido().equals(""))){
            foros.registrarUsuario(usuarioTemporal);
            foros.agregarRespuestaForo(idForo, comentarioTemporal);
            usuarioTemporal= new Usuario("Anonimo"+cont+"@gmail.com", "");
            cont++;
            comentarioTemporal = new Comentario(usuarioTemporal, "",new Date(new java.util.Date().getTime())); 
        }
    }
    
    public void registrarNuevoForo() throws ExcepcionServiciosForos{
        Usuario us=null;
        try {
            if(!(emailUs.equals("")||nombreUs.equals(""))){
                us = new Usuario(emailUs,nombreUs);
                foros.registrarUsuario(us);
                foros.registrarNuevaEntradaForo(new EntradaForo(0, us, CommentUs, Title,new Date(new java.util.Date().getTime())));
            }
        } catch (Exception e) {
        }
        restablecerVar();
       
        
    }

    private void restablecerVar() {
        emailUs=null;nombreUs=null;CommentUs=null;Title=null;
    }
}
