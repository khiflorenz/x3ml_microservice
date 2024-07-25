package net.artresearch.api.model;

import gr.forth.ics.isl.x3ml.X3MLEngineFactory;

public class X3MLSessionRequest {
  private String sessionId;
  private byte[] mappingsContent;
  private byte[] policyContent;
  private X3MLEngineFactory.OutputFormat outputFormat;

  // Getters and Setters
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public byte[] getMappingsContent() {
    return mappingsContent;
  }

  public void setMappingsContent(byte[] mappingsContent) {
    this.mappingsContent = mappingsContent;
  }

  public byte[] getPolicyContent() {
    return policyContent;
  }

  public void setPolicyContent(byte[] policyContent) {
    this.policyContent = policyContent;
  }

  public X3MLEngineFactory.OutputFormat getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(X3MLEngineFactory.OutputFormat outputFormat) {
    this.outputFormat = outputFormat;
  }
}
