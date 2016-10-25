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
package edu.eci.pdsw.samples.services;

import edu.eci.pdsw.samples.entities.Comentario;
import edu.eci.pdsw.samples.entities.EntradaForo;
import edu.eci.pdsw.samples.entities.Usuario;
import edu.eci.pdsw.samples.persistence.DaoEntradaForo;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoUsuario;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */

public class ServiciosForoDAOStub extends ServiciosForo implements Serializable{


    private  DaoUsuario usuarios;

    private  DaoFactory daof;
    
    private  DaoEntradaForo foros;
    private static int foroidcount=0;

    public ServiciosForoDAOStub(int pro)  {
        Properties properties=new PropertiesLoader().readProperties("applicationconfig.properties");
        if(pro==2){
            properties=new PropertiesLoader().readProperties("h2-applicationconfig.properties");
        }
        daof=DaoFactory.getInstance(properties);
        
        usuarios=daof.getDaoUsuario();
        
    }

    @Override
    public List<EntradaForo> consultarEntradasForo() {
        List<EntradaForo> lista = null;
        try {
            daof.beginSession();
            foros=daof.getDaoEntradaForo();
            lista= foros.loadAll();
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public EntradaForo consultarEntradaForo(int id) throws ExcepcionServiciosForos {
        try {
            EntradaForo f = foros.load(id);
            return f;
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionServiciosForos("Entrada a foro inexistente:"+id);
        }
        
    }

    @Override
    public void registrarNuevaEntradaForo(EntradaForo f) throws ExcepcionServiciosForos {
        try {     
            foros.save(f);
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<Usuario> getUsuarios()  {
        LinkedList<Usuario> lista = null;
        try {
            lista = new LinkedList<>(usuarios.loadAll());
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    @Override
    public void agregarRespuestaForo(int idforo, Comentario c) throws ExcepcionServiciosForos {
        try{
            foros.addToForo(idforo, c);
        }catch (Exception ex){
            throw new ExcepcionServiciosForos("Foro no encontrado, rectifique e intente de nuevo.");
        }
        
    }

    @Override
    public Usuario consultarUsuario(String email) throws ExcepcionServiciosForos {
        try {
            return usuarios.load(email);
        } catch (Exception e) {
            throw new ExcepcionServiciosForos("El correo "+email+" no se encuentra registrado.");
        }
        
        
    }
    
    @Override
    public void registrarUsuario(Usuario us) throws ExcepcionServiciosForos {
        try {     
            usuarios.save(us);
        } catch(Exception e) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, e);
            throw new ExcepcionServiciosForos("El correo "+us.getEmail()+" ya se encuentra registrado.");
   
        }
               
        
    }

    @Override
    public Map<String, Usuario> getUsuarios1() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
            
    
}

class PropertiesLoader {

    public Properties readProperties(String fileName)  {
        InputStream input = null;
        Properties properties = new Properties();
        input = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
}