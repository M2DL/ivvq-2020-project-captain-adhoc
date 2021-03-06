package captainadhoc.captainadhocbackend.configuration;


public class SecurityConstants {

    public static final String SECRET = System.getenv("SECRET_KEY") != null
            ? System.getenv("SECRET_KEY") : "my-secret";

    public static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final String SIGN_UP_URL = "/members";

}
