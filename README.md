# Mhub_Showcase_APPwithTelemetry
# MzuriAIR Companion

Java Android Studio companion application and FastAPI backend for the **MzuriAIR ESP32-S3 smartwatch prototype**.

MzuriAIR is an AIoT asthma-risk monitoring concept that combines environmental and physiological measurements into personalized, estimated environmental-risk information. This repository contains the software used to demonstrate synchronization between a simulated smartwatch data source, a backend API, and an Android companion application.

> **Important:** MzuriAIR is a prototype/research system. It is not a medical diagnostic device and must not claim to diagnose asthma attacks or provide emergency medical instructions. Use cautious language such as **“estimated environmental risk”**, **“may contribute”**, and **“consider”**.

---

## Project goal

The long-term goal is to keep the MzuriAIR smartwatch and Android companion app synchronized.

### Current prototype architecture

```text
Simulated MzuriAIR Watch Telemetry
                |
                | HTTP JSON
                v
      MzuriAIR Companion API
             FastAPI
                |
                | GET latest telemetry
                v
       Android Companion App
       Java + Retrofit/OkHttp
                |
                v
        Room local cache
```

### Intended future architecture

```text
ESP32-S3 MzuriAIR Watch
        |
        | Wi-Fi and/or BLE
        v
   MzuriAIR Backend
        |
        v
 Android Companion App
```

The physical watch is **not yet the live network source**. Its sensor values are currently simulated by the firmware/prototype. Wi-Fi has not yet been configured on the ESP32-S3, and BLE is intentionally a future abstraction rather than a fake connection.

---

# Repository structure

The repository is intended to keep the Android Studio project and the backend in one place:

```text
MzuriAIR-Companion/
|
+-- android/                         # Android Studio project
|   +-- app/
|   |   +-- src/main/java/
|   |   +-- src/main/res/
|   |   +-- src/main/AndroidManifest.xml
|   |   +-- ...
|   +-- build.gradle / settings.gradle
|   +-- ...
|
+-- backend/                         # FastAPI backend (VS Code project)
|   +-- main.py / app.py             # actual entry point may differ
|   +-- ...
|   +-- backend-contract/
|       +-- openapi.yaml
|
+-- firmware/                        # Optional: ESP32-S3 firmware or fixtures
|   +-- ...
|
+-- docs/                            # Optional project documentation
|   +-- ...
|
+-- README.md
+-- .gitignore
```

> The exact filenames may differ from the current local projects. Preserve the existing Android and backend structure when importing the projects; do not duplicate networking layers unless necessary.

---

# Source-of-truth rule

The **watch firmware remains authoritative** for the following values:

- Risk level
- AQI
- Dominant pollutant
- Risk reasons
- Live measurements produced by the firmware

Android **must not reproduce or recalculate the firmware risk engine**.

The Android application should:

1. receive the telemetry JSON;
2. display the `risk` object supplied by the watch/backend;
3. preserve the original sample in Room;
4. show whether the sample was simulated;
5. use timestamps and sequence numbers to preserve sample ordering.

This avoids having two different implementations of the MzuriAIR risk engine.

---

# Current status

## Backend — working

The **MzuriAIR Companion API** is currently running locally with FastAPI/Uvicorn.

Local development URL:

```text
http://127.0.0.1:8000
```

Swagger/OpenAPI documentation:

```text
http://127.0.0.1:8000/docs
```

Health endpoint:

```text
GET /health
```

## Verified API endpoints

### Receive telemetry

```http
POST /api/devices/{device_id}/telemetry
```

### Get latest telemetry

```http
GET /api/devices/{device_id}/latest
```

### Verified device ID

```text
mzuriair-001
```

### Verified telemetry flow

The following sequence has already been tested successfully through Swagger:

```text
POST simulated telemetry
        |
        v
backend accepts and stores telemetry
        |
        v
GET latest telemetry
        |
        v
stored reading is returned
```

The earlier error:

```text
No telemetry available for this device
```

was caused by there being no successfully stored telemetry record yet. The POST request is now working, so this backend issue is considered solved.

---

# Verified telemetry payload

The following simulated payload has successfully passed backend validation:

```json
{
  "device": "mzuriair-001",
  "ts": 1791443100,
  "seq": 1,
  "risk": {
    "level": "Mild Risk",
    "aqi": 42,
    "dominant": "VOC",
    "reasons": 1
  },
  "vitals": {
    "heart_rate": 78,
    "spo2": 97,
    "temperature": 24.6,
    "humidity": 61.2
  },
  "device_status": {
    "battery": 85,
    "charging": false,
    "rssi": -58
  }
}
```

> **Important firmware contract note:** SpO2 exists in the firmware sensor model but is **not currently serialized into the firmware telemetry payload**. Android must not display SpO2 as a received watch measurement unless the telemetry contract is updated to include it.

The example above was used to validate backend parsing; the Android application should follow the **actual published telemetry contract** rather than assuming every internal firmware field is transmitted.

---

# Telemetry model

The backend currently validates telemetry using a structure similar to:

```text
Telemetry
|
+-- device
+-- ts
+-- seq
+-- risk
|   +-- level
|   +-- aqi
|   +-- dominant
|   +-- reasons
|
+-- vitals
|   +-- firmware-defined vitals/environment fields
|
+-- device_status
    +-- battery
    +-- charging
    +-- rssi
```

The exact schema is defined by the backend/OpenAPI contract and the firmware implementation. Do not silently change the Android model to a different JSON shape without updating the contract.

---

# Risk reasons bitmask

Risk reasons are represented as a bitmask. The Android app should interpret the received reasons for explanation/display only; it must **not recalculate the overall risk level**.

| Bit | Value | Meaning |
|---|---:|---|
| AQI | `1` | Air-quality contribution |
| Wheeze | `2` | Wheeze contribution |
| Heart | `4` | Heart-rate contribution |
| Temperature | `8` | Temperature contribution |
| Humidity | `16` | Humidity contribution |
| No data | `32` | Insufficient data |

The firmware risk level includes **hysteresis**, so Android must never independently decide that the current state is SAFE, MODERATE, CRITICAL, etc. based on raw values.

---

# Android application

The Android companion is written in **Java** and built with Android Studio.

Current architecture/components include:

- AndroidX
- Material 3
- Retrofit
- OkHttp
- Gson JSON conversion
- Room local telemetry cache
- Repository/data-source abstraction
- ViewModel architecture
- Dashboard
- Live readings
- Trends foundation
- Insights foundation
- Device page
- SOS foundation
- Demo-data badge based on `simulated`
- Firmware telemetry fixture/parsing test
- Future BLE interface abstraction

The Android project is designed so the backend/networking layer can be replaced or extended without rebuilding the UI from scratch.

---

# Current Android data flow

The current `MainActivity` creates the existing networking stack rather than creating a second API client:

```text
Retrofit
   |
   v
MzuriApi
   |
   v
TelemetryApiDataSource
   |
   v
TelemetryRepository
   |
   v
MainViewModel
   |
   v
Android UI
```

The current device ID passed to the data source is:

```text
mzuriair-001
```

The app is intended to request:

```http
GET /api/devices/mzuriair-001/latest
```

and render the returned telemetry.

---

# Current integration problem

The **backend works** and the simulated telemetry flow works through Swagger.

The remaining problem is:

> **The Android application deployed on a physical phone cannot currently connect to the local FastAPI backend.**

This is a connectivity/development-environment problem, not a missing telemetry problem.

The backend currently runs on the developer's computer at:

```text
http://127.0.0.1:8000
```

A physical Android phone cannot normally reach the developer computer's `127.0.0.1` directly because `127.0.0.1` from the phone refers to the phone itself.

Likewise, Android emulator addressing and physical-phone addressing are different.

---

# Physical phone development options

## Option A — USB + ADB reverse (recommended for local development)

When the phone is connected by USB and ADB is available, create a reverse port mapping:

```bash
adb devices
adb reverse tcp:8000 tcp:8000
```

Then the Android app can use:

```text
http://127.0.0.1:8000/
```

because ADB forwards the phone's port 8000 to the computer's port 8000.

Expected flow:

```text
Android phone
     |
     | USB / ADB reverse
     v
Computer :8000
     |
     v
FastAPI
```

This method avoids requiring the phone and computer to communicate over Wi-Fi and is useful for local development.

### Basic checks

```bash
adb devices
```

Confirm the phone appears as `device`.

Then:

```bash
adb reverse tcp:8000 tcp:8000
```

If the command fails, fix the ADB/device connection before troubleshooting Retrofit.

---

## Option B — Phone + computer on the same Wi-Fi network

For a direct LAN connection, the FastAPI server must listen on a network interface rather than only on localhost.

Start Uvicorn using a host such as:

```bash
uvicorn <your_module>:<your_app> --host 0.0.0.0 --port 8000 --reload
```

Then determine the computer's LAN address, for example:

```bash
ip addr
```

A typical address might look like:

```text
192.168.1.105
```

The phone can then be tested with:

```text
http://192.168.1.105:8000/docs
```

Use the actual LAN IP assigned to the development computer.

The phone and computer must be reachable on the same network, and the computer's firewall must permit TCP port 8000.

### Important diagnostic rule

Before debugging Android, first open the backend Swagger URL in the **phone's browser**:

```text
http://<computer-lan-ip>:8000/docs
```

If the phone browser cannot open the page, the Android app will not be able to open the API either. Fix the network path first.

---

# Emulator vs physical phone

Do not use the same base URL for every environment.

| Environment | Typical development base URL |
|---|---|
| Backend on same Android emulator process | Not applicable here |
| Android Studio emulator → host computer | `http://10.0.2.2:8000/` |
| Physical phone + ADB reverse | `http://127.0.0.1:8000/` |
| Physical phone + LAN/Wi-Fi | `http://<computer-lan-ip>:8000/` |
| Production | HTTPS public backend URL |

The Android code should use the URL appropriate to the environment being tested.

---

# Android HTTP/cleartext development

The development backend currently uses HTTP rather than HTTPS.

Depending on the Android target/configuration, cleartext HTTP may need to be explicitly allowed for local development, for example through the application configuration in `AndroidManifest.xml`.

A common development setting is:

```xml
<application
    android:usesCleartextTraffic="true"
    ... >
```

Do not treat this as a production security configuration. A production deployment should use HTTPS and an appropriate network-security policy.

---

# Current MainActivity networking configuration

The current Android project contains a Retrofit configuration similar to:

```java
Retrofit rt = new Retrofit.Builder()
        .baseUrl("https://example.invalid/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```

For local development this placeholder URL must be replaced by the correct environment-specific backend URL.

Example for a physical phone using ADB reverse:

```java
Retrofit rt = new Retrofit.Builder()
        .baseUrl("http://127.0.0.1:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```

Example for a physical phone using LAN/Wi-Fi:

```java
Retrofit rt = new Retrofit.Builder()
        .baseUrl("http://192.168.1.105:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```

Replace the sample LAN IP with the actual development computer IP.

---

# Backend test procedure

Run the backend and open:

```text
http://127.0.0.1:8000/docs
```

Then use:

```http
POST /api/devices/mzuriair-001/telemetry
```

with a valid telemetry JSON payload.

Then test:

```http
GET /api/devices/mzuriair-001/latest
```

A successful response confirms the backend and telemetry storage are working independently of Android.

---

# Android test procedure

Once the backend is proven healthy:

1. Start the FastAPI server.
2. Make sure `mzuriair-001` has at least one stored telemetry sample.
3. Select the correct Android base URL for the testing environment.
4. Ensure Android HTTP configuration allows local development traffic when needed.
5. Deploy the app to the phone/emulator.
6. Watch Logcat for the exact Retrofit/OkHttp error if the request fails.
7. Confirm that the phone can reach the API independently before changing the repository/view-model architecture.

The intended request is:

```text
GET /api/devices/mzuriair-001/latest
```

The app should then deserialize the response into its existing `Telemetry` model and expose it through the repository and `MainViewModel`.

---

# Troubleshooting guide

## Error: `No telemetry available for this device`

Meaning:

The GET endpoint worked, but the backend has no stored telemetry for the requested device ID.

Check:

```text
Device ID = mzuriair-001
```

Then POST a valid telemetry sample before calling GET.

---

## Error: HTTP 422 from POST telemetry

Meaning:

The request reached FastAPI, but the JSON does not match the Pydantic schema.

Common causes:

- missing `ts`
- missing `seq`
- `risk` sent as a string instead of an object
- missing `risk.level`
- missing `risk.aqi`
- missing `risk.dominant`
- missing `risk.reasons`
- missing `vitals`
- missing `device_status.battery`
- missing `device_status.charging`
- missing `device_status.rssi`
- invalid JSON syntax

The FastAPI 422 response normally identifies the exact missing/invalid field.

---

## Error: Android says connection refused / failed to connect

Check in this order:

```text
1. Is FastAPI running?
2. Is the Android base URL correct for the device being used?
3. Is the phone on the same LAN as the computer if using Wi-Fi?
4. Is the server bound to 0.0.0.0 if using LAN?
5. Is port 8000 allowed through the computer firewall?
6. Can the phone browser open /docs?
7. If using USB, does adb devices show the phone?
8. If using USB, does adb reverse tcp:8000 tcp:8000 succeed?
9. Is Android cleartext HTTP permitted for local development?
10. What exact error appears in Logcat?
```

Do not change multiple layers simultaneously. Prove the network connection first, then debug Retrofit/model parsing.

---

## Error: `127.0.0.1` works on computer but not phone

Expected behavior.

`127.0.0.1` refers to the current device.

- On the development computer: it means the computer.
- On the phone: it means the phone.

Use ADB reverse for USB development, or use the computer's LAN IP for direct Wi-Fi/LAN development.

---

## Error: `10.0.2.2` does not work on a real phone

Expected behavior.

`10.0.2.2` is an Android emulator host-access address and should not be assumed to work for a physical phone.

Use either:

```text
ADB reverse + 127.0.0.1
```

or:

```text
LAN IP + server bound to 0.0.0.0
```

---

# Firmware contract

The firmware currently posts HTTP JSON to `MZ_DASHBOARD_URL`.

Telemetry includes:

- device identifier
- timestamp
- sequence number
- risk object
- environmental metrics
- heart-rate information
- wheeze information
- activity information
- battery
- charging state
- RSSI
- `simulated`

BLE is a **future TODO**.

The firmware already exposes simulated data. The Android architecture should preserve:

```json
"simulated": true
```

rather than hiding the fact that the data source is simulated.

---

# Android data integrity rules

Android should preserve the original sample rather than generating a new interpretation of it.

In particular:

```text
Firmware risk      -> authoritative
Firmware AQI       -> authoritative
Firmware dominant  -> authoritative
Firmware reasons   -> authoritative
Raw measurements   -> display/cache
Timestamp          -> preserve
Sequence           -> preserve
Simulated flag     -> preserve
```

Do not create an Android-only risk classifier just to make the UI work.

---

# Historical data / Room

The project uses Room to preserve telemetry locally.

This is important because the Android app should eventually be able to provide:

- historical readings
- short-term trends
- daily/weekly/monthly analysis foundations
- exact sample timestamps
- sequence-aware ordering

The repository/data-source abstraction should remain the boundary between the network API and local storage.

A future replay controller can feed the same telemetry fixtures into the repository to simulate changing watch readings without creating fake BLE behavior or modifying the firmware.

---

# Current UI foundations

The Android project currently contains foundations for:

### Home / Dashboard

Displays the current watch risk and synchronization state.

### Live Readings

Displays environmental and physiological values received from telemetry.

### Trends

Provides the framework for historical time windows such as:

```text
30 MIN
1 HOUR
6 HOURS
24 HOURS
7 DAYS
30 DAYS
```

### Insights

Explains why the watch classified the current state using the received risk-reason bitmask.

### Device

Shows watch identity and transport information.

### SOS

Provides a UI foundation for an SOS event. It does not automatically contact emergency services.

---

# Security considerations

The current backend is intended for local development and prototype demonstration.

Do not expose an unauthenticated development server directly to the public Internet.

Before a production-style deployment, add at minimum:

- HTTPS
- authentication/device identity
- authorization
- secure secret handling
- input validation
- rate limiting where appropriate
- protected database/storage
- logging and monitoring
- safe handling of SOS events

---

# Development philosophy

The project should be developed incrementally and each layer should be verified before adding the next one.

Recommended order:

```text
1. Backend receives telemetry
       |
2. Backend returns latest telemetry
       |
3. Android reaches backend
       |
4. Android parses telemetry
       |
5. Android updates UI
       |
6. Room preserves samples
       |
7. Simulated readings change over time
       |
8. ESP32-S3 becomes the actual telemetry source
       |
9. BLE/Wi-Fi transport is implemented
```

Do not jump directly to physical watch networking while Android-to-backend connectivity is still unverified.

---

# What is currently working

- [x] FastAPI backend starts locally
- [x] Swagger/OpenAPI documentation is available
- [x] Telemetry POST endpoint exists
- [x] Valid simulated telemetry can be submitted
- [x] Telemetry can be retrieved through the latest-reading endpoint
- [x] Device ID `mzuriair-001` is being used consistently
- [x] Android project exists and builds as a Java app
- [x] Retrofit/OkHttp architecture exists
- [x] Room/repository/ViewModel architecture exists
- [x] UI foundations exist

# What still needs to be solved

- [ ] Android physical-phone connection to the local backend
- [ ] Verify Retrofit request reaches FastAPI from Android
- [ ] Verify JSON response maps cleanly into the Android `Telemetry` model
- [ ] Confirm Home screen updates from real API data
- [ ] Confirm Live/Insights pages do not create duplicate observers during navigation
- [ ] Add reliable polling/refresh strategy
- [ ] Verify Room persistence with live API responses
- [ ] Add replay/simulation controller using the published telemetry fixtures
- [ ] Decide and implement final transport: Wi-Fi, BLE, or both
- [ ] Configure ESP32-S3 network transport
- [ ] Replace simulated values with real sensor readings when hardware is ready

---

# How a contributor can help debug the current issue

Please do **not** start by rewriting the backend.

The backend telemetry flow has already been verified.

The most useful information when investigating the Android connectivity problem is:

### Environment

```text
Computer OS:
Android Studio version:
Android phone model:
Android version:
USB or Wi-Fi:
```

### Backend

```text
FastAPI start command:
Backend URL on computer:
Does /docs open on computer? yes/no
Does /docs open on phone browser? yes/no
```

### Android

```text
Configured Retrofit base URL:
ADB output from `adb devices`:
ADB reverse result, if used:
Exact Logcat error:
```

### API

```text
Device ID: mzuriair-001
GET /api/devices/mzuriair-001/latest works in Swagger: yes/no
```

The exact error message is more useful than screenshots alone. Preserve the original Logcat/HTTP error when reporting an issue.

---

# Suggested Git workflow

Keep the Android and backend changes in the same repository so a contributor can reproduce the full system.

Suggested branches:

```text
main
feature/android-api
feature/backend
feature/firmware-network
feature/ble
```

Commit functional steps separately, for example:

```text
backend: verify telemetry POST and latest GET
android: replace placeholder API base URL
android: add local HTTP development configuration
android: display latest telemetry on dashboard
android: add polling for latest telemetry
```

---

# Final project objective

The final MzuriAIR prototype should demonstrate this complete chain:

```text
ESP32-S3 MzuriAIR Watch
        |
        | simulated or real telemetry
        v
 MzuriAIR Companion API
        |
        | REST/JSON
        v
 Android Companion App
        |
        +--> live dashboard
        +--> historical data
        +--> insights
        +--> device status
        +--> recommendations
```

The core principle is that **the watch/firmware produces the risk interpretation, while Android acts as a companion interface and historical data client**.

---

# Status

**Current milestone:** Backend telemetry is verified. Android-to-backend connectivity from the deployed physical phone remains the active integration issue.

**Current device ID:** `mzuriair-001`

**Backend:** FastAPI/Uvicorn, local development

**Android:** Java, Android Studio, Retrofit/OkHttp, Room

**Watch:** ESP32-S3, simulated telemetry, Wi-Fi not yet configured, BLE future TODO
