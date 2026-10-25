package com.mzuriair.app.risk;
public class DemoRiskEngine implements RiskEngine {
    @Override public RiskLevel classify(int i) {
        if (i <= RiskConfiguration.SAFE_MAX) return RiskLevel.SAFE;
        if (i <= RiskConfiguration.MODERATE_MAX) return RiskLevel.MODERATE;
        return RiskLevel.CRITICAL;
    }
}
