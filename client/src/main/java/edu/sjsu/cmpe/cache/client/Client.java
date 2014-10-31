package edu.sjsu.cmpe.cache.client;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cache Client...");
        
        CacheServiceInterface cache0 = new DistributedCacheService("http://localhost:3000");
        CacheServiceInterface cache1 = new DistributedCacheService("http://localhost:3001");
        CacheServiceInterface cache2 = new DistributedCacheService("http://localhost:3002");
        
        List<CacheServiceInterface> servers = new ArrayList(3);
        servers.add(cache0);
        servers.add(cache1);
        servers.add(cache2);
        
        
        ConsistentHash hash = new ConsistentHash(1, servers);
        
        for(Object server : servers) {
            hash.add(server);
        }

        char baseChar = 'a';
        
        // Create dictionary
        for(int i = 1; i < 11; i++) {
            // Get the server
            CacheServiceInterface server = (CacheServiceInterface)hash.get(Integer.toString(i));
            
            if (server == null) {
                System.out.println("server is NULL!!!!");
                return;
            }
            
            // Put key and value in server
            String value = Character.toString(++baseChar);
            System.out.println("value insert is " + value);
            server.put(i, value);
            
        }
        

        System.out.println("Existing Cache Client...");
    }

}
