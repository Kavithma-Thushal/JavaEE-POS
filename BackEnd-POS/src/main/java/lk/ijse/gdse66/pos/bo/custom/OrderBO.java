package lk.ijse.gdse66.pos.bo.custom;

import lk.ijse.gdse66.pos.bo.SuperBO;
import lk.ijse.gdse66.pos.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public interface OrderBO extends SuperBO {
    boolean purchaseOrder(OrderDTO dto, Connection connection) throws SQLException, ClassNotFoundException;

    boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrders(Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewOrder(Connection connection) throws SQLException, ClassNotFoundException;
}
