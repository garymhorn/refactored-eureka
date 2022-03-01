
package net.garyhorn.pretest.controllers;

import static net.garyhorn.pretest.util.Constants.DATE_FORMATTER;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.garyhorn.pretest.entity.Availability;
import net.garyhorn.pretest.entity.Capacity;
import net.garyhorn.pretest.repository.MainRepository;

/**
 * The Main Controller
 * 
 * @author Gary M. Horn
 *
 */
@RestController
public class MainController
{
	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private MainRepository repo;

	/**
	 * Get the Dates By Service
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/getDatesByService")
	public ResponseEntity<String> getDatesByServices(@RequestBody String json)
	{
		JsonNode output = null;

		try
		{
			// Parse the incoming request as JSON
			ObjectMapper om = new ObjectMapper();
			JsonNode input = om.readTree(json);
			output = input;

			String storeNo = input.get("storeNo").textValue();
			String prodId = input.get("productId").textValue();

			// Setup the Callables for retrieving the data
			Callable<Optional<Availability>> callAvail = () -> {
				return repo.getAvailability(storeNo, prodId);
			};

			Callable<Optional<Capacity>> callCap = () -> {
				return repo.getCapacity(storeNo, prodId);
			};

			// Call the ExecutorService to get the results
			ExecutorService executor = Executors.newFixedThreadPool(2);

			Future<Optional<Availability>> availResult = executor.submit(callAvail);
			Future<Optional<Capacity>> capResult = executor.submit(callCap);

			Optional<Availability> avail = availResult.get(200, TimeUnit.MILLISECONDS);
			Optional<Capacity> cap = capResult.get(200, TimeUnit.MILLISECONDS);

			// Shutdown the ExecutorService
			executor.shutdown();
			try
			{
				if(!executor.awaitTermination(800, TimeUnit.MILLISECONDS))
				{
					executor.shutdownNow();
				}
			}
			catch(InterruptedException e)
			{
				executor.shutdownNow();
			}

			// Continue processing the results
			if(!avail.isPresent() || !cap.isPresent())
			{
				LOG.info("No records found");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No records found!");
			}

			Date edd = (avail.get().getDate().after(cap.get().getDate())) ? avail.get().getDate() : cap.get().getDate();
			((ObjectNode)output).put("EDD", DATE_FORMATTER.format(edd));

		}
		catch(Exception e)
		{
			e.printStackTrace();
			LOG.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message");
		}

		// Return the message.
		return ResponseEntity.status(HttpStatus.OK).body(output.toPrettyString());
	}
}
