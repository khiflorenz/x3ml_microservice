package net.artresearch.api.controller;

import net.artresearch.api.model.X3MLSessionRequest;
import net.artresearch.api.service.X3MLService;
import net.artresearch.api.service.X3MLSession;
import gr.forth.ics.isl.x3ml.X3MLEngine;
import gr.forth.ics.isl.x3ml.X3MLEngineFactory; // Correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/x3ml")
public class X3MLController {

    @Autowired
    private X3MLService x3mlService;

    @PostMapping("/session")
    public ResponseEntity<X3MLSession> createSession(@RequestParam("sessionId") String sessionId,
                                                     @RequestParam("mappingsFile") MultipartFile mappingsFile,
                                                     @RequestParam("policyFile") MultipartFile policyFile,
                                                     @RequestParam("outputFormat") String outputFormat) {
        System.out.println("Entering createSession with ID: " + sessionId);
        try {
            X3MLSessionRequest request = new X3MLSessionRequest();
            request.setSessionId(sessionId);
            request.setMappingsContent(mappingsFile.getBytes());
            request.setPolicyContent(policyFile.getBytes());
            request.setOutputFormat(X3MLEngineFactory.OutputFormat.valueOf(outputFormat));
            X3MLSession session = x3mlService.createSession(request);
            return ResponseEntity.ok(session);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/process/{sessionId}")
    public ResponseEntity<String> processXML(@PathVariable String sessionId, @RequestBody String xmlInput) {
        try (InputStream inputStream = new ByteArrayInputStream(xmlInput.getBytes())) {
            X3MLSession session = x3mlService.getSession(sessionId);
            if (session == null) {
                return ResponseEntity.status(404).body("Session not found");
            }
            X3MLEngine.Output output = session.executeMapping(inputStream);
            return ResponseEntity.ok(output.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing XML");
        }
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> removeSession(@PathVariable String sessionId) {
        x3mlService.removeSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<X3MLSession> getSession(@PathVariable String sessionId) {
        X3MLSession session = x3mlService.getSession(sessionId);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

