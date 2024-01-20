package lk.ijse.gdse66.pos.dao;

import lk.ijse.gdse66.pos.dao.custom.impl.*;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:17 PM - 1/16/2024
 **/
public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case CUSTOM:
                return new QueryDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDERS:
                return new OrderDAOImpl();
            case ORDERDETAILS:
                return new OrderDetailsDAOImpl();
            default:
                return null;
        }
    }

    public enum DAOTypes {
        CUSTOMER, CUSTOM, ITEM, ORDERS, ORDERDETAILS
    }
}