



/**
 * @program: agileopskafka2hbase
 * @description: main方法
 * @author: jiangyun
 * @create: 2018-06-06 10:42
 **/
public class Processer {
    public static void main(String[] args) {



       new Woker(KafkaConf.AppLogIqj, "golden_compass:appLogIqj_test", "CF",5);
       new Woker(KafkaConf.HomeLogIqj, "golden_compass:homeLogIqj_test", "CF",5);


    }
    }
