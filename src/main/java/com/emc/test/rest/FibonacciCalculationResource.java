/**
 * @author Jason.Wang
 */
package com.emc.test.rest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.emc.test.common.utils.ConfigurationUtils;
import com.emc.test.common.utils.NioFileSystemUtils;
import com.emc.test.fibonacci.FibonacciPartThread;
import com.emc.test.process.ProcessRunner;

/**
 * REST controller for managing fibonacci calculation.
 */
@RestController
@RequestMapping("/v1")
public class FibonacciCalculationResource {

	private final Logger log = LoggerFactory
			.getLogger(FibonacciCalculationResource.class);
	
	private final OptimizedFibonacciCalculator calculator = new OptimizedFibonacciCalculator();
	
	/**
	 * GET /rest/fibonacci/:id -> get the "id" calucalation number.
	 */
	@RequestMapping(value = "/rest/fibonacci/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> get(@PathVariable String id,
			HttpServletResponse response) {
		log.debug("REST request to calculate fibonacci : {}", id);
		if (!validation(id)) {
			return new ResponseEntity<>("Invalid number - " + id,
					HttpStatus.BAD_REQUEST);
		}
		
		try {
			BigInteger result = calculator.calculate(Integer.parseInt(id));
			return new ResponseEntity<>(result.toString(), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error calculating Fibonacci number", e);
			return new ResponseEntity<>("Error calculating Fibonacci number",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validation(String id) {
		try {
			int i = Integer.parseInt(id);
			if (i < 0) {
				log.error(String.format("%s should be positive number. ", id));
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			log.error(String.format("%s is not a valid number. ", id), e);
			return false;
		}
	}

}
