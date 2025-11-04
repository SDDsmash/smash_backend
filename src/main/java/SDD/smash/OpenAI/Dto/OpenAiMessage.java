package SDD.smash.OpenAI.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiMessage {

    // 메시지 역할 (user, assistant, system )
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

    public OpenAiMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
