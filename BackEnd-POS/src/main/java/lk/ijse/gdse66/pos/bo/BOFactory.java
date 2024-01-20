package lk.ijse.gdse66.pos.bo;

import lk.ijse.gdse66.pos.bo.custom.impl.*;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 4:18 PM - 1/16/2024
 **/
public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case CUSTOM:
                return new QueryBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case ORDERS:
                return new OrderBOImpl();
            case ORDERDETAILS:
                return new OrderDetailsBOImpl();
            default:
                return null;
        }
    }

    public enum BOTypes {
        CUSTOMER, CUSTOM, ITEM, ORDERS, ORDERDETAILS
    }
}
