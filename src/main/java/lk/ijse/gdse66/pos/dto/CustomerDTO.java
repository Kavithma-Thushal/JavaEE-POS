package lk.ijse.gdse66.pos.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 8:38 PM - 1/15/2024
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private String id;
    private String name;
    private String address;
    private double salary;
}