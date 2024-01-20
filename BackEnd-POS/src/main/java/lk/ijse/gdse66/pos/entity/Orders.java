package lk.ijse.gdse66.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:11 PM - 1/16/2024
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Orders {
    private String orderId;
    private String orderDate;
    private String cusId;
}