package com.javalec.chat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;


@RestController
public class Controller {
	
		//keyboard
		@RequestMapping(value="/keyboard", method=RequestMethod.GET,produces="application/json; charset=UTF-8")
		public KeyboardInfo keyboard() {
			System.out.println("/keyboard");
			
			KeyboardInfo keyboard=new KeyboardInfo();
			keyboard.setType("text");
			return keyboard;		
		}
		
		//message
		@RequestMapping(value="/message", method=RequestMethod.POST,produces="application/json; charset=UTF-8")
		@ResponseBody
		//ResponseBody�� HTTP ���� ������ ǥ��
		public ResponseMessage message(@RequestBody RequestMessage rqm)throws JsonParseException, JsonMappingException, IOException {
			ResponseMessage rpm=new ResponseMessage();
			Message m=new Message();
			KeyboardInfo keyboard=new KeyboardInfo();
			keyboard.setType("text");
			rpm.setKeyboard(keyboard);
			
			if(!rqm.getType().equals("text"))
			{
				m.setText("�ؽ�Ʈ Ÿ�Ը� ����մϴ�.");
				rpm.setMessage(m);
				return rpm;
			}
			
			
			String subway=rqm.getContent();
			
			
			// Set up Assistant service.
	        Assistant service = new Assistant("2018-08-31");
	        service.setUsernameAndPassword(username, // replace with service username
	                                       pw); // replace with service password
	        String workspaceId = ""; // replace with workspace ID
	 
	        // Start assistant with empty message.
	        MessageOptions options = new MessageOptions.Builder(workspaceId).build();
	         
	 
	        InputData input = new InputData.Builder(subway).build();//������ �Է¹��� ������� ��û�� ����� �־��ش�.
	        //��, �ڿ���(������� ��û) --- �ӽ� -----> "��� ����" 
	        options = new MessageOptions.Builder(workspaceId).input(input).build();
	          
	        // Send message to Assistant service.
	        MessageResponse mes_response = service.message(options).execute();      
	        String response_m = mes_response.getOutput().getText().get(0); 
	        String s[]=response_m.split("\'");
	        String subnum="";
	        for(int i=0;i<s.length;i++) {
	        	subnum=subnum+s[i];
	        }
	        List<RuntimeIntent> responseIntents = mes_response.getIntents();
	 
	        // If an intent was detected, print it to the console.
	        if(responseIntents.size() > 0) {
	        System.out.println("Detected intent: #" + responseIntents.get(0).getIntent());
	        }
	 
	         
			//Rest�� ���� �����ϴ� ��� �ڿ��� ������ URL�� �ο��� Ȱ���ϴ� ��
			//Rest API ȣ��
			RestTemplate restTemplate = new RestTemplate(); 
			
			//header ����
			HttpHeaders headers = new HttpHeaders(); 
			headers.add("Content-type", "application/json; charset=UTF8");
			try{
				String encodeSubway=URLEncoder.encode(subnum,"UTF-8");
				//HttpEntity�� Http����� ���뿡 �����ϱ� ���ؼ� ���
				HttpEntity entity = new HttpEntity("parameters", headers); 
				URI url=URI.create("http://swopenapi.seoul.go.kr/api/subway/api/json/busLineToTransfer/0/15/"+encodeSubway); 
			
			 
				//HttpEntity�� Ŭ������ �����ϴµ� �� Ŭ������ ������ Http ���������� �̿��ϴ� ����� header�� body���� ������ ������ �� �ְ��Ѵ�.
				//RequestEntity�� ResponseEntity�� �̸� ��ӹ��� Ŭ����
				//ResponseEntity�� ��ü HTTP ������ ��Ÿ��
				//header ���� ������Ѿ� �� ��� @ResponseBody�� ��� �Ķ���ͷ� Response ��ü�� �޾Ƽ� �� ��ü���� header�� �����Ű��
				//ResponseEntity������ �� Ŭ���� ��ü�� ������ �� ��ü���� header���� �����Ű�� �ȴ�.
				//��, @ResponseBody�� ResponseEntity�� ��ɻ� ū �������� ����.
				//exchange�� �̿��Ͽ� ��ü�� json�����͸� ������, String Ÿ������ �޾ƿ��� JSON ��ü �������� �Ѿ��
				ResponseEntity response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			
				//json�� �Ľ�
				ObjectMapper obj=new ObjectMapper();
				BusList bl=new BusList();
			
				JsonNode node=obj.readValue(response.getBody().toString(), JsonNode.class);
				JsonNode BusList=node.get("busList");
				String t="";
				for(int i=0;i<15;i++) {
					t=t+BusList.get(i).get("ectrcNo")+"�� �ⱸ,  "+BusList.get(i).get("rtnm")+"��,    ";
					t=t+"�������� : "+BusList.get(i).get("allctintn")+"�� \n\r";
				}
				
				m.setText(t);
				rpm.setMessage(m);
				return rpm;
				
			}catch(NullPointerException exNull){
	            m.setText("������ �ùٸ��� �Է����ּ���");
	            rpm.setMessage(m);
	            return rpm;
	        }
			catch(Exception ex){
				m.setText("���� �����Դϴ�. 5�� �ڿ� �ٽ� �õ����ּ���.");
				rpm.setMessage(m);
				return rpm;
			}
		}
}