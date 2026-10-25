package com.mzuriair.app.risk;
/** Prototype DEMONSTRATION thresholds only - NOT clinical asthma thresholds.
 *  Replace with validated research/ML model thresholds before clinical use. */
public final class RiskConfiguration {
    public static final int SAFE_MAX = 30;      // 0-30
    public static final int MODERATE_MAX = 60;  // 31-60, 61-100 = CRITICAL
    private RiskConfiguration() {}
}
