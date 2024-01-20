package lk.ijse.gdse66.pos.dao.custom;

import lk.ijse.gdse66.pos.dao.CrudDAO;
import lk.ijse.gdse66.pos.entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:32 PM - 1/16/2024
 **/
public interface OrderDAO extends CrudDAO<Orders, String> {
    boolean mangeItems(int qty, String code, Connection connection) throws SQLException, ClassNotFoundException;
}