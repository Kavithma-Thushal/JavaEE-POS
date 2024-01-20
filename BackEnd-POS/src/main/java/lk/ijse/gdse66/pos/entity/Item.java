package lk.ijse.gdse66.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:10 PM - 1/16/2024
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;
}