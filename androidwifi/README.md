# MzuriAIR Android Companion

Java Android Studio companion app for the MzuriAIR ESP32-S3 watch.

## Source-of-truth rule
The watch firmware remains authoritative for risk, AQI, dominant pollutant and live measurements. Android does **not** reproduce the firmware risk engine. It displays the `risk` object received from telemetry and preserves the original sample in Room.

## Verified firmware contract
The current firmware posts HTTP JSON to `MZ_DASHBOARD_URL`. BLE is only a future TODO. Telemetry contains device, timestamp, sequence, risk, nine environmental metrics, HR/wheeze/activity, battery/charging/RSSI and `simulated`.

SpO2 exists in the firmware sensor model but is not currently serialized into telemetry, so this app does not display SpO2 as if it were received.

Risk reasons are a bitmask: AQI=1, wheeze=2, heart=4, temperature=8, humidity=16, no-data=32. The published risk level includes firmware hysteresis, so Android must not recalculate it.

## Run
1. Open this folder in Android Studio.
2. Let Gradle sync.
3. Set the Retrofit base URL in `MainActivity.java` from `https://example.invalid/` to your HTTPS backend URL.
4. Ensure the backend implements `backend-contract/openapi.yaml`.
5. Run on an Android phone/emulator.

## Current implementation
- Java + AndroidX + Material 3
- Retrofit/OkHttp JSON API
- Room local telemetry cache
- Repository/data-source abstraction
- Dashboard, live readings, trends, insights, device and SOS foundations
- Demo-data badge based on firmware `simulated`
- Firmware telemetry fixture and parsing test
- Future BLE interface without fake BLE

## Demo/replay
The firmware already exposes simulated data. The Android architecture preserves `simulated=true`. A production showcase can add a replay controller that feeds the same JSON fixtures into the repository without modifying watch firmware.

## Safety
MzuriAIR is a prototype/research system. The app should use cautious language such as “estimated environmental risk”, “may contribute”, and “consider”. It must not claim to diagnose asthma attacks.
