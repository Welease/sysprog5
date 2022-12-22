package tables;

import java.util.ArrayList;

public class ModificationTableSingleton {
    private static ModificationTableSingleton instance;
    private ArrayList<String> modificationList;

    private ModificationTableSingleton() {
        modificationList = new ArrayList<>();
    }

    public static ModificationTableSingleton getInstance() {
        if(instance == null){
            instance = new ModificationTableSingleton();
        }
        return instance;
    }

    public void addModification(String address) {
        address = "0".repeat(Math.max(0, 6 - address.length())) + address;
        modificationList.add(address);
    }

    public ArrayList<String> getModificationList() {
        return modificationList;
    }

    public static void clear() {
        instance = null;
    }
}
