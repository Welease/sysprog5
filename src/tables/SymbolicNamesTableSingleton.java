package tables;

import exceptions.UnknownCommandException;
import models.RecordBody;
import models.SymbolicName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SymbolicNamesTableSingleton {
    private static SymbolicNamesTableSingleton instance;
    private HashMap<String, SymbolicName> symbolicNames;

    private SymbolicNamesTableSingleton() {
        symbolicNames = new HashMap<>();
    }

    public static SymbolicNamesTableSingleton getInstance(){
        if(instance == null){
            instance = new SymbolicNamesTableSingleton();
        }
        return instance;
    }

    public void addNewName(String name, String address, String nextAddress) throws UnknownCommandException {
        address = address.toUpperCase();
        if (address.length() != 6){
            address = "0".repeat(Math.max(0, 6 - address.length())) + address;
        }

        if (symbolicNames.containsKey(name)) {
            SymbolicName symbolicName = symbolicNames.get(name);
            if (!"".equals(symbolicName.getAddressName()))
                throw new UnknownCommandException("Повторение метки.");

            symbolicName.setAddressName(address);

            //Ищем в объектном модуле вхождения адресов и меняем
            RecordingTableSingleton record = RecordingTableSingleton.getInstance();
            HashMap<String , RecordBody> hashMap = record.getRecordingTable();
            ArrayList<String> addressArrayList = symbolicName.getAddressArrayList();
            ArrayList<Integer> typeArrayList = symbolicName.getTypeArrayList();

            //Для поиска следующего адреса
            ArrayList<String> arrayList = new ArrayList<>();
            for (String elem : hashMap.keySet()) {
                arrayList.add(elem);
            }
            Collections.sort(arrayList);
            //
            for (int i = 0; i < addressArrayList.size(); i++) {
                RecordBody body = hashMap.get("T" + addressArrayList.get(i));

                if (typeArrayList.get(i) == 0) {
                    String str = body.getBody().substring(0, body.getBody().indexOf("#"));
                    hashMap.put("T" + addressArrayList.get(i), new RecordBody(str + address));
                } else {
                    String str = body.getBody().substring(0, body.getBody().indexOf("&"));
                    String next;
                    try{
                        next = arrayList.get(arrayList.indexOf("T" + addressArrayList.get(i)) + 1);
                    } catch (Exception ex) {
                        next = nextAddress;
                    }
                    int val = Integer.parseInt(address, 16) - Integer.parseInt(next.substring(1), 16);
                    String numHex = Integer.toHexString(val);
                    if (numHex.length() < 6) {
                        numHex = "0".repeat(Math.max(0, 6 - numHex.length())) + numHex;
                    } else if (numHex.length() > 6) {
                        numHex = numHex.substring(2);
                    }
                    hashMap.put("T" + addressArrayList.get(i), new RecordBody(str + numHex));
                }
            }

            symbolicName.clearAddressCounter();
        }
        else {
            symbolicNames.put(name, new SymbolicName(address));
        }
    }

    public void addAddressToName(String name, String address, int typeOfAddressation) throws UnknownCommandException {
        address = address.toUpperCase();
        if (address.length() != 6){
            address = "0".repeat(Math.max(0, 6 - address.length())) + address;
        }

        if (symbolicNames.containsKey(name)) {
            SymbolicName symbolicName = symbolicNames.get(name);
            if (!"".equals(symbolicName.getAddressName()))
                return;

            symbolicName.addAddressCounter(address, typeOfAddressation);
        }
        else {
            SymbolicName symbolicName = new SymbolicName("");
            symbolicName.addAddressCounter(address, typeOfAddressation);
            symbolicNames.put(name, symbolicName);
        }
    }

    public HashMap<String , SymbolicName> getSymbolicNames() {
        return symbolicNames;
    }

    public static void clear() {
        instance = null;
    }
}
