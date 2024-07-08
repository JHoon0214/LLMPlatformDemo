package cnsa.demo.config.LLM;

import lombok.Getter;

public class GPT3_5Config {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 1000;
    public static final Boolean STREAM = true;
    public static final Double TEMPERATURE = 0.7;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    public static final String RESPONSE_NODE_AT = "/choices/0/delta/content";
}
