package lk.ijse.gdse66.pos.api;

import jakarta.json.*;
import lk.ijse.gdse66.pos.bo.BOFactory;
import lk.ijse.gdse66.pos.bo.custom.OrderBO;
import lk.ijse.gdse66.pos.bo.custom.OrderDetailsBO;
import lk.ijse.gdse66.pos.bo.custom.QueryBO;
import lk.ijse.gdse66.pos.dto.OrderDTO;
import lk.ijse.gdse66.pos.dto.OrderDetailDTO;

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
 * @since : 2:19 PM - 1/16/2024
 **/
@WebServlet(urlPatterns = "/orders")
public class OrdersServlet extends HttpServlet {
    private final OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERS);
    private final OrderDetailsBO orderDetailBO = (OrderDetailsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ORDERDETAILS);
    private final QueryBO queryBO = (QueryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOM);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource pool;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String option = req.getParameter("option");

        JsonArrayBuilder allOrders = Json.createArrayBuilder();
        JsonArrayBuilder allOrderDetails = Json.createArrayBuilder();

        switch (option) {
            case "ordersCount":
                try (Connection connection = pool.getConnection()) {
                    int count = queryBO.getSumOrders(connection);

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

            case "OrderIdGenerate":
                try (Connection connection = pool.getConnection()) {
                    String orderId = orderBO.generateNewOrder(connection);

                    JsonObjectBuilder ordID = Json.createObjectBuilder();
                    ordID.add("orderId", orderId);
                    resp.getWriter().print(ordID.build());

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", "Error 500");
                    response.add("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(response.build());
                }
                break;

            case "LoadOrders":
                try (Connection connection = pool.getConnection()) {
                    ArrayList<OrderDTO> orderArrayList = orderBO.getAllOrders(connection);

                    for (OrderDTO orderDTO : orderArrayList) {
                        JsonObjectBuilder order = Json.createObjectBuilder();
                        order.add("orderId", orderDTO.getId());
                        order.add("date", orderDTO.getDate());
                        order.add("cusId", orderDTO.getCustomerId());
                        allOrders.add(order.build());
                    }

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("state", "200 OK");
                    response.add("message", "Loaded Successfully...!");
                    response.add("data", allOrders.build());
                    resp.setStatus(HttpServletResponse.SC_OK);
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

            case "LoadOrderDetails":
                try (Connection connection = pool.getConnection()) {
                    ArrayList<OrderDetailDTO> orderDetailsArrayList = orderDetailBO.getAllOrderDetails(connection);

                    for (OrderDetailDTO customerDTO : orderDetailsArrayList) {
                        JsonObjectBuilder orderDetails = Json.createObjectBuilder();
                        orderDetails.add("OrderId", customerDTO.getOrderId());
                        orderDetails.add("code", customerDTO.getItemCode());
                        orderDetails.add("qty", customerDTO.getQty());
                        orderDetails.add("unitPrice", customerDTO.getTotal());
                        allOrderDetails.add(orderDetails.build());
                    }

                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("state", "200 OK");
                    response.add("message", "Loaded Successfully...!");
                    response.add("data", allOrderDetails.build());
                    resp.setStatus(HttpServletResponse.SC_OK);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");

        String customerId = jsonObject.getString("customerId");
        String date = jsonObject.getString("date");
        String orderId = jsonObject.getString("orderId");

        try (Connection connection = pool.getConnection()) {
            connection.setAutoCommit(false);

            OrderDTO orderDTO = new OrderDTO(orderId, date, customerId);
            boolean b = orderBO.purchaseOrder(orderDTO, connection);
            if (!(b)) {
                connection.rollback();
                connection.setAutoCommit(true);

                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "Error");
                rjo.add("message", "Order Issue");
                rjo.add("data", "");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().print(rjo.build());

            } else {
                for (JsonValue orderDetail : oDetail) {
                    JsonObject object = orderDetail.asJsonObject();

                    String orId = object.getString("orderId");
                    String itId = object.getString("itemId");
                    int qty = Integer.parseInt(object.getString("qty"));
                    double price = Double.parseDouble(object.getString("unitPrice"));

                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(orId, itId, qty, price);
                    boolean b1 = orderDetailBO.purchaseOrderDetails(orderDetailDTO, connection);

                    if (!(b1)) {
                        connection.rollback();
                        connection.setAutoCommit(true);

                        JsonObjectBuilder rjo = Json.createObjectBuilder();
                        rjo.add("state", "Error");
                        rjo.add("message", "Order Details Issue");
                        rjo.add("data", "");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().print(rjo.build());
                    }
                }
                connection.commit();
                connection.setAutoCommit(true);

                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("state", "Ok");
                job.add("message", "Successfully Place Order..!");
                job.add("data", "");
                resp.getWriter().print(job.build());
            }

        } catch (SQLException | ClassNotFoundException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state", "Error");
            rjo.add("message", e.getLocalizedMessage());
            rjo.add("data", "");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray oDetail = jsonObject.getJsonArray("detail");

        try (Connection connection = pool.getConnection()) {
            for (JsonValue orderDetail : oDetail) {
                JsonObject object = orderDetail.asJsonObject();

                String orId = object.getString("orderId");
                String itId = object.getString("itemId");
                int qty = Integer.parseInt(object.getString("qty"));
                double price = Double.parseDouble(object.getString("unitPrice"));

                orderBO.mangeItems(qty, itId, connection);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}