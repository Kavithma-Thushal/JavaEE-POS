package lk.ijse.gdse66.pos.bo.custom;

import lk.ijse.gdse66.pos.bo.SuperBO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public interface QueryBO extends SuperBO {
    int getCustomer(Connection connection) throws SQLException, ClassNotFoundException;

    int getItem(Connection connection) throws SQLException, ClassNotFoundException;

    int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException;
}