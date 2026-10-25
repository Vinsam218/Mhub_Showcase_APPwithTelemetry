package com.mzuriair.app.risk;
public enum RiskLevel {
    SAFE("SAFE"), MODERATE("MODERATE RISK"), CRITICAL("CRITICAL RISK");
    public final String label;
    RiskLevel(String l) { label = l; }
}
