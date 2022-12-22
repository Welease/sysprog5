package models;


import java.util.ArrayList;

public class SymbolicName {
    private String addressName;
    private ArrayList<String> addressArrayList;
    private ArrayList<Integer> typeArrayList;

    public static final int STRAIGHT = 0;
    public static final int RELATIVE = 1;

    public SymbolicName(String addressName) {
        this.addressName = addressName;
        addressArrayList = new ArrayList<>();
        typeArrayList = new ArrayList<>();
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public void addAddressCounter(String addressCounter, int type) {
        addressArrayList.add(addressCounter);
        typeArrayList.add(type);
    }

    public ArrayList<String> getAddressArrayList() {return addressArrayList;}

    public ArrayList<Integer> getTypeArrayList() {return typeArrayList;}

    public void clearAddressCounter() {
        addressArrayList.clear();
    }
}
