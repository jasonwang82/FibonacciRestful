/**
 * This class contains test cases for the FibonacciRestServiceApplication.
 * It uses the MockMvc framework to test the FibonacciCalculationResource class.
 * The test cases verify the Fibonacci calculation for various inputs.
 * 
 * Author: Jason.Wang
 */
package com.emc.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.emc.test.rest.FibonacciCalculationResource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FibonacciRestServiceApplication.class)
@WebAppConfiguration
public class FibonacciRestServiceApplicationTests {

	private MockMvc restFibonacciMvc;

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FibonacciCalculationResource cal = new FibonacciCalculationResource();
		this.restFibonacciMvc = MockMvcBuilders.standaloneSetup(cal).build();
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 7.
	 * It expects the output to be "0 1 1 2 3 5 8 ".
	 */
	@Test
	public void verifyFibonacci() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/7"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 1 1 2 3 5 8 ".getBytes()));
		
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/-1"))
		.andExpect(status().is4xxClientError())
		.andExpect(content().bytes("Invalid number - -1".getBytes()));
		
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/a"))
		.andExpect(status().is4xxClientError())
		.andExpect(content().bytes("Invalid number - a".getBytes()));
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 0.
	 * It expects the output to be "0 ".
	 */
	@Test
	public void verifyFibonacciForInput0() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/0"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 ".getBytes()));
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 1.
	 * It expects the output to be "0 1 ".
	 */
	@Test
	public void verifyFibonacciForInput1() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/1"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 1 ".getBytes()));
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 2.
	 * It expects the output to be "0 1 1 ".
	 */
	@Test
	public void verifyFibonacciForInput2() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/2"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 1 1 ".getBytes()));
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 10.
	 * It expects the output to be "0 1 1 2 3 5 8 13 21 34 ".
	 */
	@Test
	public void verifyFibonacciForInput10() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/10"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 1 1 2 3 5 8 13 21 34 ".getBytes()));
	}

	/**
	 * Test case to verify the Fibonacci calculation for input 20.
	 * It expects the output to be "0 1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 ".
	 */
	@Test
	public void verifyFibonacciForInput20() throws Exception {
		restFibonacciMvc.perform(get("/v1/rest/fibonacci/20"))
				.andExpect(status().isOk())
				.andExpect(content().bytes("0 1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584 4181 ".getBytes()));
	}
}
