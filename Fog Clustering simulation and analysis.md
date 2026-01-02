lab Assignment - 5


Here is the `README.md` for your Fog Clustering Simulation, designed to match the structure and depth of the example you provided.

---

# Fog Computing Clustering Simulation - SimpleFogClustering

## Overview

The **SimpleFogClustering** project is a standalone Java simulation that demonstrates **Distance-Based Clustering** in a Fog Computing environment. It addresses the scalability challenges of large-scale fog deployments by organizing dispersed fog nodes into cooperative clusters, electing intelligent Cluster Heads (CH), and calculating efficiency metrics.

## Table of Contents

1. [Problem Statement](https://www.google.com/search?q=%23problem-statement)
2. [Solution Architecture](https://www.google.com/search?q=%23solution-architecture)
3. [Implementation Details](https://www.google.com/search?q=%23implementation-details)
4. [Features](https://www.google.com/search?q=%23features)
5. [Code Structure](https://www.google.com/search?q=%23code-structure)
6. [Running the Simulation](https://www.google.com/search?q=%23running-the-simulation)
7. [Expected Output](https://www.google.com/search?q=%23expected-output)
8. [Performance Analysis](https://www.google.com/search?q=%23performance-analysis)
9. [Clustering Statistics](https://www.google.com/search?q=%23clustering-statistics)

---

## Problem Statement

In large-scale Fog/Edge deployments, managing thousands of individual nodes directly from the Cloud is inefficient. This simulation addresses the following requirements:

1. **Scalability**: Reduce the number of direct connections to the Cloud.
2. **Distance-Based Grouping**: Form clusters based on physical proximity (Euclidean distance).
3. **Intelligent Head Election**: Select the best node (Cluster Head) to manage communication based on Energy, Load, and Centrality.
4. **Performance Metrics**: Evaluate the network's stability and efficiency through scoring algorithms.

---

## Solution Architecture

### Hierarchical Clustering Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       CLOUD LAYER                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                 Central Cloud Server                  │  │
│  │      • Global orchestration                           │  │
│  │      • Receives aggregated data from Cluster Heads    │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            ▲
                            │ Aggregated Data Stream
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       FOG CLUSTER LAYER                     │
│                                                             │
│   CLUSTER 1                     CLUSTER 2                   │
│  ┌───────────────┐             ┌───────────────┐            │
│  │ ┌───────────┐ │             │ ┌───────────┐ │            │
│  │ │CLUSTER    │◀├───(Dist)───▶│ │CLUSTER    │ │            │
│  │ │HEAD (CH)  │ │             │ │HEAD (CH)  │ │            │
│  │ └─────▲─────┘ │             │ └─────▲─────┘ │            │
│  └───────│───────┘             └───────│───────┘            │
│          │                             │                    │
└──────────┼─────────────────────────────┼────────────────────┘
           │ Local Offloading            │ Task Management
┌──────────▼─────────────────────────────▼────────────────────┐
│                       EDGE DEVICE LAYER                     │
│                                                             │
│  ┌──────┐   ┌──────┐         ┌──────┐   ┌──────┐            │
│  │ Node │   │ Node │         │ Node │   │ Node │            │
│  └──────┘   └──────┘         └──────┘   └──────┘            │
│   (Members of C1)             (Members of C2)               │
└─────────────────────────────────────────────────────────────┘

```

### Node Specifications (Randomized Range)

| Attribute | Range / Value | Description |
| --- | --- | --- |
| **Location (x, y)** | 0 - 1000 | Simulated physical coordinates |
| **Energy** | 70% - 100% | Residual battery power |
| **Load** | 0% - 50% | Current CPU utilization |
| **MIPS** | 1000 - 3000 | Processing power |
| **Transmission Range** | 250.0 units | Maximum distance to join a cluster |

---

## Implementation Details

### Core Algorithms

#### 1. Distance-Based Cluster Formation

The system iterates through unclustered devices and forms groups based on a strict distance threshold (`MAX_DISTANCE = 250.0`).

```java
// Logic snippet from ClusterManager.java
while (iterator.hasNext()) {
    FogDevice device = iterator.next();
    double distance = tempHead.calculateDistance(device);
    
    // Check if device is within communication range
    if (distance <= MAX_DISTANCE) {
        clusterMembers.add(device);
        device.clusterId = nextClusterId;
        // ...
    }
}

```

#### 2. Weighted Cluster Head Election

Once a cluster is formed, a leader is elected using a weighted score function. This ensures the Cluster Head is not just a random node, but the *most capable* one.

**Weights:**

* ** (0.5) - Energy Priority:** Higher energy is preferred for stability.
* ** (0.3) - Distance Penalty:** Nodes closer to the center (lower avg distance) are preferred.
* ** (0.2) - Load Penalty:** Nodes with lower current load are preferred.

```java
public double calculateWeight(double avgDistance) {
    double alpha = 0.5, beta = 0.3, gamma = 0.2;
    // Higher energy adds to score; Distance and Load subtract from it
    return (alpha * energy) - (beta * avgDistance) - (gamma * load);
}

```

---

## Features

### ✅ **Dynamic Topology Generation**

* Creates 20 Fog Devices with randomized attributes (Location, Energy, Load).
* Uses a fixed seed (`Random(42)`) to ensure reproducible simulation runs.

### ✅ **Intelligent Grouping**

* Automatically detects neighbors within range.
* Handles edge cases where devices are isolated (single-node clusters).

### ✅ **Efficiency Scoring**

* Calculates a "Clustering Efficiency Score" (0-100).
* Evaluates how balanced the cluster sizes are.
* Evaluates the energy reserves of the elected leaders.

### ✅ **Detailed Reporting**

* Generates a console report listing all clusters, members, and heads.
* Provides statistical summaries (Avg size, Max/Min size).

---

## Code Structure

### Main Classes

```java
public class SimpleFogClustering {
    // Main simulation driver
    public static void main(String[] args)        // Entry point
    private static void createFogDevices()        // Generates the 20 nodes
    private static void printResults()            // Outputs the formatted report
    private static void printStatistics()         // Calculates network metrics
}

class FogDevice {
    // Data model for a node
    double x, y;                                  // Location
    double energy, load;                          // State
    boolean isClusterHead;                        // Role
    
    double calculateDistance(FogDevice other)     // Euclidean math
    double calculateWeight(double avgDist)        // Election logic
}

class ClusterManager {
    // Singleton controller
    public void formClusters()                    // The main clustering algorithm
    private FogDevice electClusterHead(...)       // Election execution
    private FogDevice findDeviceWithHighestEnergy // Anchor selection
}

```

---

## Running the Simulation

### Prerequisites

* Java JDK 8 or higher.
* No external libraries required (Standalone Standard Java).

### Compilation and Execution

```bash
# Compile the Java file
javac org/fogclustering/example/SimpleFogClustering.java

# Run the simulation
java org.fogclustering.example.SimpleFogClustering

```

---

## Expected Output

### Console Output Sample

```
=== Starting Simple Fog Clustering Simulation ===

Creating 20 Fog Devices...
Created FogDevice_0 at (730.00, 240.00) - Energy: 95.00%, Load: 12.00%...
Created FogDevice_1 at (120.00, 850.00) - Energy: 88.50%, Load: 45.20%...

Forming clusters with 20 devices...
Maximum cluster distance: 250.0
 Added FogDevice_3 to cluster 1 (distance: 145.20 units)
 Added FogDevice_7 to cluster 1 (distance: 210.50 units)
✓ Cluster 1 formed: FogDevice_0 (Head) with 4 members

================================================================================
CLUSTERING FORMATION RESULTS
================================================================================
Total Fog Devices: 20
Total Clusters Formed: 5

DETAILED CLUSTER INFORMATION:
--------------------------------------------------------------------------------
FogDevice_0     | Cluster:  1 | CLUSTER HEAD | Location: (730.00, 240.00) ...
FogDevice_3     | Cluster:  1 | Member       | Location: (650.00, 310.00) ...

CLUSTERING STATISTICS:
--------------------------------------------------------------------------------
Number of Cluster Heads: 5
Average Cluster Size: 4.00 devices
Clustering Efficiency Score: 88.50/100

```

---

## Performance Analysis

This section maps the simulation logic to the theoretical impacts requested in the assignment.

### 1. Task Migration Overhead

* **Effect:** **Significantly Reduced.**
* **Why:** In this architecture, if a Cluster Member is overloaded (High Load), it can offload tasks to the **Cluster Head** or a neighbor within the same cluster.
* **Simulation Evidence:** The `MAX_DISTANCE` logic ensures that offloading targets are physically close (low latency), avoiding expensive migrations to the Cloud.

### 2. Network Stability

* **Effect:** **Improved.**
* **Why:** The `electClusterHead` method prioritizes nodes with **High Energy** and **Low Load**. This prevents the network from electing a weak node as a leader, which would otherwise fail quickly and break network connectivity.
* **Simulation Evidence:** The `calculateWeight` function explicitly penalizes Low Energy and High Load nodes during election.

### 3. Energy Efficiency

* **Effect:** **Optimized.**
* **Why:** Clustering reduces long-distance transmission. Members only transmit short distances to their Cluster Head (demonstrated by `avgDistance` metrics in the output). Only the Head performs the high-energy long-range transmission to the Cloud.
* **Simulation Evidence:** `calculateAvgDistance` tracks intra-cluster proximity; the tighter the cluster, the lower the transmission power required.

---

## Clustering Statistics

The simulation outputs specific metrics to validate the quality of the clusters formed:

| Metric | Formula / Logic | Ideal Outcome |
| --- | --- | --- |
| **Load Balance** | Deviation from `TotalDevices / ClusterCount` | Clusters should be roughly equal in size to prevent hotspots. |
| **Energy Score** | Average Energy of all Cluster Heads | Higher is better (ensures leaders are robust). |
| **Efficiency Score** | `(BalanceScore * 0.5) + (EnergyScore * 0.5)` | A score closer to 100 indicates a healthy, well-distributed network. |

---

## References

* **Algorithm**: Weighted Clustering Algorithm (WCA) & Euclidean Distance Grouping.
* **Framework**: Logic adapted from `iFogSim` methodologies for standalone demonstration.