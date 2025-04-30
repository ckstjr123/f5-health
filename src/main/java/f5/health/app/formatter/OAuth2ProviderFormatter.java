package f5.health.app.formatter;

import f5.health.app.constant.OAuth2Provider;
import org.springframework.format.Formatter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.ParseException;
import java.util.Locale;

/**
 * {provider} 경로 변수를 OAuthProvider으로 parse
 */
public class OAuth2ProviderFormatter implements Formatter<OAuth2Provider> {

    @Override
    public OAuth2Provider parse(String providerPathVariable, Locale locale) throws ParseException {
        return OAuth2Provider.valueOf(providerPathVariable.toUpperCase(locale)); //ex) kakao → KAKAO
    }

    @Override
    public String print(OAuth2Provider provider, Locale locale) {
        return provider.name();
    }
}
