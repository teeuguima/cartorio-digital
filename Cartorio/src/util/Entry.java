package util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author matee
 */
public class Entry implements Serializable {

    private String key;
    private String value;
    Entry next;

    public Entry(String key, String value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Entry) {
            return (key == null && ((Entry) o).key == null)
                    || (key != null && key.equals(((Entry) o).getKey()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.key);
        hash = 79 * hash + Objects.hashCode(this.value);
        return hash;
    }
}
