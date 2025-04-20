/*
package f5.health.app.service.auth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f5.health.app.exception.auth.AppleAuthException;
import f5.health.app.exception.auth.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleIdTokenParser {

    private static final String TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;
    private final ObjectMapper objectMapper;

    */
/**
     * Identity Token에 담긴 암호화 알고리즘 ALG 및 키 식별자인 KID 헤더 추출
     *//*

    public Map<String, String> parseHeaders(String idToken) {
        try {
            String encodedHeader = idToken.split(TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader));
            return this.objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            throw new AppleAuthException(AuthErrorCode.INVALID_JWT, ex);
        }
    }
}
*/
