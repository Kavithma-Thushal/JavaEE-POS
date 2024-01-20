package lk.ijse.gdse66.pos.bo.custom.impl;

import lk.ijse.gdse66.pos.bo.custom.OrderDetailsBO;
import lk.ijse.gdse66.pos.dao.DAOFactory;
import lk.ijse.gdse66.pos.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse66.pos.dto.OrderDetailDTO;
import lk.ijse.gdse66.pos.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:30 PM - 1/16/2024
 **/
public class OrderDetailsBOImpl implements OrderDetailsBO {
    private final OrderDetailsDAO orderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);

    @Override
    public boolean purchaseOrderDetails(OrderDetailDTO dto, Connection connection) throws SQLException, ClassNotFoundException {
        return orderDetailsDAO.save(new OrderDetail(dto.getOrderId(), dto.getItemCode(), dto.getQty(), dto.getTotal()), connection);
    }

    @Override
    public ArrayList<OrderDetailDTO> getAllOrderDetails(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetail> all = orderDetailsDAO.getAll(connection);

        ArrayList<OrderDetailDTO> allOrderDetails = new ArrayList<>();
        for (OrderDetail orderDetail : all) {
            allOrderDetails.add(new OrderDetailDTO(orderDetail.getOrderId(), orderDetail.getItemCode(), orderDetail.getQty(), orderDetail.getTotal()));
        }
        return allOrderDetails;
    }
}