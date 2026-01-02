//Assignment - 5


package org.fogclustering.example;


import java.util.*;
import java.text.DecimalFormat;
/**
* Simple Fog Clustering Simulation
* Standalone version that doesn&#39;t require iFogSim libraries
*/
public class SimpleFogClustering {
private static List&lt;FogDevice&gt; fogDevices = new ArrayList&lt;&gt;();
private static ClusterManager clusterManager = ClusterManager.getInstance();
private static DecimalFormat df = new DecimalFormat(&quot;0.00&quot;);
public static void main(String[] args) {
System.out.println(&quot;=== Starting Simple Fog Clustering Simulation ===&quot;);
try {
// Step 1: Create fog devices
createFogDevices();
// Step 2: Form clusters
clusterManager.formClusters();
// Step 3: Print results
printResults();
System.out.println(&quot;=== Simulation Completed Successfully ===&quot;);
} catch (Exception e) {
e.printStackTrace();
System.out.println(&quot;Error in simulation: &quot; + e.getMessage());
}
}
private static void createFogDevices() {
System.out.println(&quot;\nCreating 20 Fog Devices...&quot;);
Random random = new Random(42); // Fixed seed for reproducible results

for (int i = 0; i &lt; 20; i++) {
double x = random.nextDouble() * 1000; // 0-1000
double y = random.nextDouble() * 1000; // 0-1000
double energy = 70 + random.nextDouble() * 30; // 70-100%
double load = random.nextDouble() * 50; // 0-50%
int mips = 1000 + random.nextInt(2000); // 1000-3000 MIPS
FogDevice device = new FogDevice(
&quot;FogDevice_&quot; + i,
x, y, energy, load, mips
);
fogDevices.add(device);
clusterManager.addDevice(device);
System.out.println(&quot;Created &quot; + device.name + &quot; at (&quot; + df.format(x) + &quot;, &quot; +
df.format(y) +
&quot;) - Energy: &quot; + df.format(energy) + &quot;%, Load: &quot; + df.format(load) + &quot;%, MIPS:
&quot; + mips);
}
}
private static void printResults() {
System.out.println(&quot;\n&quot; + &quot;=&quot;.repeat(80));
System.out.println(&quot;CLUSTERING FORMATION RESULTS&quot;);
System.out.println(&quot;=&quot;.repeat(80));
System.out.println(&quot;Total Fog Devices: &quot; + fogDevices.size());
System.out.println(&quot;Total Clusters Formed: &quot; + clusterManager.getClusterCount());
System.out.println(&quot;\nDETAILED CLUSTER INFORMATION:&quot;);
System.out.println(&quot;-&quot;.repeat(80));
// Print all devices with their cluster info
for (FogDevice device : fogDevices) {
String role = device.isClusterHead ? &quot;CLUSTER HEAD&quot; : &quot;Member&quot;;
System.out.printf(&quot;%-15s | Cluster: %2d | %-12s | Location: (%6s, %6s) | Energy:
%5s%% | Load: %5s%% | MIPS: %4d%n&quot;,
device.name,
device.clusterId,
role,
df.format(device.x),
df.format(device.y),
df.format(device.energy),
df.format(device.load),
device.mips);

}
// Print cluster head summary
System.out.println(&quot;\nCLUSTER HEAD SUMMARY:&quot;);
System.out.println(&quot;-&quot;.repeat(80));
List&lt;FogDevice&gt; clusterHeads = clusterManager.getClusterHeads();
for (FogDevice head : clusterHeads) {
System.out.println(&quot;Cluster &quot; + head.clusterId + &quot; Head: &quot; + head.name +
&quot; with &quot; + head.clusterMembers.size() + &quot; members&quot; +
&quot; | Avg Distance: &quot; + df.format(calculateAvgDistance(head)) + &quot; units&quot;);
}
// Print statistics
printStatistics();
}
private static double calculateAvgDistance(FogDevice head) {
if (head.clusterMembers.size() &lt;= 1) return 0.0;
double totalDistance = 0;
int count = 0;
for (FogDevice member : head.clusterMembers) {
if (member != head) {
totalDistance += head.calculateDistance(member);
count++;
}
}
return count &gt; 0 ? totalDistance / count : 0;
}
private static void printStatistics() {
System.out.println(&quot;\nCLUSTERING STATISTICS:&quot;);
System.out.println(&quot;-&quot;.repeat(80));
int totalDevices = fogDevices.size();
int clusterHeads = 0;
int maxClusterSize = 0;
int minClusterSize = Integer.MAX_VALUE;
double totalEnergy = 0;
double totalLoad = 0;
for (FogDevice device : fogDevices) {
totalEnergy += device.energy;
totalLoad += device.load;
if (device.isClusterHead) {
clusterHeads++;

int clusterSize = device.clusterMembers.size();
maxClusterSize = Math.max(maxClusterSize, clusterSize);
minClusterSize = Math.min(minClusterSize, clusterSize);
}
}
double avgEnergy = totalEnergy / totalDevices;
double avgLoad = totalLoad / totalDevices;
double avgClusterSize = (double) totalDevices / clusterHeads;
System.out.println(&quot;Number of Cluster Heads: &quot; + clusterHeads);
System.out.println(&quot;Average Cluster Size: &quot; + df.format(avgClusterSize) + &quot; devices&quot;);
System.out.println(&quot;Largest Cluster: &quot; + maxClusterSize + &quot; devices&quot;);
System.out.println(&quot;Smallest Cluster: &quot; + minClusterSize + &quot; devices&quot;);
System.out.println(&quot;Average Energy Level: &quot; + df.format(avgEnergy) + &quot;%&quot;);
System.out.println(&quot;Average Load: &quot; + df.format(avgLoad) + &quot;%&quot;);
// Calculate clustering efficiency
double efficiency = calculateClusteringEfficiency();
System.out.println(&quot;Clustering Efficiency Score: &quot; + df.format(efficiency) + &quot;/100&quot;);
}
private static double calculateClusteringEfficiency() {
// Simple efficiency metric based on balanced clusters and energy distribution
double score = 0.0;
List&lt;FogDevice&gt; heads = clusterManager.getClusterHeads();
if (heads.isEmpty()) return 0.0;
// Balance score (how balanced are the cluster sizes)
int totalDevices = fogDevices.size();
int idealSize = totalDevices / heads.size();
double balanceScore = 0.0;
for (FogDevice head : heads) {
int size = head.clusterMembers.size();
double deviation = Math.abs(size - idealSize);
balanceScore += (1 - deviation / idealSize);
}
balanceScore = (balanceScore / heads.size()) * 50; // 50% of total score
// Energy score (how good are the cluster head selections)
double energyScore = 0.0;
for (FogDevice head : heads) {
energyScore += head.energy;
}
energyScore = (energyScore / heads.size()) * 0.5; // 50% of total score

return balanceScore + energyScore;
}
}
class FogDevice {
String name;
double x, y;
double energy; // residual energy %
double load; // current load %
int mips; // processing power
boolean isClusterHead;
int clusterId;
List&lt;FogDevice&gt; clusterMembers;
public FogDevice(String name, double x, double y, double energy, double load, int mips) {
this.name = name;
this.x = x;
this.y = y;
this.energy = energy;
this.load = load;
this.mips = mips;
this.isClusterHead = false;
this.clusterId = -1;
this.clusterMembers = new ArrayList&lt;&gt;();
}
public double calculateDistance(FogDevice other) {
double dx = this.x - other.x;
double dy = this.y - other.y;
return Math.sqrt(dx * dx + dy * dy);
}
public double calculateWeight(double avgDistance) {
double alpha = 0.5, beta = 0.3, gamma = 0.2;
return (alpha * energy) - (beta * avgDistance) - (gamma * load);
}
public void addClusterMember(FogDevice device) {
if (!clusterMembers.contains(device)) {
clusterMembers.add(device);
}
}
}
class ClusterManager {
private static ClusterManager instance;

private List&lt;FogDevice&gt; allDevices;
private Map&lt;Integer, FogDevice&gt; clusterHeads;
private final double MAX_DISTANCE = 250.0; // Maximum distance for cluster formation
private int nextClusterId = 1;
private ClusterManager() {
this.allDevices = new ArrayList&lt;&gt;();
this.clusterHeads = new HashMap&lt;&gt;();
}
public static ClusterManager getInstance() {
if (instance == null) {
instance = new ClusterManager();
}
return instance;
}
public void addDevice(FogDevice device) {
allDevices.add(device);
}
public void formClusters() {
System.out.println(&quot;\nForming clusters with &quot; + allDevices.size() + &quot; devices...&quot;);
System.out.println(&quot;Maximum cluster distance: &quot; + MAX_DISTANCE);
List&lt;FogDevice&gt; unclustered = new ArrayList&lt;&gt;(allDevices);
Random random = new Random(42);
while (!unclustered.isEmpty()) {
// Start with device having highest energy as temporary head
FogDevice tempHead = findDeviceWithHighestEnergy(unclustered);
tempHead.clusterId = nextClusterId;
unclustered.remove(tempHead);
List&lt;FogDevice&gt; clusterMembers = new ArrayList&lt;&gt;();
clusterMembers.add(tempHead);
// Find nearby devices to join cluster
Iterator&lt;FogDevice&gt; iterator = unclustered.iterator();
while (iterator.hasNext()) {
FogDevice device = iterator.next();
double distance = tempHead.calculateDistance(device);
if (distance &lt;= MAX_DISTANCE) {
clusterMembers.add(device);
device.clusterId = nextClusterId;
iterator.remove();
System.out.println(&quot; Added &quot; + device.name + &quot; to cluster &quot; + nextClusterId +

&quot; (distance: &quot; + String.format(&quot;%.2f&quot;, distance) + &quot; units)&quot;);
}
}
// Elect permanent cluster head
FogDevice electedHead = electClusterHead(clusterMembers);
electedHead.isClusterHead = true;
// Set up cluster members
for (FogDevice member : clusterMembers) {
electedHead.addClusterMember(member);
if (member != electedHead) {
member.isClusterHead = false;
}
}
clusterHeads.put(nextClusterId, electedHead);
System.out.println(&quot;✓ Cluster &quot; + nextClusterId + &quot; formed: &quot; + electedHead.name +
&quot; (Head) with &quot; + (clusterMembers.size() - 1) + &quot; members&quot;);
nextClusterId++;
}
}
private FogDevice findDeviceWithHighestEnergy(List&lt;FogDevice&gt; devices) {
return devices.stream()
.max(Comparator.comparing(device -&gt; device.energy))
.orElse(devices.get(0));
}
private FogDevice electClusterHead(List&lt;FogDevice&gt; clusterMembers) {
// Calculate average distance for each device to all other members
Map&lt;FogDevice, Double&gt; avgDistances = new HashMap&lt;&gt;();
for (FogDevice device : clusterMembers) {
double totalDistance = 0.0;
int count = 0;
for (FogDevice other : clusterMembers) {
if (device != other) {
totalDistance += device.calculateDistance(other);
count++;
}
}
double avgDistance = count &gt; 0 ? totalDistance / count : 0;
avgDistances.put(device, avgDistance);
}

// Elect device with highest weight
return clusterMembers.stream()
.max(Comparator.comparing(device -&gt;
device.calculateWeight(avgDistances.get(device))))
.orElse(clusterMembers.get(0));
}
public FogDevice getClusterHeadForDevice(FogDevice device) {
return clusterHeads.get(device.clusterId);
}
public List&lt;FogDevice&gt; getClusterHeads() {
return new ArrayList&lt;&gt;(clusterHeads.values());
}
public int getClusterCount() {
return clusterHeads.size();
}
}