package net.garyhorn.pretest.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Capacity
{

	private String storeNo;
	private String productId;
	private Date date;
	private Double noOfOrdersAccepted;
	
}
