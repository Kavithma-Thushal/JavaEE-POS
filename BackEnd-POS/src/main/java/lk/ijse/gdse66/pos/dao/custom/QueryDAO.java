package lk.ijse.gdse66.pos.dao.custom;

import lk.ijse.gdse66.pos.dao.SuperDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:32 PM - 1/16/2024
 **/
public interface QueryDAO extends SuperDAO {
    int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException;

    int getItem(Connection connection) throws SQLException, ClassNotFoundException;

    int getCustomer(Connection connection) throws SQLException, ClassNotFoundException;
}