package com.mzuriair.app.risk;
/** Implemented by DemoRiskEngine now; an MLRiskEngine can implement it later. */
public interface RiskEngine { RiskLevel classify(int riskIndex); }
