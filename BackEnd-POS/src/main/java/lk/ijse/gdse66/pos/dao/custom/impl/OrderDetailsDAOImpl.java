package lk.ijse.gdse66.pos.dao.custom.impl;

import lk.ijse.gdse66.pos.dao.custom.OrderDetailsDAO;
import lk.ijse.gdse66.pos.entity.OrderDetail;
import lk.ijse.gdse66.pos.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:32 PM - 1/16/2024
 **/
public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public boolean save(OrderDetail orderDetailDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO OrderDetail VALUES(?,?,?,?)", orderDetailDTO.getOrderId(), orderDetailDTO.getItemCode(), orderDetailDTO.getQty(), orderDetailDTO.getTotal());
    }

    @Override
    public ArrayList<OrderDetail> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `OrderDetail`");

        ArrayList<OrderDetail> orderDetailDTO = new ArrayList<>();
        while (result.next()) {
            orderDetailDTO.add(new OrderDetail(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
        }
        return orderDetailDTO;
    }

    @Override
    public ArrayList<OrderDetail> searchId(String id, Connection connection) {
        return null;
    }

    @Override
    public boolean update(OrderDetail dto, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewID(Connection connection) {
        return null;
    }
}
