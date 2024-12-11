package com.example.serviceb.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServiceBRestController {

	private final DiscoveryClient discoveryClient;
	private final RestClient restClient;

	public ServiceBRestController(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
		this.discoveryClient = discoveryClient;
		restClient = restClientBuilder.build();
	}

	@GetMapping("helloEureka")
	public String helloWorld() {
		List<String> list = discoveryClient.getServices();
		for (String s : list) {
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
			System.out.println("Service: " + s);
		}
		ServiceInstance serviceInstance = discoveryClient.getInstances("servicea").get(0);
		String serviceAResponse = restClient.get()
				.uri(serviceInstance.getUri() + "/helloWorld")
				.retrieve()
				.body(String.class);
		return serviceAResponse;
	}
	@GetMapping("services")
	public List<ServiceInstance> services() {
		List<String> list = discoveryClient.getServices();
		List<ServiceInstance> serviceInstanceAll = new ArrayList<>();
		for (String s : list) {
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(s);
			serviceInstanceAll.addAll(serviceInstances);
		}
//		ServiceInstance serviceInstance = discoveryClient.getInstances("servicea").get(0);
//		String serviceAResponse = restClient.get()
//				.uri(serviceInstance.getUri() + "/helloWorld")
//				.retrieve()
//				.body(String.class);
		return serviceInstanceAll;
	}
}
