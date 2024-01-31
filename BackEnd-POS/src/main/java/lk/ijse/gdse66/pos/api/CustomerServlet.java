package lk.ijse.gdse66.pos.api;

import jakarta.json.*;
import lk.ijse.gdse66.pos.bo.BOFactory;
import lk.ijse.gdse66.pos.bo.custom.CustomerBO;
import lk.ijse.gdse66.pos.bo.custom.QueryBO;
import lk.ijse.gdse66.pos.dto.CustomerDTO;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/
@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    private final QueryBO queryBO = (QueryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOM);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource dataSource;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        double salary = Double.parseDouble(req.getParameter("salary"));

        CustomerDTO customerDTO = new CustomerDTO(id, name, address, salary);
        try (Connection connection = dataSource.getConnection()) {
            boolean customerSaved = customerBO.saveCustomer(customerDTO, connection);

            if (customerSaved) {
                JsonObjectBuilder successResponse = Json.createObjectBuilder();
                successResponse.add("status", "200 OK");
                successResponse.add("message", "Added Successfully...!");
                resp.getWriter().print(successResponse.build());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("Failed to save customer");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder errorResponse = Json.createObjectBuilder();
            errorResponse.add("status", "Error");
            errorResponse.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(errorResponse.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader jsonReader = Json.createReader(req.getReader());
        JsonObject jsonObject = jsonReader.readObject();

        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        double salary = Double.parseDouble(jsonObject.getString("salary"));

        CustomerDTO customerDTO = new CustomerDTO(id, name, address, salary);
        try (Connection connection = dataSource.getConnection()) {
            boolean customerUpdated = customerBO.updateCustomer(customerDTO, connection);

            if (customerUpdated) {
                JsonObjectBuilder successResponse = Json.createObjectBuilder();
                successResponse.add("status", "200 OK");
                successResponse.add("message", "Updated Successfully...!");
                resp.getWriter().print(successResponse.build());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("Failed to update customer");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder errorResponse = Json.createObjectBuilder();
            errorResponse.add("status", "Error");
            errorResponse.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(errorResponse.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");

        try (Connection connection = dataSource.getConnection()) {
            boolean customerDeleted = customerBO.deleteCustomer(id, connection);

            if (customerDeleted) {
                JsonObjectBuilder successResponse = Json.createObjectBuilder();
                successResponse.add("status", "200 OK");
                successResponse.add("message", "Deleted Successfully...!");
                resp.getWriter().print(successResponse.build());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("Failed to delete customer");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder errorResponse = Json.createObjectBuilder();
            errorResponse.add("status", "Error");
            errorResponse.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(errorResponse.build());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String option = req.getParameter("option");

        switch (option) {
            case "searchCusId":
                try (Connection connection = dataSource.getConnection()) {
                    ArrayList<CustomerDTO> arrayList = customerBO.customerSearchId(id, connection);

                    if (!arrayList.isEmpty()) {
                        for (CustomerDTO customerDTO : arrayList) {
                            JsonObjectBuilder successResponse = Json.createObjectBuilder();
                            successResponse.add("id", customerDTO.getId());
                            successResponse.add("name", customerDTO.getName());
                            successResponse.add("address", customerDTO.getAddress());
                            successResponse.add("salary", customerDTO.getSalary());
                            resp.getWriter().print(successResponse.build());
                        }

                    } else {
                        JsonObjectBuilder errorResponse = Json.createObjectBuilder();
                        errorResponse.add("status", "Error");
                        errorResponse.add("message", "Customer");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().print(errorResponse.build());
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder errorResponse = Json.createObjectBuilder();
                    errorResponse.add("status", "Error");
                    errorResponse.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(errorResponse.build());
                }
                break;

            case "loadAllCustomer":
                try (Connection connection = dataSource.getConnection()) {
                    JsonArrayBuilder jsonAllCustomersArray = Json.createArrayBuilder();
                    ArrayList<CustomerDTO> arrayList = customerBO.getAllCustomers(connection);

                    for (CustomerDTO customerDTO : arrayList) {
                        JsonObjectBuilder successResponse = Json.createObjectBuilder();
                        successResponse.add("id", customerDTO.getId());
                        successResponse.add("name", customerDTO.getName());
                        successResponse.add("address", customerDTO.getAddress());
                        successResponse.add("salary", customerDTO.getSalary());
                        jsonAllCustomersArray.add(successResponse.build());
                    }

                    JsonObjectBuilder successResponse = Json.createObjectBuilder();
                    successResponse.add("status", "200 OK");
                    successResponse.add("message", "Loaded Successfully...!");
                    successResponse.add("data", jsonAllCustomersArray.build());
                    resp.getWriter().print(successResponse.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder errorResponse = Json.createObjectBuilder();
                    errorResponse.add("status", "Error");
                    errorResponse.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(errorResponse.build());
                }
                break;

            case "CustomerIdGenerate":
                try (Connection connection = dataSource.getConnection()) {
                    String cID = customerBO.generateNewCustomerID(connection);

                    JsonObjectBuilder CusId = Json.createObjectBuilder();
                    CusId.add("id", cID);
                    resp.getWriter().print(CusId.build());

                } catch (SQLException | ClassNotFoundException e) {
                    JsonObjectBuilder rjo = Json.createObjectBuilder();
                    rjo.add("state", "Error");
                    rjo.add("message", e.getLocalizedMessage());
                    rjo.add("data", "");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(rjo.build());
                }
                break;

            case "CustomerCount":
                try (Connection connection = dataSource.getConnection()) {
                    int count = queryBO.getCustomer(connection);
                    JsonObjectBuilder successResponse = Json.createObjectBuilder();
                    successResponse.add("count", count);
                    resp.getWriter().print(successResponse.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder errorResponse = Json.createObjectBuilder();
                    errorResponse.add("status", "Error");
                    errorResponse.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(errorResponse.build());
                }
                break;
        }
    }
}