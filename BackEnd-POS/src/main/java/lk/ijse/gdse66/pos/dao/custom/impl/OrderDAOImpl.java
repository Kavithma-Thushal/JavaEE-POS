package lk.ijse.gdse66.pos.dao.custom.impl;

import lk.ijse.gdse66.pos.dao.custom.OrderDAO;
import lk.ijse.gdse66.pos.entity.Orders;
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
public class OrderDAOImpl implements OrderDAO {
    @Override
    public boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "UPDATE Item SET qty=qty-? WHERE code=?", qty, code);
    }

    @Override
    public boolean save(Orders orderDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(connection, "INSERT INTO orders VALUES(?,?,?)", orderDTO.getOrderId(), orderDTO.getOrderDate(), orderDTO.getCusId());
    }

    @Override
    public ArrayList<Orders> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT * FROM `Orders`");

        ArrayList<Orders> obList = new ArrayList<>();
        while (result.next()) {
            obList.add(new Orders(result.getString(1), result.getString(2), result.getString(3)));
        }
        return obList;
    }

    @Override
    public String generateNewID(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT orderId FROM `Orders` ORDER BY orderId DESC LIMIT 1");
        if (result.next()) {
            return result.getString(1);
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Orders> searchId(String id, Connection connection) {
        return null;
    }

    @Override
    public boolean update(Orders dto, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(String s, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }
}
