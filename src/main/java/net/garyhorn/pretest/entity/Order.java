package net.garyhorn.pretest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order
{
	private String orderId;
	private String productId;
	private Double qty;
}
