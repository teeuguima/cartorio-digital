package util;

/**
 *
 * @author matee
 */
public interface HashTable {

    public void put(Object key, Object value);

    public Object get(Object key);

    public void removeKey(Object key);

    public void removeValue(Object value, Object Key);

    public boolean isEmpty(int pos);

    public int size();
}
