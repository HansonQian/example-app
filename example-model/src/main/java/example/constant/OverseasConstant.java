package example.constant;

public final class OverseasConstant {
    /**
     * 分割符
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");


    // 消息主题名称
    public static final String REDIS_MAIL_TOPIC = "OVERSEAS__SERVICE_MAIL";

    public static final String MQ_MAIL_TOPIC = "OVERSEAS__SERVICE_MAIL";

    public static final String MQ_SMS_TOPIC = "MQ_SMS_TOPIC";

    public static final String MQ_MAIL_TOPIC_EXCHANGE = "MQ_MAIL_TOPIC_EXCHANGE";

    // cookie 名称
    public static final String COOKIE_NAME = "ouid";

    // REDIS auth token 前缀
    public static final String REDIS_AUTH_TOKEN_PREFIX = "user_token:";
}
