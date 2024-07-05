package cnsa.demo.service;

import cnsa.demo.entity.Message;
import cnsa.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.*;

@Service
public class DemoService {

    private final MessageRepository messageRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public DemoService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(String text, boolean isUserInput) {
        Message message = new Message();
        message.setText(text);
        message.setUserInput(isUserInput);
        messageRepository.save(message);
    }

    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    private List<Map<String, String>> parseConversationToGPTInput(List<Message> conversation) {
        List<Map<String, String>> ret = new ArrayList<>();

        Map<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", "you are chat gpt. You have to look at the user's question and give an appropriate answer. If necessary, you can refer to our previous conversation. Content with a role of user is input made by the user, and content with a role of assistant is a response made by gpt in the past.");

        ret.add(system);

        for(Message curr: conversation) {
            Map<String, String> map = new HashMap<>();
            if(curr.isUserInput()) map.put("role", "user");
            else map.put("role", "assistant");

            map.put("content", curr.getText());

            ret.add(map);
        }
        return ret;
    }

    public String getGptResponse(List<Message> conversation) {
        String url = "https://api.openai.com/v1/chat/completions";
        RestTemplate restTemplate = new RestTemplate();

        for(Message message:conversation) {
            System.out.println(message.getText() + "\n");
        }

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo");
        request.put("messages", parseConversationToGPTInput(conversation));
        request.put("max_tokens", 4096);
        request.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);
        System.out.println(responseEntity);
        List<Map<String, Object>> list = ((List) responseEntity.getBody().get("choices"));
        String ret = ((Map<String, String>)list.get(0).get("message")).get("content");
        System.out.println(ret);

        return ret;
    }
}
