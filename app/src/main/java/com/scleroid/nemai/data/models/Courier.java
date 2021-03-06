package com.scleroid.nemai.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.scleroid.nemai.utils.RandomSerialNumberGen;

/**
 * @author Ganesh Kaple
 * @since 09-01-2018
 */
@Entity
public class Courier {
	@SerializedName("")
	@PrimaryKey
	private long serialNo;
    private String courierName;
    private double price;
    private String serviceType;
    private String deliveryTime;
    private String courierImageUrl;
    private String trackingID;
    private String referenceNo;

    public Courier(String courierName, double price, String serviceType, String deliveryTime, String courierImageUrl, String trackingID, String referenceNo) {
        this.trackingID = trackingID;

        this.referenceNo = referenceNo;
        this.serialNo = RandomSerialNumberGen.getRandomSerialNo();
        this.courierName = courierName;
        this.price = price;
        this.serviceType = serviceType;
        this.deliveryTime = deliveryTime;
        this.courierImageUrl = courierImageUrl;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCourierImageUrl() {
        return courierImageUrl;
    }

    public void setCourierImageUrl(String courierImageUrl) {
        this.courierImageUrl = courierImageUrl;
    }
}
