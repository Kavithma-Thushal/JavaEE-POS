package lk.ijse.gdse66.pos.bo.custom.impl;

import lk.ijse.gdse66.pos.bo.custom.OrderBO;
import lk.ijse.gdse66.pos.dao.DAOFactory;
import lk.ijse.gdse66.pos.dao.custom.OrderDAO;
import lk.ijse.gdse66.pos.dto.OrderDTO;
import lk.ijse.gdse66.pos.entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:30 PM - 1/16/2024
 **/
public class OrderBOImpl implements OrderBO {
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERS);

    @Override
    public boolean purchaseOrder(OrderDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.save(new Orders(dto.getId(), dto.getDate(), dto.getCustomerId()), connection);
    }

    @Override
    public boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.mangeItems(qty, code, connection);
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> all = orderDAO.getAll(connection);

        ArrayList<OrderDTO> allOrders = new ArrayList<>();
        for (Orders orders : all) {
            allOrders.add(new OrderDTO(orders.getOrderId(), orders.getOrderDate(), orders.getCusId()));
        }
        return allOrders;
    }

    @Override
    public String generateNewOrder(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewID(connection);
    }
}