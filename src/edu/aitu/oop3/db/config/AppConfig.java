package edu.aitu.oop3.db.config;

public final class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final String url;
    private final String user;
    private final String password;

    private AppConfig() {
        // Берем из Environment Variables (переменные среды)
        this.url = System.getenv().getOrDefault("DB_URL",
                "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:5432/postgres?sslmode=require");
        this.user = System.getenv().getOrDefault("DB_USER", "postgres.eccfvjinhgldnywflozx");
        this.password = System.getenv().getOrDefault("DB_PASSWORD", "CHANGE_ME");
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
}
