package lk.ijse.gdse66.pos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:09 PM - 1/16/2024
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    private String id;
    private String name;
    private String address;
    private double salary;
}