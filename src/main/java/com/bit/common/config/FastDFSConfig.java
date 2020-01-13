package com.bit.common.config;

import com.bit.common.core.ConnectionPool;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.InetSocketAddress;

/**
 * @author lyj
 */
@Configuration
public class FastDFSConfig extends WebMvcConfigurerAdapter {

    static final String FASTDFS_CONFIG = "config/fdfs-client.conf";

    @Value("${connect_timeout}")
    private int connectTimeout;
    @Value("${network_timeout}")
    private int networkTimeout;
    @Value("${charset}")
    private String charset;
    @Value("${tracker_http_port}")
    private int tracker_http_port;
    @Value("${anti_steal_token}")
    private boolean antiStealToken;
    @Value("${secret_key}")
    private String secret_key;
    @Value("${tracker_server}")
    private String tracker_server;

    @Bean
    public ConnectionPool initStorageClient1(){
        ConnectionPool  pool=new ConnectionPool(10,30,200, connectTimeout, networkTimeout, charset, tracker_http_port, antiStealToken, secret_key, tracker_server);
        return  pool;
    }

   @Bean
    public StorageClient1 initStorageClient()
    {
        StorageClient1 storageClient = null;
        try
        {
           /* Resource resource = new ClassPathResource("config/fdfs-client.conf");
            ClientGlobal.init(FASTDFS_CONFIG);*/
            ClientGlobal.setG_connect_timeout(connectTimeout);
            ClientGlobal.setG_network_timeout(networkTimeout);
            ClientGlobal.setG_tracker_http_port(tracker_http_port);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_secret_key(secret_key);
            ClientGlobal.setG_anti_steal_token(false);
            InetSocketAddress[] tracker_servers = new InetSocketAddress[1];

            String[] parts = this.tracker_server.split("\\:", 2);
            tracker_servers[0]=new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));

            ClientGlobal.setG_tracker_group( new TrackerGroup(tracker_servers));

            /**/
            System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null)
            {
                throw new IllegalStateException("getConnection return null");
            }
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null)
            {
                throw new IllegalStateException("getStoreStorage return null");
            }
            storageClient = new StorageClient1(trackerServer, storageServer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return storageClient;
    }
}
