

import java.util.Properties;

/**
 * @program: agileopskafka2hbase
 * @description: kafka配置类
 * @author: jiangyun
 * @create: 2018-06-06 10:43
 **/
public class KafkaConf {

    private static Properties conf;
    //public static final String[] topics= {"HomeLogIqj","AppLogIqj"};
    public static final String[] AppLogIqj= {"AppLogIqj"};
    public static final String[] HomeLogIqj= {"HomeLogIqj"};

    private KafkaConf() {
    }

    public static synchronized Properties getConf(){

        if (null!=conf){
            return conf;
        }else {
            conf = new Properties();
            conf.put("bootstrap.servers", "192.168.40.19:9090,192.168.40.22:9090");
            conf.put("group.id", "golden_compass_test");
            conf.put("enable.auto.commit", "false");
          // conf.put("auto.offset.reset", "latest");
            conf.put("auto.offset.reset", "earliest");
            conf.put("session.timeout.ms", "300000");
            conf.put("fetch.min.bytes", 1024*1024*2);
            //conf.put("socket.buffersize", 1024*1024);
           // conf.put("socket.timeout.ms", "60000");
            conf.put("heartbeat.interval.ms", 200000);
            conf.put("auto.commit.interval.ms", "30000");//consumer向zookeeper提交offset的频率，单位是秒
            conf.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            conf.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            return conf;
        }


}


}