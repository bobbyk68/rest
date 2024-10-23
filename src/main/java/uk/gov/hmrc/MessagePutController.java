package uk.gov.hmrc;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagePutController {

        @PutMapping("/message-put")
        public ResponseEntity<MessagePutResponse> messagePut(@RequestBody MessagePutRequest request) {
            // Process the validated request
            // ... your logic to handle the request ...

            // Create a response object
            MessagePutResponse response = new MessagePutResponse("Message processed successfully");

            // Load the JSON schema for the response
            JSONObject responseJsonSchema = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/response-schema.json")));
            Schema responseSchema = SchemaLoader.load(responseJsonSchema);

            try {
                // Validate the response against the schema
                responseSchema.validate(new JSONObject(response));
            } catch (ValidationException e) {
                // Handle validation errors (e.g., log the error)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            return ResponseEntity.ok(response);
        }


    // Request class representing the JSON message
    public static class MessagePutRequest {
        // ... your request fields with getters and setters ...
    }

    // Response class representing the JSON response
    public static class MessagePutResponse {
        private String message;

        public MessagePutResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }




}
