@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JsonSchemaValidatorInterceptor())
                .addPathPatterns("/message-put") // Apply only to this URL
                .method(HttpMethod.PUT);       // Apply only to PUT requests
    }
}