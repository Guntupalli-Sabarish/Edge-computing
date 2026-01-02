//Assignment - 6


package org.fog.healthsim;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogUtils;

/**
 * FogHierarchyDemo
 * Demonstrates the connection of lower-level fog devices (Edge) 
 * to nearby Gateways and subsequently to the Cloud.
 */
public class FogHierarchyDemo {

    static List<FogDevice> fogDevices = new ArrayList<FogDevice>();

    public static void main(String[] args) {
        Log.printLine("Starting FogHierarchyDemo...");

        try {
            // 1. Initialize CloudSim
            int num_user = 1;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;
            CloudSim.init(num_user, calendar, trace_flag);

            String appId = "test_app";
            FogBroker broker = new FogBroker("Broker");

            // ==========================================
            // STEP 1: Create Cloud Node (Level 0)
            // ==========================================
            FogDevice cloud = createFogDevice("Cloud_Node", 44800, 40000, 100, 10000, 0, 0.01, 16 * 103, 16 * 83.25);
            cloud.setParentId(-1); // Cloud has no parent
            fogDevices.add(cloud);

            // ==========================================
            // STEP 2: Create Gateway Nodes (Level 1)
            // ==========================================
            // Gateways connect to the Cloud. Latency Cloud <-> Gateway = 100ms
            
            FogDevice gateway1 = createFogDevice("Gateway_1", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.433);
            gateway1.setParentId(cloud.getId());
            gateway1.setUplinkLatency(100.0); // 100ms latency to Cloud
            fogDevices.add(gateway1);

            FogDevice gateway2 = createFogDevice("Gateway_2", 2800, 4000, 10000, 10000, 1, 0.0, 107.339, 83.433);
            gateway2.setParentId(cloud.getId());
            gateway2.setUplinkLatency(100.0); // 100ms latency to Cloud
            fogDevices.add(gateway2);

            // ==========================================
            // STEP 3: Create Edge Devices (Level 2)
            // ==========================================
            // Edge devices connect to specific Gateways. Latency Gateway <-> Edge = 10ms

            // Edge1 and Edge2 connect to Gateway1
            FogDevice edge1 = createFogDevice("Edge_1", 1000, 1000, 1000, 1000, 2, 0.0, 87.53, 82.44);
            edge1.setParentId(gateway1.getId()); // Connect to Gateway 1
            edge1.setUplinkLatency(10.0);        // 10ms latency to Gateway
            fogDevices.add(edge1);

            FogDevice edge2 = createFogDevice("Edge_2", 1000, 1000, 1000, 1000, 2, 0.0, 87.53, 82.44);
            edge2.setParentId(gateway1.getId()); // Connect to Gateway 1
            edge2.setUplinkLatency(10.0);        // 10ms latency to Gateway
            fogDevices.add(edge2);

            // Edge3 connects to Gateway2
            FogDevice edge3 = createFogDevice("Edge_3", 1000, 1000, 1000, 1000, 2, 0.0, 87.53, 82.44);
            edge3.setParentId(gateway2.getId()); // Connect to Gateway 2
            edge3.setUplinkLatency(10.0);        // 10ms latency to Gateway
            fogDevices.add(edge3);

            // ==========================================
            // VERIFICATION: Print Hierarchy
            // ==========================================
            printHierarchy();

            CloudSim.startSimulation();
            CloudSim.stopSimulation();
            Log.printLine("FogHierarchyDemo finished!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    /**
     * Helper method to print the connected topology
     */
    private static void printHierarchy() {
        System.out.println("\n==========================================");
        System.out.println("       FOG HIERARCHY CONFIGURATION       ");
        System.out.println("==========================================");
        for (FogDevice device : fogDevices) {
            String parentName = "None";
            // Find parent name by ID
            for (FogDevice potentialParent : fogDevices) {
                if (potentialParent.getId() == device.getParentId()) {
                    parentName = potentialParent.getName();
                    break;
                }
            }
            
            System.out.println(String.format("Device: %-10s | Level: %d | Parent: %-10s | Uplink Latency: %.1f ms",
                    device.getName(),
                    device.getLevel(),
                    parentName,
                    device.getUplinkLatency()));
        }
        System.out.println("==========================================\n");
    }

    /**
     * Helper method to create a Fog Device with standard properties
     */
    private static FogDevice createFogDevice(String nodeName, long mips,
            int ram, long upBw, long downBw, int level, double ratePerMips, double busyPower, double idlePower) {
        
        List<Pe> peList = new ArrayList<Pe>();
        // Create PEs (Processing Elements)
        peList.add(new Pe(0, new PeProvisionerOverbooking(mips))); 

        int hostId = FogUtils.generateEntityId();
        long storage = 1000000; // host storage
        int bw = 10000;

        PowerHost host = new PowerHost(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerOverbooking(bw),
                storage,
                peList,
                new StreamOperatorScheduler(peList),
                new FogDeviceCharacteristics(
                        "x86", "Linux", "Xen", new Host(hostId, new RamProvisionerSimple(ram), 
                        new BwProvisionerOverbooking(bw), storage, peList, 
                        new StreamOperatorScheduler(peList)).toString(), 10.0, 3.0, 0.05, 0.001, 0.0, 0.05));

        List<Host> hostList = new ArrayList<Host>();
        hostList.add(host);

        String arch = "x86"; 
        String os = "Linux"; 
        String vmm = "Xen";
        double time_zone = 10.0; 
        double cost = 3.0; 
        double costPerMem = 0.05; 
        double costPerStorage = 0.001; 
        double costPerBw = 0.0;

        Storage storageDevice = new Storage(nodeName); // placeholder for storage
        LinkedList<Storage> storageList = new LinkedList<Storage>(); 
        storageList.add(storageDevice);

        FogDevice characteristics = null;
        try {
            characteristics = new FogDevice(nodeName, characteristics, 
                    new FogDeviceCharacteristics(arch, os, vmm, host, time_zone, cost, costPerMem, 
                            costPerStorage, costPerBw), 
                    new StreamOperatorScheduler(peList), hostList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        characteristics.setLevel(level);
        characteristics.setRatePerMips(ratePerMips);
        return characteristics;
    }
}