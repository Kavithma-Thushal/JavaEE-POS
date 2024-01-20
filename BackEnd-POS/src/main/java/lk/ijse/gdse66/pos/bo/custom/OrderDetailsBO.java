package lk.ijse.gdse66.pos.bo.custom;

import lk.ijse.gdse66.pos.bo.SuperBO;
import lk.ijse.gdse66.pos.dto.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public interface OrderDetailsBO extends SuperBO {
    ArrayList<OrderDetailDTO> getAllOrderDetails(Connection connection) throws SQLException, ClassNotFoundException;

    boolean purchaseOrderDetails(OrderDetailDTO dto, Connection connection) throws SQLException, ClassNotFoundException;
}
