package net.garyhorn.pretest.repository;

import static net.garyhorn.pretest.util.Constants.DATE_FORMATTER;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.garyhorn.pretest.entity.Availability;
import net.garyhorn.pretest.entity.Capacity;

/**
 * The repository
 * @author Gary M. Horn
 *
 */
@Service
public class MainRepository
{
	
	/**
	 * Get all the Availability records
	 * @return
	 */
	public List<Availability> getAllAvailabilities()
	{
		try
		{
			return Arrays.asList(new Availability("Store001", "Prod1", DATE_FORMATTER.parse("2021-10-29"), 1.0),
												 	 new Availability("Store001", "Prod2", DATE_FORMATTER.parse("2021-10-26"), 3.0),
													 new Availability("Store001", "Prod3", DATE_FORMATTER.parse("2021-10-25"), 2.0));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Get all the Capacity records
	 * @return
	 */
	public List<Capacity> getAllCapacities()
	{
		try
		{
			return Arrays.asList(new Capacity("Store001", "Prod1", DATE_FORMATTER.parse("2021-10-28"), 1.0), 
													 new Capacity("Store001", "Prod2", DATE_FORMATTER.parse("2021-10-30"), 3.0), 
													 new Capacity("Store001", "Prod3", DATE_FORMATTER.parse("2021-10-29"), 2.0));
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}
	
	/**
	 * Get the Availability record for the specified values
	 * 
	 * @param storeNo
	 * @param productId
	 * @return
	 */
	public Optional<Availability> getAvailability(String storeNo, String productId)
	{
		return getAllAvailabilities().stream()
																 .filter(a -> a.getStoreNo().equals(storeNo) && a.getProductId().equals(productId))
																 .findFirst();
	}
	
	/**
	 * Get the Capacity record for the specified values
	 * 
	 * @param storeNo
	 * @param productId
	 * @return
	 */
	public Optional<Capacity> getCapacity(String storeNo, String productId)
	{
		return getAllCapacities().stream()
														 .filter(c -> c.getStoreNo().equals(storeNo) && c.getProductId().equals(productId))
														 .findFirst();
	}
}
