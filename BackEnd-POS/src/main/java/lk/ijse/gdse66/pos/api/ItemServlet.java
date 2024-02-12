package lk.ijse.gdse66.pos.api;

import jakarta.json.*;
import lk.ijse.gdse66.pos.bo.BOFactory;
import lk.ijse.gdse66.pos.bo.custom.ItemBO;
import lk.ijse.gdse66.pos.bo.custom.QueryBO;
import lk.ijse.gdse66.pos.dto.CustomerDTO;
import lk.ijse.gdse66.pos.dto.ItemDTO;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 2:11 PM - 1/16/2024
 **/
@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);
    private final QueryBO queryBO = (QueryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOM);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource pool;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        int qty = Integer.parseInt(req.getParameter("qty"));
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));

        ItemDTO itemDTO = new ItemDTO(code, description, qty, unitPrice);
        try (Connection connection = pool.getConnection()) {
            boolean itemSaved = itemBO.saveItem(itemDTO, connection);

            JsonObjectBuilder response = Json.createObjectBuilder();
            if (itemSaved) {
                response.add("status", "200 OK");
                response.add("message", "Added Successfully...!");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.add("status", "Error 500");
                response.add("message", "Failed to add the item");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().print(response.build());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", "Error 500");
            response.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader jsonReader = Json.createReader(req.getReader());
        JsonObject jsonObject = jsonReader.readObject();

        String code = jsonObject.getString("code");
        String description = jsonObject.getString("description");
        int qty = Integer.parseInt(jsonObject.getString("qty"));
        double unitPrice = Double.parseDouble(jsonObject.getString("unitPrice"));

        ItemDTO itemDTO = new ItemDTO(code, description, qty, unitPrice);
        try (Connection connection = pool.getConnection()) {
            boolean itemUpdated = itemBO.updateItem(itemDTO, connection);

            JsonObjectBuilder response = Json.createObjectBuilder();
            if (itemUpdated) {
                response.add("status", "200 OK");
                response.add("message", "Updated Successfully...!");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.add("status", "Error 500");
                response.add("message", "Failed to update the item");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().print(response.build());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", "Error 500");
            response.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader jsonReader = Json.createReader(req.getReader());
        JsonObject jsonObject = jsonReader.readObject();
        String code = jsonObject.getString("code");

        try (Connection connection = pool.getConnection()) {
            boolean itemDeleted = itemBO.deleteItem(code, connection);

            JsonObjectBuilder response = Json.createObjectBuilder();
            if (itemDeleted) {
                response.add("status", "200 OK");
                response.add("message", "Deleted Successfully...!");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.add("status", "Error 500");
                response.add("message", "Failed to delete the item");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            resp.getWriter().print(response.build());

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", "Error 500");
            response.add("message", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String option = req.getParameter("option");

        switch (option) {
            case "itemCount":
                try (Connection connection = pool.getConnection()) {
                    int count = queryBO.getItem(connection);

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("count", count);
                    resp.getWriter().print(response.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", "Error 500");
                    response.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(response.build());
                }
                break;

            case "searchItemCode":
                try (Connection connection = pool.getConnection()) {
                    ArrayList<ItemDTO> itemArrayList = itemBO.itemSearchId(code, connection);

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    if (!itemArrayList.isEmpty()) {
                        for (ItemDTO itemDetails : itemArrayList) {
                            response.add("code", itemDetails.getCode());
                            response.add("description", itemDetails.getDescription());
                            response.add("qty", itemDetails.getQty());
                            response.add("unitPrice", itemDetails.getUnitPrice());
                        }

                    } else {
                        response.add("status", "Error 500");
                        response.add("message", "Failed to search the item");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    resp.getWriter().print(response.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", "Error 500");
                    response.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(response.build());
                }
                break;

            case "loadAllItems":
                try (Connection connection = pool.getConnection()) {
                    ArrayList<ItemDTO> itemArrayList = itemBO.getAllItems(connection);
                    JsonArrayBuilder jsonAllItemsArrayBuilder = Json.createArrayBuilder();

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    for (ItemDTO itemDTO : itemArrayList) {
                        response.add("code", itemDTO.getCode());
                        response.add("description", itemDTO.getDescription());
                        response.add("qty", itemDTO.getQty());
                        response.add("unitPrice", itemDTO.getUnitPrice());
                        jsonAllItemsArrayBuilder.add(response.build());
                    }

                    response.add("status", "200 OK");
                    response.add("message", "Loaded Successfully...!");
                    response.add("data", jsonAllItemsArrayBuilder.build());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().print(response.build());

                } catch (ClassNotFoundException | SQLException e) {
                    JsonObjectBuilder rjo = Json.createObjectBuilder();
                    rjo.add("state", "Error");
                    rjo.add("message", e.getLocalizedMessage());
                    rjo.add("data", "");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(rjo.build());
                }
                break;

            case "generateItemCode":
                try (Connection connection = pool.getConnection()) {
                    String itemCode = itemBO.generateNewItemCode(connection);

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("code", itemCode);
                    resp.getWriter().print(response.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", "Error 500");
                    response.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(response.build());
                }
                break;
        }
    }
}