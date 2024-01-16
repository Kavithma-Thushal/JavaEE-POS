package lk.ijse.gdse66.pos.api;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import lk.ijse.gdse66.pos.dto.CustomerDTO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/
@WebServlet(urlPatterns = "/customers", loadOnStartup = 1, initParams = {
        @WebInitParam(name = "username", value = "root"),
        @WebInitParam(name = "password", value = "1234"),
        @WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/javaee_pos")})
public class CustomerServlet extends HttpServlet {
    private String username;
    private String password;
    private String url;

    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        username = servletConfig.getInitParameter("username");
        password = servletConfig.getInitParameter("password");
        url = servletConfig.getInitParameter("url");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;

        /*Using Query Parameters*/
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        double salary = Double.parseDouble(req.getParameter("salary"));

        /*Using JSON-P Object*/
        /*JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        String salary = jsonObject.getString("salary");*/

        /*Using Json-B Object*/
        /*Jsonb jsonb = JsonbBuilder.create();
        CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);   // JSON Object ---> Java Object
        String id = customerDTO.getId();
        String name = customerDTO.getName();
        String address = customerDTO.getAddress();
        String salary = customerDTO.getContact();*/

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO customer(id, name, address, salary) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setDouble(4,salary);
            int rowsEffected = preparedStatement.executeUpdate();

            if (rowsEffected != 0) {
                resp.getWriter().println("Customer Saved Successfully!");
                System.out.println("Customer Saved Successfully!");
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer Saved Error!");
                resp.getWriter().println("Customer Saved Error!");
                System.out.println("Customer Saved Error!");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}