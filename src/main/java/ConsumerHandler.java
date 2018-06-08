import com.alibaba.fastjson.JSON;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: agileopskafka2hbase
 * @description: 处理逻辑
 * @author: jiangyun
 * @create: 2018-06-06 11:47
 **/
public class ConsumerHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ConsumerHandler.class);
    private static  Connection conn;
    private    KafkaConsumer<String, String> consumer;
    private static String tableName;
    private static Table table;
    private String columefamily;
    private static Configuration conf=null;
    private static AggregationClient ac;


    static {
        System.setProperty("HADOOP_USER_NAME", "hbase");
        conf = HBaseConfiguration.create();
        conf.set("fs.defaultFS", "hdfs://HadoopCluster");
        conf.set("dfs.nameservices", "HadoopCluster");
        conf.set("dfs.ha.namenodes.HadoopCluster", "namenode1,namenode2");
        conf.set("dfs.namenode.rpc-address.HadoopCluster.namenode1", "hadoop-001.qianjin.com:8020");
        conf.set("dfs.namenode.rpc-address.HadoopCluster.namenode2", "hadoop-002.qianjin.com:8020");
        conf.set("dfs.client.failover.proxy.provider.HadoopCluster", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        // conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.setInt("dfs.datanode.socket.write.timeout", 6000000);
        conf.setInt("hbase.rpc.timeout",200000);
        conf.setInt("hbase.client.operation.timeout",300000);
        conf.setInt("hbase.client.scanner.timeout.period",2000000);
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "192.168.32.43,192.168.32.42,192.168.32.44");
        ac = new AggregationClient(conf);
        try {
            conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   public ConsumerHandler(String[] topicName, String tableName, String columefamily){
      consumer = new KafkaConsumer<String, String>(KafkaConf.getConf());
      consumer.subscribe(Arrays.asList(topicName));
      this.tableName=tableName;
      this.columefamily=columefamily;
      table= getTable(tableName);
   }



   @Override
   public void run() {

       while (true){

           ConsumerRecords<String, String> records = consumer.poll(10000);
           List<Put> puts = new ArrayList<Put>(100);
           List<Delete> deletes = new ArrayList<Delete>(100);
           for (ConsumerRecord<String,String> record:records){



               if ("golden_compass:appLogIqj_test"==tableName){
                   AppLog appLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), AppLog.class);
                    if (doCheckA(appLog)){
                        Put put = null;
                        try {
                            put = objectToPut(record, columefamily);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        puts.add(put);
                    }else {

                        Delete delete = new Delete(Bytes.toBytes(appLog.pid));
                        deletes.add(delete);
                    }

               }
               else if ("golden_compass:homeLogIqj_test"==tableName){
                   HomeLog homeLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), HomeLog.class);
                   if (doCheckB(homeLog)){
                       Put put = null;
                       try {
                           put = objectToPut(record, columefamily);
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (NoSuchFieldException e) {
                           e.printStackTrace();
                       }

                       puts.add(put);
                   }else {
                       if (null==homeLog.userId||""==homeLog.userId){
                           homeLog.userId="0000000000";
                       }
                       Delete delete = new Delete(Bytes.toBytes(homeLog.userId));
                       deletes.add(delete);
                   }

               }




           }

           try {
             //  table.put(puts);
               put(tableName,puts);


           } catch (IOException e) {
               e.printStackTrace();
               try {
                   logger.error("-----------------hbase第一次插入数据失败 开始第二次插入-----------------------");
                   put(tableName,puts);
               } catch (IOException e1) {
                   try {
                       logger.error("-----------------hbase第二次插入数据失败 开始第三次插入-----------------------");
                       put(tableName,puts);
                   } catch (IOException e2) {
                       logger.error("-----------------hbase第三次插入数据失败 数据将丢失-----------------------");
                       logger.error("-----------------hbase第三次插入数据失败 数据将丢失-----------------------");
                       logger.error("-----------------hbase第三次插入数据失败 数据丢失-----------------------");
                       e2.printStackTrace();
                   }

               }
           }

           try {
               table.delete(deletes);
           } catch (IOException e) {
               e.printStackTrace();
               try {
                   logger.error("-----------------hbase第一次删除数据失败 开始第二次删除-----------------------");
                   table.delete(deletes);
               } catch (IOException e1) {
                   e1.printStackTrace();
                   try {
                       logger.error("-----------------hbase第二次删除数据失败 开始第三次删除-----------------------");
                       table.delete(deletes);
                   } catch (IOException e2) {
                       e2.printStackTrace();
                       logger.error("-----------------hbase第三次删除数据失败----------------------");
                   }
               }
           }

           consumer.commitSync();

       }


   }


   public AppLog jsonToAppLog(ConsumerRecord<String,String> record){

       AppLog appLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), AppLog.class);
        return appLog;
   }


    public HomeLog jsonToHomeLog(ConsumerRecord<String,String> record){
        HomeLog homeLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), HomeLog.class);
        return homeLog;
    }


    //检查删除还是插入
private boolean doCheckA(AppLog appLog){

        if (appLog.pid.hashCode()%2==1){

            return true;
        }else {
            return false;
        }



}

    //检查删除还是插入
    private boolean doCheckB(HomeLog homeLog){

        if (null!=homeLog.uid&&homeLog.uid.hashCode()%2==1){
            return true;
        }
        else {

            return false;
        }


}

    public  Put objectToPut(Object object, String cf) throws IllegalAccessException, NoSuchFieldException {

        ConsumerRecord record = (ConsumerRecord)object;


        if ("golden_compass:appLogIqj_test"==tableName){
            AppLog appLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), AppLog.class);
            String pk = appLog.pid;
            Put put = new Put(Bytes.toBytes( pk));
            for (Field field : appLog.getClass().getDeclaredFields()) {
                field.setAccessible(true);


                if (null!=field.getName()&&null!=field.get(appLog)&&""!=field.get(appLog)){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field.getName().toUpperCase()), Bytes.toBytes(field.get(appLog).toString()));
                }
                else if (null!=field.getName()){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field.getName().toUpperCase()), Bytes.toBytes("null"));
                }
                else if (null==field.getName()&&null!=field.get(appLog)&&""!=field.get(appLog)){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("NULLKEY"),  Bytes.toBytes(field.get(appLog).toString()));
                }


            }
            return put;
        }else if ("golden_compass:homeLogIqj_test"==tableName){
            HomeLog homeLog = JSON.parseObject(record.key().toString().trim().substring(0, record.key().toString().trim().length() - 1) + "," + record.value().toString().trim().substring(1, record.value().toString().trim().length()), HomeLog.class);



                homeLog.getClass().getDeclaredField("userId");
                if (null==homeLog.userId||""==homeLog.userId){
                    homeLog.userId="00000000";
                }



                String pk = homeLog.userId;
            Put  put = new Put(Bytes.toBytes( pk));

            for (Field field : homeLog.getClass().getDeclaredFields()) {
                field.setAccessible(true);


                if (null!=field.getName()&&null!=field.get(homeLog)&&""!=field.get(homeLog)){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field.getName().toUpperCase()), Bytes.toBytes(field.get(homeLog).toString()));
                }
                else if (null!=field.getName()){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(field.getName().toUpperCase()), Bytes.toBytes("null"));
                }
                else if (null==field.getName()&&null!=field.get(homeLog)&&""!=field.get(homeLog)){
                    put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("NULLKEY"),  Bytes.toBytes(field.get(homeLog).toString()));
                }


            }
            return put;



        }



return null;


    }



    //hbase 建立连接
    public static synchronized Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionFactory.createConnection(conf);
            }
        } catch (IOException e) {
            logger.error("HBase 建立链接失败 ", e);
        }
        return conn;

    }


    //批量写数据
    public  void put(String tablename, List<Put> puts) throws IOException {
        Connection conn = getConnection();
        final BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
            public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
                for (int i = 0; i < e.getNumExceptions(); i++) {
                    logger.error("Failed to sent put " + e.getRow(i) + ".");
                }
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf(tablename))
                .listener(listener);
        params.writeBufferSize(5 * 1024 * 1024);

        final BufferedMutator mutator = conn.getBufferedMutator(params);
        try {
            mutator.mutate(puts);
            mutator.flush();
        } finally {
            mutator.close();
        }

    }

    //获取表
    public static Table getTable(String tableName) {
        try {
            return getConnection().getTable(TableName.valueOf(tableName));
        } catch (Exception e) {
            logger.error("Obtain Table failure !", e);
        }
        return null;
    }

    public boolean check(ConsumerRecord<String, String> record){



        return true;
    }

}