package com.echovue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Date;

public class ESMetrics {

    @JsonProperty("StartDateTime")
    private Date startDate;
    @JsonProperty("EndDateTime")
    private Date endDate;
    @JsonProperty("CPUUtilization")
    private Double cpuUtilization;
    @JsonProperty("CacheHits")
    private Double cacheHits;
    @JsonProperty("CacheMisses")
    private Double cacheMisses;
    @JsonProperty("CurrItems")
    private Double currItems;
    @JsonProperty("CurrConnections")
    private Double currConnections;
    @JsonProperty("Reclaimed")
    private Double reclaimed;
    @JsonProperty("ReplicationBytes")
    private Double replicationBytes;
    @JsonProperty("Evictions")
    private Double evictions;
    @JsonProperty("SwapUsage")
    private Double swapUsage;
    @JsonProperty("NetworkBytesOut")
    private Double networkBytesOut;
    @JsonProperty("NetworkBytesIn")
    private Double networkBytesIn;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(Double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    public Double getCacheHits() {
        return cacheHits;
    }

    public void setCacheHits(Double cacheHits) {
        this.cacheHits = cacheHits;
    }

    public Double getCacheMisses() {
        return cacheMisses;
    }

    public void setCacheMisses(Double cacheMisses) {
        this.cacheMisses = cacheMisses;
    }

    public Double getCurrItems() {
        return currItems;
    }

    public void setCurrItems(Double currItems) {
        this.currItems = currItems;
    }

    public Double getCurrConnections() {
        return currConnections;
    }

    public void setCurrConnections(Double currConnections) {
        this.currConnections = currConnections;
    }

    public Double getReclaimed() {
        return reclaimed;
    }

    public void setReclaimed(Double reclaimed) {
        this.reclaimed = reclaimed;
    }

    public Double getReplicationBytes() {
        return replicationBytes;
    }

    public void setReplicationBytes(Double replicationBytes) {
        this.replicationBytes = replicationBytes;
    }

    public Double getEvictions() {
        return evictions;
    }

    public void setEvictions(Double evictions) {
        this.evictions = evictions;
    }

    public Double getSwapUsage() {
        return swapUsage;
    }

    public void setSwapUsage(Double swapUsage) {
        this.swapUsage = swapUsage;
    }

    public Double getNetworkBytesOut() {
        return networkBytesOut;
    }

    public void setNetworkBytesOut(Double networkBytesOut) {
        this.networkBytesOut = networkBytesOut;
    }

    public Double getNetworkBytesIn() {
        return networkBytesIn;
    }

    public void setNetworkBytesIn(Double networkBytesIn) {
        this.networkBytesIn = networkBytesIn;
    }
}