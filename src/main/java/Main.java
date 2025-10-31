import java.sql.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        final String url = "jdbc:sqlite:C:/db/dataDB/sqlite/empresa.db";

        //Creamos la conexión con la base de datos en un try con recursos
        try (Connection conexion = DriverManager.getConnection(url)) {
            //Llamámos al metodo el cual inserta y comprueba los datos antes de insertarlos
            insertarEmpleado(
                    Integer.parseInt(args[args.length - 1]), //numero departemento
                    Integer.parseInt(args[0]), //numero de empleado
                    Double.parseDouble(args[5]), //salario
                    Integer.parseInt(args[3]), //numero director
                    args[1], //apellido
                    args[2], //oficio
                    Double.parseDouble(args[6]), //comision
                    conexion //la conexion a la base de datos
            );
        } catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Metodo que antes de insertar los datos comprueba los mismos
    private static void insertarEmpleado(int numeroDepartamento, int numeroEmpleado, double salario, int numeroDirector, String apellido, String oficio, double comision, Connection connection) throws SQLException{
        String mensaje = "";
        if(!comprobarExistenciaDepartamento(numeroDepartamento, connection)){
            mensaje = "El departamento "+numeroDepartamento+" no existe.";
        } if (comprobarExistenciaNumeroEmpleado(numeroEmpleado, connection)){
            mensaje = "El empleado "+numeroEmpleado+" ya existe.";
        } if (!comprobarSalarioValido(salario)){
            mensaje = "El salario ("+salario+"€) no es válido.";
        } if (!comprobarExistenciaNumeroEmpleado(numeroDirector, connection)){
            mensaje = "El director "+numeroDirector+" no existe.";
        } if (apellido == null || apellido.trim().isEmpty()){
            mensaje = "El apellido no puede estar vacío.";
        } if (oficio == null || oficio.trim().isEmpty()){
            mensaje = "El oficio no puede estar vacío.";
        }

        if (mensaje.isEmpty()) {
            String sql = "INSERT INTO EMPLEADOS VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, numeroEmpleado);
            ps.setString(2, apellido);
            ps.setString(3, oficio);
            ps.setInt(4, numeroDirector);
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setDouble(6, salario);
            ps.setDouble(7, comision);
            ps.setInt(8, numeroDepartamento);
            ps.executeUpdate();
            System.out.println("Insert realizado correctamente.");
        } else {
            System.out.println(mensaje);
        }
    }

    //Comprueba que el salario sea > 0
    private static boolean comprobarSalarioValido(double salario){
        return salario > 0;
    }

    //Comprueba que el numero de empleado existe
    private static boolean comprobarExistenciaNumeroEmpleado(int numeroEmpleado, Connection connection)  throws SQLException{
        String sql = "SELECT 1 FROM EMPLEADOS WHERE EMP_NO = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, numeroEmpleado);
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    //Comprueba que el departamento indicado existe
   private static boolean comprobarExistenciaDepartamento(int numeroDepartamento, Connection conexion) throws SQLException {
       String sql = "SELECT 1 FROM DEPARTAMENTOS WHERE DEPT_NO = ?";
       PreparedStatement ps = conexion.prepareStatement(sql);
       ps.setInt(1, numeroDepartamento);
       ResultSet rs = ps.executeQuery();

       return rs.next();
   }
}