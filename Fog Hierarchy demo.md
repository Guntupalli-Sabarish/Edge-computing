//Assignment - 6



## Overview

The **FogHierarchyDemo** project is a Java-based simulation using the **iFogSim** toolkit concepts. It demonstrates the implementation of a **Three-Tier Fog Architecture** (Cloud → Fog Gateway → Edge). The simulation focuses on establishing parent-child relationships between nodes and simulating realistic **Network Latency** to showcase the benefits of connecting edge devices to nearby gateways rather than distant cloud servers.

## Table of Contents

1. [Problem Statement](https://www.google.com/search?q=%23problem-statement)
2. [Solution Architecture](https://www.google.com/search?q=%23solution-architecture)
3. [Implementation Details](https://www.google.com/search?q=%23implementation-details)
4. [Features](https://www.google.com/search?q=%23features)
5. [Code Structure](https://www.google.com/search?q=%23code-structure)
6. [Running the Simulation](https://www.google.com/search?q=%23running-the-simulation)
7. [Expected Output](https://www.google.com/search?q=%23expected-output)
8. [Performance Analysis](https://www.google.com/search?q=%23performance-analysis)
9. [Hierarchy Statistics](https://www.google.com/search?q=%23hierarchy-statistics)

---

## Problem Statement

In real-world IoT scenarios, minimizing latency is critical. This simulation addresses the following specific requirements:

1. **Hierarchical Organization**: Establish a clear chain of command: Cloud (Level 0) → Gateway (Level 1) → Edge (Level 2).
2. **Proximity-Aware Connectivity**: Connect Edge devices to specific, "nearby" Gateways rather than a single central point.
3. **Latency Simulation**: Model different network delays for different layers (High latency for Cloud uplink, Low latency for Local uplink).
4. **Topology Verification**: Programmatically verify that the parent-child linkages and latency values are correctly assigned.

---

## Solution Architecture

### Three-Tier Fog Topology

```
┌─────────────────────────────────────────────────────────────┐
│                         CLOUD LAYER                         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                     Cloud_Node                        │  │
│  │           (Level 0, Parent: -1, Latency: N/A)         │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
          ▲                                         ▲
          │ Uplink: 100ms                           │ Uplink: 100ms
          ▼                                         ▼
┌─────────────────────────────────────────────────────────────┐
│                       FOG GATEWAY LAYER                     │
│                                                             │
│    Gateway_1 (Level 1)               Gateway_2 (Level 1)    │
│  ┌─────────────────────┐           ┌─────────────────────┐  │
│  │ Aggregates Edge 1&2 │           │ Aggregates Edge 3   │  │
│  └──────────▲──────────┘           └──────────▲──────────┘  │
└─────────────┼─────────────────────────────────┼─────────────┘
              │ Uplink: 10ms                    │ Uplink: 10ms
              ▼                                 ▼
┌─────────────────────────────────────────────────────────────┐
│                       EDGE DEVICE LAYER                     │
│                                                             │
│  ┌──────┐    ┌──────┐                 ┌──────┐              │
│  │Edge_1│    │Edge_2│                 │Edge_3│              │
│  └──────┘    └──────┘                 └──────┘              │
│  (Level 2)   (Level 2)                (Level 2)             │
└─────────────────────────────────────────────────────────────┘

```

### Node Specifications

| Device Name | Level | MIPS | Uplink Latency | Parent Node |
| --- | --- | --- | --- | --- |
| **Cloud_Node** | 0 | 44,800 | N/A | None (-1) |
| **Gateway_1** | 1 | 2,800 | 100.0 ms | Cloud_Node |
| **Gateway_2** | 1 | 2,800 | 100.0 ms | Cloud_Node |
| **Edge_1** | 2 | 1,000 | 10.0 ms | Gateway_1 |
| **Edge_2** | 2 | 1,000 | 10.0 ms | Gateway_1 |
| **Edge_3** | 2 | 1,000 | 10.0 ms | Gateway_2 |

---

## Implementation Details

### Core Logic

#### 1. Defining Parent-Child Relationships

The hierarchy is built by assigning the unique ID of a higher-level device (Parent) to a lower-level device (Child). This creates the routing path for data transmission.

```java
// Logic from FogHierarchyDemo.java
// Connect Edge_1 to Gateway_1
edge1.setParentId(gateway1.getId()); 

// Connect Gateway_1 to Cloud
gateway1.setParentId(cloud.getId());

```

#### 2. Configuring Network Latency

To simulate physical distance and network quality, we use `setUplinkLatency()`. This value represents the time taken for a packet to travel from the child to the parent.

```java
// Simulating WAN connection (Slow)
gateway1.setUplinkLatency(100.0); 

// Simulating LAN/WiFi connection (Fast)
edge1.setUplinkLatency(10.0);

```

---

## Features

### ✅ **Multi-Tiered Architecture**

* Implements a strict 3-level depth (Level 0 to Level 2).
* Demonstrates how `FogDevice` characteristics (like `level` and `ratePerMips`) are inherited and customized.

### ✅ **Realistic Network Modeling**

* Differentiates between **Local Fog connectivity** (10ms) and **Cloud connectivity** (100ms).
* Allows for performance evaluation based on network topology rather than just CPU speed.

### ✅ **Topology Verification**

* Includes a `printHierarchy()` utility that traverses the device list.
* Dynamically resolves Parent IDs to readable names for debugging and validation.

---

## Code Structure

### Main Classes

```java
public class FogHierarchyDemo {
    // Main simulation driver
    public static void main(String[] args)        // Entry point
    
    // Factory method
    private static FogDevice createFogDevice(...) // Creates devices with specific MIPS/RAM
    
    // Verification utility
    private static void printHierarchy()          // Prints the table of connections
}

```

---

## Running the Simulation

### Prerequisites

* Java JDK 8 or higher.
* **iFogSim Toolkit** libraries added to the classpath.

### Compilation and Execution

```bash
# Navigate to project root
cd workspace/iFogSim

# Compile
javac -cp "jars/*:." org/fog/healthsim/FogHierarchyDemo.java

# Run
java -cp "jars/*:." org.fog.healthsim.FogHierarchyDemo

```

---

## Expected Output

### Console Output Sample

```
Starting FogHierarchyDemo...

==========================================
       FOG HIERARCHY CONFIGURATION       
==========================================
Device: Cloud_Node | Level: 0 | Parent: None       | Uplink Latency: 0.0 ms
Device: Gateway_1  | Level: 1 | Parent: Cloud_Node | Uplink Latency: 100.0 ms
Device: Gateway_2  | Level: 1 | Parent: Cloud_Node | Uplink Latency: 100.0 ms
Device: Edge_1     | Level: 2 | Parent: Gateway_1  | Uplink Latency: 10.0 ms
Device: Edge_2     | Level: 2 | Parent: Gateway_1  | Uplink Latency: 10.0 ms
Device: Edge_3     | Level: 2 | Parent: Gateway_2  | Uplink Latency: 10.0 ms
==========================================

FogHierarchyDemo finished!

```

---

## Performance Analysis

This section discusses the theoretical impact of the topology implemented in the code (Proximity-Aware Connectivity).

### 1. Latency

* **Effect:** ** drastically Reduced.**
* **Why:** By connecting `Edge_1` to `Gateway_1` (10ms), the Round Trip Time (RTT) for local processing is ~20ms. If `Edge_1` connected directly to the Cloud, the RTT would be >200ms.
* **Simulation Evidence:** The code explicitly sets `edge1.setUplinkLatency(10.0)` vs `gateway1.setUplinkLatency(100.0)`.

### 2. Energy Consumption

* **Effect:** **Lowered.**
* **Why:** Wireless transmission power increases significantly with distance. Transmitting to a nearby Gateway requires less signal amplification than transmitting to a distant Cloud server or cell tower.
* **Context:** While this specific code sets static power profiles, in a full simulation, the `EnergyModel` would calculate lower usage for short-range transmission.

### 3. Task Offloading Performance

* **Effect:** **Optimized.**
* **Why:** "Proximity-aware" means tasks are offloaded to the *nearest* available resource. If `Edge_1` is overloaded, it can offload to `Gateway_1` quickly. If it had to offload to the Cloud, the transmission time might exceed the deadline of real-time applications (e.g., healthcare monitoring).

---

## Hierarchy Statistics

The `printHierarchy` method provides a snapshot of the configured topology:

| Metric | Value | Description |
| --- | --- | --- |
| **Total Devices** | 6 | 1 Cloud, 2 Gateways, 3 Edge Nodes |
| **Max Depth** | 2 | Cloud (0) → Gateway (1) → Edge (2) |
| **Gateway Load** | Balanced | Gateway 1 handles 2 devices; Gateway 2 handles 1. |

---

## References

* **Toolkit**: [iFogSim GitHub Repository](https://github.com/Cloudslab/iFogSim)
* **Concepts**: Fog Computing Hierarchies & Edge-to-Cloud Continuum.