/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import control.Usuario;
import interfaz.UI_usadoLogin.ContraseñaIncorrecta;
import interfaz.UI_usadoLogin.UsuarioIncorrecto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class controladorUsuario {

    public static Usuario login(String nombre, String password) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            return null;
        }

        String query = "SELECT * FROM usuarios WHERE nombre = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("pass");
                if (storedPassword.equals(password)) {
                    return new Usuario(
                            resultSet.getString("nombre"),
                            resultSet.getString("correo"),
                            resultSet.getString("pass"),
                            resultSet.getString("rol")
                    );
                } else {
                    ContraseñaIncorrecta Ccontraseña=new ContraseñaIncorrecta();
                    Ccontraseña.setVisible(true);
                }
            } else {
                UsuarioIncorrecto n=new UsuarioIncorrecto();
                n.setVisible(true);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario.");
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }

        return null;
    }

    public static boolean resetPassword(String nombre, String newPassword) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            return false;
        }

        String query = "UPDATE usuarios SET pass = ? WHERE nombre = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newPassword);
            statement.setString(2, nombre);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
              
                return true;
            } else {
                 UsuarioIncorrecto n=new UsuarioIncorrecto();
                n.setVisible(true);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }

        return false;
    }

}
