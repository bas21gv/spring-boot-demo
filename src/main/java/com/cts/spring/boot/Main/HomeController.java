package com.cts.spring.boot.Main;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class HomeController {
	
	public List<String> idList = new ArrayList<>();
	public final static String AUTH_KEY_FCM = "AAAArjHd0i0:APA91bEPAF02plVTrULYG7vLRpAAtdvbL7aMxMlPBfpqivfSKhqlIIfUmPO5gGxEKskLp3_XRiYF12nJWLYKDLpcPzOzKKYt_hPFWC2-k8-udJHqxOOqYlEKcKVMCj4klww9_hDQaRdhIpi67SyDtszmLSY5xzRT5Q";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	@Autowired
	PersonRepo personRepo;
	
	@RequestMapping(value="/persons",method=RequestMethod.GET)
	public List<Person> getPersons(){
		List<Person> personList = new ArrayList<>();
		personList.add(new Person("Bala", 27));
		personList.add(new Person("Guna", 29));
		personList.add(new Person("Subu", 25));
		System.out.println("called get person api .... ");
		return personList;
	}
	
	@RequestMapping(value="/notify", method=RequestMethod.POST)
	public ResponseEntity<?> pushNotification(){
		ResponseEntity<Object> response = null;

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "key="+AUTH_KEY_FCM);
		
		JSONObject info = new JSONObject();
		info.put("title", "Notification Title");   // Notification title
		info.put("body", "HI, Happy New Year 2017"); // Notification body
		
		JSONObject requestJson = new JSONObject();
		requestJson.put("registration_ids", idList); /** to - for single sender*/
		requestJson.put("notification", info);

		
		HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);
		
		System.out.println(entity.getBody());
		//ResponseEntity<Object> response = restTemplate.postForEntity(new URI(API_URL_FCM), request, Object.class);
		try {
			response = restTemplate.exchange(new URI(API_URL_FCM), HttpMethod.POST, entity, Object.class);
		} catch (RestClientException | URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println(response.getBody());
		return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/saveid",method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDeviceId(@RequestBody String userDeviceId){
		System.out.println(userDeviceId);
		if(userDeviceId != null){
			JSONParser parser = new JSONParser();
			JSONObject requestPayload = null;
			try {
				requestPayload = (JSONObject) parser.parse(userDeviceId);
				idList.add(requestPayload.get("deviceId").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>("Device Id Stored",HttpStatus.OK);
	}
}
