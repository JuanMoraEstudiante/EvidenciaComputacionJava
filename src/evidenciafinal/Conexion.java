/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evidenciafinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juanc
 */
public class Conexion {
    //DIRECCIÓN DE LA BASE DE DATOS
    String url = "D:\\Documentos\\Proyectos Netbeans\\EvidenciaFinal\\consultorio.db";
    Connection connect;
    
    //Conectar a base de datos
    public void connect(){
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:"+url);
            if (connect!=null) {
                System.out.println("Conectado");
            }
        }catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
        }
    }
    
    //Cerrar base de datos
    public void close(){
           try {
               connect.close();
           } catch (SQLException ex) {
               Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
 
    public void verificarTablas(){
        try {
            PreparedStatement st = connect.prepareStatement("create table if not exists usuarios (id integer primary key autoincrement, nombre varchar(150) not null, rol varchar(20) not null, especialidad varchar(100) default null, usuario varchar(100) not null, clave varchar(100) not null);");
            st.execute();
            PreparedStatement st2 = connect.prepareStatement("create table if not exists citas (id integer primary key autoincrement, usuario varchar(100) not null, nombre varchar(150) not null, doctor varchar(150) not null, fecha varchar(50) not null, hora int not null);");
            st2.execute();
            PreparedStatement st3 = connect.prepareStatement("create table if not exists especialidades (id integer primary key autoincrement, nombre varchar(150) not null);");
            st3.execute();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList iniciarSesion(String usuario, String clave){
        ArrayList usuarioG =  new ArrayList();
        String iniciar = "No iniciar";
        try {
            PreparedStatement st = connect.prepareStatement("select * from usuarios;");
            //Seleccionar todo de la tabla de usuarios
            ResultSet result = null;
            result = st.executeQuery();
            //Verificar todo
            while(result.next()){
                if(result.getString("usuario").equals(usuario) && result.getString("clave").equals(clave)){
                    iniciar = "Iniciar";
                    usuarioG.add(iniciar);
                    usuarioG.add(result.getString("usuario"));
                    usuarioG.add(result.getString("rol"));
                    break;
                }
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioG;
    }
    
    public String encontrarDoctor(String especialidad, String fecha){
        ArrayList<Integer> horas = new ArrayList();
        String doctor = "No se encontró un doctor.";
        try {
            //Seleccionar horas dentro de la fecha especificada para el doctor
            System.out.println(especialidad);
            PreparedStatement st = connect.prepareStatement("select * from usuarios where especialidad = '" + especialidad.toLowerCase() + "';");
            
            ResultSet result = null;
            result = st.executeQuery();
            //Verificar todo
            while(result.next()){
                doctor = result.getString("usuario");
                horas = encontrarHoras(doctor,fecha);
                if(horas.size() < 8){
                    System.out.print("Las horas disponibles son: ");
                    for (int i = 10; i < 19; i++) {
                        for (int j = 0; j < horas.size(); j++) {
                            if(horas.get(j) != i){
                            System.out.print(i + ", ");
                        }
                        }
                    }
                    return doctor;
                }
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doctor;
    }
    
    public ArrayList<Integer> encontrarHoras(String doctor,String fecha){
        ArrayList<Integer> horas = new ArrayList();
        try {
            //Seleccionar horas dentro de la fecha especificada para el doctor
            PreparedStatement st = connect.prepareStatement("select hora from citas where fecha = '" + fecha.toLowerCase() + "' and doctor in (select doctor from citas where doctor = '"+ doctor.toLowerCase() + "');");
            
            ResultSet result = null;
            result = st.executeQuery();
            //Verificar todo
            while(result.next()){
                horas.add(result.getInt("hora"));
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return horas;
    }
    
    public void checarCitas(String usuario){
        try {
            System.out.println(usuario);
            //Seleccionar horas dentro de la fecha especificada para el doctor
            PreparedStatement st = connect.prepareStatement("select * from citas where usuario  = '" + usuario.toLowerCase() + "' or doctor = '" + usuario + "';");
            
            ResultSet result = null;
            result = st.executeQuery();
            System.out.println("Citas: ");
            //Verificar todo
            while(result.next()){
                System.out.println("Nombre: " + result.getString("nombre") + " Fecha: " + result.getString("fecha") + " Hora: " + result.getString("hora") + " Doctor: " + result.getString("doctor"));
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void crearCita(String paciente, String fecha, int hora, String doctor, String usuario){
        try {
            //Seleccionar horas dentro de la fecha especificada para el doctor
            PreparedStatement st = connect.prepareStatement("insert into citas(nombre, doctor, fecha, hora, usuario) values (?,?,?,?,?)");
            st.setString(1, paciente.toLowerCase());
            st.setString(2, doctor.toLowerCase());
            st.setString(3, fecha.toLowerCase());
            st.setInt(4, hora);
            st.setString(5, usuario);
            
            st.execute();            
            System.out.println("La cita se creó perfectamente.\n");
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void crearUsuarioDoctor(String nombre, String rol, String especialidad, String usuario, String clave){
        try {
            //Seleccionar horas dentro de la fecha especificada para el doctor
            PreparedStatement st = connect.prepareStatement("insert into citas(nombre, rol, especialidad, usuario, clave) values (?,?,?,?, ?)");
            st.setString(1, nombre.toLowerCase());
            st.setString(2, rol.toLowerCase());
            st.setString(3, especialidad.toLowerCase());
            st.setString(4, usuario.toLowerCase());
            st.setString(5, clave);
            
            st.execute();            
            System.out.println("El doctor se registró perfectamente.\n");
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void crearUsuario(String nombre, String rol, String usuario, String clave){
        try {
            //Seleccionar horas dentro de la fecha especificada para el doctor
            PreparedStatement st = connect.prepareStatement("insert into citas(nombre, rol, usuario, clave) values (?,?,?,?)");
            st.setString(1, nombre);
            st.setString(2, rol);
            st.setString(3, usuario);
            st.setString(4, clave);
            
            st.execute();            
            System.out.println("El doctor se registró perfectamente.\n");
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
