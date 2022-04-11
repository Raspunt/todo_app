import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.sql.*;
import java.util.ArrayList;

public class dbCon{

    Connection c = null;
    Statement stmt = null;

    public Connection ConnectToDb(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:todos.db");
            //System.out.println("Opened database successfully");



        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }


    public void CreateTable(){

        try {
            c = ConnectToDb();
            stmt = c.createStatement();

            String sql = "CREATE TABLE  IF NOT EXISTS  todosDb (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " task text NOT NULL, " +
                    " status text NOT NULL " +
                    ")" ;

            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertToDb(String task ,String status) {
        try {

            c = ConnectToDb();
            stmt = c.createStatement();
            String sql = String.format("INSERT INTO todosDb (task,status) " +
                    "VALUES ('%s' , '%s');",task,status);

            stmt.executeUpdate(sql);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getTodosJson(){
        String responseJson = "";
        ArrayList<Todo> todoArr = new ArrayList<>();
        try {
            c = ConnectToDb();
            stmt = c.createStatement();
            String sql = "SELECT * FROM todosDb;";
            ResultSet rs = stmt.executeQuery(sql);

            while ( rs.next()) {
                String  task = rs.getString("task");
                String  status = rs.getString("status");

                todoArr.add(new Todo(task,status));
            }

            Gson gson = new Gson();

            responseJson = gson.toJson(todoArr);



            return responseJson;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    return responseJson;
    }




}
