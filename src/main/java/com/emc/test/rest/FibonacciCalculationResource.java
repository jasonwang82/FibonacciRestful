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
import org.springframework.web.bind.annotation.*;

import com.emc.test.rest.dto.BatchFibonacciRequest;
import com.emc.test.rest.dto.BatchFibonacciResponse;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    /**
     * POST /rest/fibonacci/batch -> Calculate multiple Fibonacci numbers in batch
     */
    @RequestMapping(value = "/rest/fibonacci/batch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BatchFibonacciResponse> calculateBatch(@RequestBody BatchFibonacciRequest request) {
        Map<Integer, String> results = new HashMap<>();
        Map<Integer, String> errors = new HashMap<>();

        request.getNumbers().parallelStream().forEach(number -> {
            try {
                results.put(number, calculator.calculate(number).toString());
            } catch (Exception e) {
                errors.put(number, e.getMessage());
            }
        });

        return new ResponseEntity<>(new BatchFibonacciResponse(results, errors), HttpStatus.OK);
    }

    /**
     * GET /rest/fibonacci/async/{id} -> Calculate Fibonacci number asynchronously
     */
    @RequestMapping(value = "/rest/fibonacci/async/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CompletableFuture<ResponseEntity<String>> getAsync(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            if (!validation(id)) {
                return new ResponseEntity<>("Invalid number - " + id, HttpStatus.BAD_REQUEST);
            }

            try {
                BigInteger result = calculator.calculate(Integer.parseInt(id));
                return new ResponseEntity<>(result.toString(), HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error calculating Fibonacci number", e);
                return new ResponseEntity<>("Error calculating Fibonacci number", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }
}
