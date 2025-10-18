package f5.health.app.auth.service.oauth2client;

import java.util.Map;

final class OAuth2Clients {

    private final Map<String, OAuth2Client> oauth2Clients;

    OAuth2Clients(Map<String, OAuth2Client> oauth2Clients) {
        this.oauth2Clients = oauth2Clients;
    }

    OAuth2Client get(String key) {
        return this.oauth2Clients.get(key);
    }
}
