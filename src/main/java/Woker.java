/**
 * @program: agile_ops_hbase2kafka
 * @description:
 * @author: jiangyun
 * @create: 2018-06-07 13:33
 **/
public class Woker {

    private int workers;

    public Woker(String[] topicName, String tableName, String columefamily,int workers){

        for (int i=0;i<workers;i++){
            ConsumerHandler appLogIqj = new ConsumerHandler(topicName, tableName, columefamily);
            new Thread(appLogIqj,tableName+"consumer"+i).start();
        }

    }
}