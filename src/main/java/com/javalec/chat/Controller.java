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
		//ResponseBody는 HTTP 응답 본문의 표식
		public ResponseMessage message(@RequestBody RequestMessage rqm)throws JsonParseException, JsonMappingException, IOException {
			ResponseMessage rpm=new ResponseMessage();
			Message m=new Message();
			KeyboardInfo keyboard=new KeyboardInfo();
			keyboard.setType("text");
			rpm.setKeyboard(keyboard);
			
			if(!rqm.getType().equals("text"))
			{
				m.setText("텍스트 타입만 허용합니다.");
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
	         
	 
	        InputData input = new InputData.Builder(subway).build();//위에서 입력받은 사용자의 요청을 여기로 넣어준다.
	        //즉, 자연어(사용자의 요청) --- 왓슨 -----> "결과 도출" 
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
	 
	         
			//Rest란 웹에 존재하는 모든 자원에 고유한 URL을 부여해 활용하는 것
			//Rest API 호출
			RestTemplate restTemplate = new RestTemplate(); 
			
			//header 설정
			HttpHeaders headers = new HttpHeaders(); 
			headers.add("Content-type", "application/json; charset=UTF8");
			try{
				String encodeSubway=URLEncoder.encode(subnum,"UTF-8");
				//HttpEntity는 Http헤더와 내용에 접근하기 위해서 사용
				HttpEntity entity = new HttpEntity("parameters", headers); 
				URI url=URI.create("http://swopenapi.seoul.go.kr/api/subway/api/json/busLineToTransfer/0/15/"+encodeSubway); 
			
			 
				//HttpEntity란 클래스를 제공하는데 이 클래스의 역할은 Http 프로토콜을 이용하는 통신의 header와 body관련 정보를 저장할 수 있게한다.
				//RequestEntity와 ResponseEntity는 이를 상속받은 클래스
				//ResponseEntity는 전체 HTTP 응답을 나타냄
				//header 값을 변경시켜야 할 경우 @ResponseBody의 경우 파라미터로 Response 객체를 받아서 이 객체에서 header를 변경시키고
				//ResponseEntity에서는 이 클래스 객체를 생성한 뒤 객체에서 header값을 변경시키면 된다.
				//즉, @ResponseBody와 ResponseEntity는 기능상에 큰 차이점은 없다.
				//exchange를 이용하여 객체에 json데이터를 매핑함, String 타입으로 받아오면 JSON 객체 형식으로 넘어옴
				ResponseEntity response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			
				//json을 파싱
				ObjectMapper obj=new ObjectMapper();
				BusList bl=new BusList();
			
				JsonNode node=obj.readValue(response.getBody().toString(), JsonNode.class);
				JsonNode BusList=node.get("busList");
				String t="";
				for(int i=0;i<15;i++) {
					t=t+BusList.get(i).get("ectrcNo")+"번 출구,  "+BusList.get(i).get("rtnm")+"번,    ";
					t=t+"배차간격 : "+BusList.get(i).get("allctintn")+"분 \n\r";
				}
				
				m.setText(t);
				rpm.setMessage(m);
				return rpm;
				
			}catch(NullPointerException exNull){
	            m.setText("역명을 올바르게 입력해주세요");
	            rpm.setMessage(m);
	            return rpm;
	        }
			catch(Exception ex){
				m.setText("서버 오류입니다. 5초 뒤에 다시 시도해주세요.");
				rpm.setMessage(m);
				return rpm;
			}
		}
}