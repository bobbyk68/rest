package uk.gov.hmrc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;

public class JsonSchemaValidatorInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {

            if (request.getMethod().equals("PUT") && request.getRequestURI().equals("/message-put")) {
                // Read the request body
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = request.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String requestBody = buffer.toString();

                // Load the JSON schema for the request
                JSONObject requestJsonSchema = new JSONObject(
                        new JSONTokener(getClass().getResourceAsStream("/request-schema.json")));
                Schema requestSchema = SchemaLoader.load(requestJsonSchema);

                try {
                    // Validate the request body against the schema
                    requestSchema.validate(new JSONObject(requestBody));
                } catch (ValidationException e) {
                    // Handle validation errors
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.getWriter().write(e.getMessage());
                    return false; // Stop further processing
                }
            }
            return true; // Continue processing
        }
    }