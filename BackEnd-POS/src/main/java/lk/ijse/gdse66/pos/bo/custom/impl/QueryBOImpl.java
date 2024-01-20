package lk.ijse.gdse66.pos.bo.custom.impl;

import lk.ijse.gdse66.pos.bo.custom.QueryBO;
import lk.ijse.gdse66.pos.dao.DAOFactory;
import lk.ijse.gdse66.pos.dao.custom.QueryDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:30 PM - 1/16/2024
 **/
public class QueryBOImpl implements QueryBO {
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOM);

    @Override
    public int getCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getCustomer(connection);
    }

    @Override
    public int getItem(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getItem(connection);
    }

    @Override
    public int getSumOrders(Connection connection) throws SQLException, ClassNotFoundException {
        return queryDAO.getSumOrders(connection);
    }
}