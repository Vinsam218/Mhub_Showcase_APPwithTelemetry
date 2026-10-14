from typing import Any

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field


app = FastAPI(
    title="MzuriAIR Companion API",
    version="1.0.0",
)


# -----------------------------
# Telemetry models
# -----------------------------

class Risk(BaseModel):
    level: str
    aqi: int
    dominant: str
    reasons: int


class Vitals(BaseModel):
    hr: float | None = None
    wheeze: bool | None = None
    wheeze_db: float | None = None
    activity: str = "unknown"


class DeviceStatus(BaseModel):
    battery: int
    charging: bool
    rssi: int


class Telemetry(BaseModel):
    device: str
    ts: int
    seq: int

    event: str | None = None

    risk: Risk
    metrics: dict[str, float] = Field(default_factory=dict)
    vitals: Vitals
    device_status: DeviceStatus

    simulated: bool = False


# -----------------------------
# Temporary in-memory storage
# -----------------------------

telemetry_store: dict[str, list[Telemetry]] = {}


# -----------------------------
# Basic endpoints
# -----------------------------

@app.get("/")
def root():
    return {
        "service": "MzuriAIR Companion API",
        "status": "online",
    }


@app.get("/health")
def health():
    return {
        "status": "healthy"
    }


# -----------------------------
# Watch telemetry ingestion
# -----------------------------

@app.post("/api/devices/{device_id}/telemetry")
def receive_telemetry(device_id: str, telemetry: Telemetry):

    # Make sure the URL device ID agrees with the watch payload.
    if telemetry.device != device_id:
        raise HTTPException(
            status_code=400,
            detail="Device ID mismatch"
        )

    if device_id not in telemetry_store:
        telemetry_store[device_id] = []

    telemetry_store[device_id].append(telemetry)

    # Keep only the latest 1000 samples for now.
    telemetry_store[device_id] = telemetry_store[device_id][-1000:]

    print(
        f"[TELEMETRY] "
        f"device={device_id} "
        f"seq={telemetry.seq} "
        f"risk={telemetry.risk.level} "
        f"aqi={telemetry.risk.aqi}"
    )

    return {
        "status": "accepted",
        "device": device_id,
        "seq": telemetry.seq,
    }

@app.get("/api/devices/{device_id}/latest")
def get_latest_telemetry(device_id: str):

    samples = telemetry_store.get(device_id, [])

    if not samples:
        raise HTTPException(
            status_code=404,
            detail="No telemetry available for this device"
        )

    latest = samples[-1]

    return latest