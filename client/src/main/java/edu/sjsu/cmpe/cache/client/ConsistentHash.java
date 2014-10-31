package edu.sjsu.cmpe.cache.client;


import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.security.*;
import java.math.*;
import java.lang.*;


public class ConsistentHash<T> {
    private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Integer, T> circle =
    new TreeMap<Integer, T>();
    
    public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
        
        this.hashFunction = new HashFunction();
        this.numberOfReplicas = numberOfReplicas;
        
        for (T node : nodes) {
            add(node);
        }
    }
    
    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(hashFunction.hash(node.toString() + i), node);
        }
    }
    
    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hash(node.toString() + i));
        }
    }
    
    // This returns the node
    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }
        int hash = hashFunction.hash((String)key);
        if (!circle.containsKey(hash)) {
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
    
    
    static public class HashFunction<T> {
        
        
        public Integer hash(String string) {
            BigInteger bigInt = new BigInteger("0");
            
            try{
                byte[] bytesOfMessage = string.getBytes("UTF-8");
                
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.reset();
                md.update(bytesOfMessage);
                byte[] thedigest = md.digest();
                
                bigInt = new BigInteger(thedigest);
            } catch (Exception e) {
                System.err.println(e);
            }
            

            return bigInt.intValue();
            
        }
    }
    
}

