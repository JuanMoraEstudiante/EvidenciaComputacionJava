/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evidenciafinal;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author juanc
 * usuario admin = admin
 * contraseña admin = admin1
 */
public class EvidenciaFinal {
    
    public static int menuSesion(){
        
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        
        System.out.println("#####Inicio Sesión#####");
        System.out.println("# 1.Iniciar Sesión.   #");
        System.out.println("# 2.Registrar usuario #");
        System.out.println("#######################");
        
        try{
            opcion = sc.nextInt();
        }catch(Exception e){
            menuSesion();
        }
        
        if(opcion != 1 && opcion != 2){
            menuSesion();
        }
        
        return opcion;
    }

    public static ArrayList iniciarSesion(Conexion con){
        
        ArrayList usuarioG =  new ArrayList();
        
        String usuario, clave;
        Scanner sc =  new Scanner(System.in);
        System.out.print("Ingresa tu usuario: ");
        usuario = sc.nextLine();
        System.out.print("Ingresa tu contraseña: ");
        clave = sc.nextLine();
        
        usuarioG = con.iniciarSesion(usuario, clave);
        
        if(usuarioG.get(0).toString().equals("Iniciar")){
            System.out.println("Bienvenido " + usuario + ", ¿qué desea realizar?");
            return usuarioG;
        }else{
            System.out.println("El usuario o contraseña fueron incorrectos, intente nuevamente.");
            return iniciarSesion(con);
        }
    }
    
    public static int menu(String rol){
        if(rol.toLowerCase().equals("paciente")){
            
            Scanner sc = new Scanner(System.in);
            int opcion = 0;
            
            System.out.println("######### Menú #########");
            System.out.println("# 1.Crear cita.        #");
            System.out.println("# 2.Checar mis citas.  #");
            System.out.println("# 3.Cerrar sesión.     #");
            System.out.println("########################");
            
            try{
                opcion = sc.nextInt();
            }catch(Exception e){
                menu(rol);
            }

            if(opcion != 1 && opcion != 2 && opcion!=3){
                menu(rol);
            }

            return opcion;
        }else if(rol.toLowerCase().equals("doctor")){
            Scanner sc = new Scanner(System.in);
            int opcion = 0;
            
            System.out.println("######### Menú #########");
            System.out.println("# 1.Checar mis citas.  #");
            System.out.println("# 2.Cerrar sesión.     #");
            System.out.println("########################");
            
            try{
                opcion = sc.nextInt();
            }catch(Exception e){
                menu(rol);
            }

            if(opcion != 1 && opcion != 2 && opcion!=3){
                menu(rol);
            }
            
            if(opcion == 2){
                opcion = 3;
            }else if(opcion == 1){
                opcion = 2;
            }

            return opcion;
        }else{
            Scanner sc = new Scanner(System.in);
            int opcion = 0;
            
            System.out.println("######### Menú #########");
            System.out.println("# 1.Crear Doctor       #");
            System.out.println("# 2.Cerrar sesión.     #");
            System.out.println("########################");
            
            try{
                opcion = sc.nextInt();
            }catch(Exception e){
                menu(rol);
            }

            if(opcion != 1 && opcion != 2 && opcion!=3){
                menu(rol);
            }
            
            if(opcion == 1){
                opcion = 4;
            }else if(opcion == 2){
                opcion = 3;
            }

            return opcion;
        }
    }
    
    public static int elegirEspecialidad(){
        
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        
        System.out.println("######### Especialidades #########");
        System.out.println("#           1.Pediatra           #");
        System.out.println("#            2.General           #");
        System.out.println("#          3.Ginecología         #");
        System.out.println("##################################");
        
        try{
            opcion = sc.nextInt();
            }catch(Exception e){
                elegirEspecialidad();
            }
        
        if(opcion != 1 && opcion != 2 && opcion!=3){
                elegirEspecialidad();
        }
        
        return opcion;
    }
    
    public static void main(String[] args) {
        
        //Variables
        boolean exitApp = false;
        Scanner sc = new Scanner(System.in);
        ArrayList usuario;
        int menuSesion, menuOpciones;
        
        //Programa
        Conexion con = new Conexion();
        con.connect();
        con.verificarTablas();
        //Iniciar Sesión
        menuSesion = menuSesion();
        if(menuSesion == 2){
            String nombreIn, usuarioIn, claveIn;
            System.out.print("Ingresa tu nombre: ");
            nombreIn = sc.nextLine();
            System.out.print("Ingresa tu usuario: ");
            usuarioIn = sc.nextLine();
            System.out.print("Ingresa tu clave: ");
            claveIn = sc.nextLine();
            
            con.crearUsuario(nombreIn, "paciente", usuarioIn, claveIn);
        }
        usuario = iniciarSesion(con);
        do{
            //Desplegar menú
          menuOpciones = menu(usuario.get(2).toString());
          
          switch(menuOpciones){
              case 1: 
                  String paciente, fecha, doctor, especialidad = "";
                  int opcion, hora;
          
                  System.out.print("Ingresa tu nombre: ");
                  paciente = sc.nextLine();
                  System.out.print("Ingresa la fecha deseada: ");
                  fecha = sc.nextLine();
                  opcion = elegirEspecialidad();
                  
                  switch(opcion){
                      case 1: 
                          especialidad = "Pediatra";
                          break;
                      case 2:
                          especialidad = "General";
                          break;
                      case 3:
                          especialidad = "Ginecologa";
                  }
                  System.out.print("Horas disponibles: ");
                  doctor = con.encontrarDoctor(especialidad, fecha);
                  System.out.print("Selecciona tu hora: ");
                  
                  hora = sc.nextInt();

                  con.crearCita(paciente, fecha, hora, doctor, usuario.get(1).toString());
                  break;
              case 2:
                  con.checarCitas(usuario.get(1).toString());
                  break;
              case 3:
                  exitApp = true;
                  break;
              case 4:
                  String nombreI, rolI, doctorI, especialidadI = "", usuarioI, claveI;
                  int opcionI;
          
                  System.out.print("Ingresa el nombre del doctor: ");
                  nombreI = sc.nextLine();
                  System.out.print("Ingresa el rol del doctor: ");
                  rolI = sc.nextLine();
                  System.out.print("Ingresa el usuario del doctor: ");
                  usuarioI = sc.nextLine();
                  System.out.print("Ingresa la contraseña deseada: ");
                  claveI = sc.nextLine();
                  
                  opcionI = elegirEspecialidad();
                  
                  switch(opcionI){
                      case 1: 
                          especialidadI = "Pediatra";
                          break;
                      case 2:
                          especialidadI = "Genaral";
                          break;
                      case 3:
                          especialidadI = "Ginecologa";
                  }
                  
                  
                  hora = sc.nextInt();

                  con.crearUsuarioDoctor(nombreI, rolI, especialidadI, usuarioI, claveI);
                  break;
          }
        
        }while(exitApp == false);
        //Cerrar base de datos
        con.close();
    }
    
}
