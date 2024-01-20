package lk.ijse.gdse66.pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 3:13 PM - 1/16/2024
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String id;
    private String date;
    private String customerId;
}