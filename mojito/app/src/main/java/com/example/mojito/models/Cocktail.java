package com.example.mojito.models;

import java.util.ArrayList;

public class Cocktail {

    private int mId;
    private String mName;
    private String mInstructions;
    private String mImage;
    private String mGlass;
    private ArrayList<String> mIngredients;
    private ArrayList<String> mAmounts;

    public Cocktail(int mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmInstructions() {
        return mInstructions;
    }

    public void setmInstructions(String mInstructions) {
        this.mInstructions = mInstructions;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmGlass() {
        return mGlass;
    }

    public void setmGlass(String mGlass) {
        this.mGlass = mGlass;
    }

    public ArrayList<String> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(ArrayList<String> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public ArrayList<String> getmAmounts() {
        return mAmounts;
    }

    public void setmAmounts(ArrayList<String> mAmounts) {
        this.mAmounts = mAmounts;
    }
}
