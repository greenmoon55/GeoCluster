/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * markerclusterer is an api from google-map
 * @author AdamGu0
 */
public class SplitSmartSwapClusterer {

    private final MapKit mapKit;
    private Point[] data;
    private ArrayList<Cluster> clusters;
    public Cluster[] clustersArray;
    public long duration;
    public boolean startCluster = false;
    private double[] minDistance;
    private ArrayList<Cluster[]> allClusters;
    private double[] dnear = new double[50];



    public SplitSmartSwapClusterer(MapKit mapKit) {
        this.mapKit = mapKit;
        this.minDistance = getDistance();
    }

    public int startSplitSmartSwap(Point[] data, int zoom) {
        if (data == null) {
            return 1;
        } else if (data.length == 0) {
            return 2;
        }
        this.startCluster = true;
        this.data = data.clone();
        this.allClusters = new ArrayList<Cluster[]>();
        
        long start = System.currentTimeMillis();
        this.clusters = new ArrayList<Cluster>();
        ClusterData cd = new ClusterData(data, true);
        Cluster[] clusters = null;
        allClusters.add(null);
        allClusters.add(null);
        for (int i = 2; i < 45; i++) {
            clusters = cd.smartSwap(i);
            dnear[i] = cd.getNearestDist();
            allClusters.add(clusters);
        }
        System.out.println(Arrays.toString(dnear));
        duration = System.currentTimeMillis() - start;
        doSplitSmartSwap(zoom);
        return 0;
    }

    public boolean doSplitSmartSwap(int zoom) {
        if (!startCluster) return startCluster;
        zoom = 17 - zoom;
        
        int i;
        for (i = 2; i < 40; i++) {
            System.out.println("dnear: "  + dnear[i] + " mindis:" + minDistance[zoom]);

            if (dnear[i] < minDistance[zoom]) {
                System.out.println(' '  + dnear[i] + ' ' + minDistance[zoom]);
                break;
            }
        }
        
        System.out.println("do split smart swap " + i + ' ' + zoom);
        Cluster[] clusters = allClusters.get(i);

        mapKit.setWaypoints(clusters);
        mapKit.repaint();
        this.clustersArray = clusters;
        return startCluster;
    }
    
    private double[] getDistance() {
        double[] distances = new double[50];
        distances[0] = 2500;
        for (int i = 1; i < distances.length; i++) {
            distances[i] = distances[i - 1] / 2;
        }
        System.out.println(Arrays.toString(distances));
        return distances;
    }
    
}
