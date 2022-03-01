package net.garyhorn.pretest.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Shipment
{
	private String orderId;
	private String shipmentId;
	private String productId;
	private Date shipmentDate;
	private Double qty;

}
