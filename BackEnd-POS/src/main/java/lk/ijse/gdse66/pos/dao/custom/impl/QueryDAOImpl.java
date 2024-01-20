package lk.ijse.gdse66.pos.dao.custom.impl;

import lk.ijse.gdse66.pos.dao.custom.QueryDAO;
import lk.ijse.gdse66.pos.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:32 PM - 1/16/2024
 **/
public class QueryDAOImpl implements QueryDAO {
    @Override
    public int getCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(id) FROM Customer");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }

    @Override
    public int getItem(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(code) FROM Item");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }

    @Override
    public int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute(connection, "SELECT COUNT(orderId) FROM `Orders`");
        if (result.next()) {
            return result.getInt(1);
        } else {
            return 0;
        }
    }
}
