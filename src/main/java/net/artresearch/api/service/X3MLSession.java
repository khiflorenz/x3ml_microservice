package net.artresearch.api.service;

import gr.forth.ics.isl.x3ml.X3MLEngine;
import gr.forth.ics.isl.x3ml.X3MLEngineFactory;
import gr.forth.ics.isl.x3ml.X3MLGeneratorPolicy;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class X3MLSession {
  private final String sessionId;
  private final X3MLEngineFactory.OutputFormat outputFormat;
  private final AtomicReference<X3MLEngine> engine = new AtomicReference<>();
  private final AtomicReference<X3MLGeneratorPolicy> policy = new AtomicReference<>();
  private final byte[] mappingsContent;
  private final byte[] policyContent;

  public X3MLSession(String sessionId, byte[] mappingsContent, byte[] policyContent,
      X3MLEngineFactory.OutputFormat outputFormat) throws IOException {
    this.sessionId = sessionId;
    this.mappingsContent = mappingsContent;
    this.policyContent = policyContent;
    this.outputFormat = outputFormat;
    initializeEngineAndPolicy();
  }

  private void initializeEngineAndPolicy() throws IOException {
    try (InputStream mappingsStream = new ByteArrayInputStream(mappingsContent);
        InputStream policyStream = new ByteArrayInputStream(policyContent)) {
      this.engine.set(X3MLEngine.load(mappingsStream));
      this.policy.set(X3MLGeneratorPolicy.load(policyStream, X3MLGeneratorPolicy.createUUIDSource(-1)));
    } catch (Exception e) {
      throw new IOException("Failed to load mappings or policy", e);
    }
  }

  private Document loadXMLDocument(InputStream inputStream) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(inputStream);
  }

  public X3MLEngine.Output executeMapping(InputStream input) throws IOException {
    try {
      X3MLEngine engineInstance = engine.get();
      X3MLGeneratorPolicy policyInstance = policy.get();
      Document inputDoc = loadXMLDocument(input);
      Element inputElement = inputDoc.getDocumentElement();
      return engineInstance.execute(inputElement, policyInstance);
    } catch (Exception e) {
      throw new IOException("Failed to execute mapping", e);
    }
  }

  public void cleanUp() {
    engine.set(null);
    policy.set(null);
  }

  public String getSessionId() {
    return sessionId;
  }
}
