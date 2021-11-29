/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonel
 * Objetivo: Programas las rutinas SQL para hacer insercion, consultam eliminacionn y actualizacion
 *          Codigos de conexion entre el lenguaje JAVA y DDBMS MySQL
 */
public class BaseDatos {
    Connection conexion;
    Statement transaccion;
    ResultSet cursor;
    
    public BaseDatos(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Practica1?zeroDateTimeBehavior=CONVERT_TO_NULL","root","");
            transaccion = conexion.createStatement();
        }catch(SQLException ex){
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean insertar(Producto producto){
        String SQL_Insert = "INSERT INTO PRODUCTOS VALUES (NULL, '%DESC%','%PRE%','%EXS%')";
        SQL_Insert = SQL_Insert.replaceAll("%DESC%", producto.descripcion);
        SQL_Insert = SQL_Insert.replaceAll("%PRE%", Float.toString(producto.precio));
        SQL_Insert = SQL_Insert.replaceAll("%EXS%", Integer.toString(producto.existencia));
        
        
        try {
            transaccion.execute(SQL_Insert);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    public ArrayList<String[]>consultarTodos(){
        ArrayList<String[]> resultado = new ArrayList<String[]>();
        try {
            cursor = transaccion.executeQuery("select * from productos");
            if(cursor.next()){
                do{
                    String[] renglon = {cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)};
                    resultado.add(renglon);
                }while(cursor.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }
    
    public Producto consultarId(String ID){
        Producto productoResultado = new Producto();
        
        try {
            cursor = transaccion.executeQuery("select * from productos where id="+ID);
            if(cursor.next()){
                productoResultado.descripcion = cursor.getString(2); productoResultado.precio = cursor.getFloat(3); productoResultado.existencia = cursor.getInt(4);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productoResultado;
    }
    
    
    public boolean eliminar(String ID){
        try {
            transaccion.execute("delete from productos where id="+ID);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
    public boolean actualizar(Producto producto){
        String update = "update productos set descripcion= '%DESC%', precio = '%PRE%', existencia = '%EXS%' where id ="+producto.id;
        update = update.replaceAll("%DESC%", producto.descripcion);
        update = update.replaceAll("%PRE%", Float.toString(producto.precio));
        update = update.replaceAll("%EXS%", Integer.toString(producto.existencia));
        
        
        try {
            transaccion.executeUpdate(update);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    
}
