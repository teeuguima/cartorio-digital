package util;

import java.io.Serializable;

public class Hash implements Serializable {

    private static int INITIAL_SIZE = 16;
    private Entry[] entries = new Entry[INITIAL_SIZE];
    
    public Hash(){
        
    }

    public boolean put(String key, String value) {
        int hash = getHash(key);
        final Entry hashEntry = new Entry(key, value);
        if (entries[hash] == null) {
            entries[hash] = hashEntry;
            return true;
        } // If there is already an entry at current hash
        // position, add entry to the linked list.
        else if (entries[INITIAL_SIZE] == null) {
            Entry temp = entries[hash];
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = hashEntry;
            return true;
        } else {
            redimensionarArray();
            Entry temp = entries[hash];
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = hashEntry;
            return true;
        }
    }

    public void redimensionarArray() {
        Entry[] novoArray = new Entry[INITIAL_SIZE * 2];
        System.arraycopy(entries, 0, novoArray, 1, entries.length);
        this.entries = novoArray;
    }
    
    public boolean isEmpty(){
        return entries[0] != null;
    }

    /**
     * Returns 'null' if the element is not found.
     *
     * @param key
     * @return
     */
    public String get(String key) {
        int hash = getHash(key);
        if (entries[hash] != null) {
            Entry temp = entries[hash];

            // Check the entry linked list for march
            // for the given 'key'
            while (!temp.getKey().equals(key)
                    && temp.next != null) {
                temp = temp.next;
            }
            return temp.getValue();
        }

        return null;
    }

    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return Math.abs(key.hashCode()) % INITIAL_SIZE;
    }
    
    public int size(){
        return entries.length;
    }

}
