package com.unal.micasaya.ui.Home;

import android.os.Parcel;
import android.os.Parcelable;

public class MaterialsActivity implements Parcelable {
    private String name;
    private String type;
    private double quantity;
    private String unit;
    private double estimatedCostPerUnit;
    private String notes;

    public MaterialsActivity() {

    }

    public MaterialsActivity(String name, String type, double quantity, String unit, double estimatedCostPerUnit, String notes) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.estimatedCostPerUnit = estimatedCostPerUnit;
        this.notes = notes;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getEstimatedCostPerUnit() {
        return estimatedCostPerUnit;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setEstimatedCostPerUnit(double estimatedCostPerUnit) {
        this.estimatedCostPerUnit = estimatedCostPerUnit;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    protected MaterialsActivity(Parcel in) {
        name = in.readString();
        type = in.readString();
        quantity = in.readDouble();
        unit = in.readString();
        estimatedCostPerUnit = in.readDouble();
        notes = in.readString();
    }

    public static final Creator<MaterialsActivity> CREATOR = new Creator<MaterialsActivity>() {
        @Override
        public MaterialsActivity createFromParcel(Parcel in) {
            return new MaterialsActivity(in);
        }

        @Override
        public MaterialsActivity[] newArray(int size) {
            return new MaterialsActivity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeDouble(quantity);
        dest.writeString(unit);
        dest.writeDouble(estimatedCostPerUnit);
        dest.writeString(notes);
    }
}
