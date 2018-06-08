
/**
 * @program: agileopskafka2hbase
 * @description:
 * @author: jiangyun
 * @create: 2018-06-06 23:05
 **/
public class HomeLog {

    public String appServerIp;
    public String appServerPort;
    public String syncTaskId;
    public String syncTaskStartTime;
    public String dbName;
    public String msgSyncStartTime;
    public String msgSyncUsedTime;
    public String msgSequence;
    public String msgSize;
    public String kafkaTopic;
    public String kafkaPartition;
    public String kafkaOffset;
    public String tableName;
    public String appName;
    public String sourceType;
    public String url;
    public String userId;
    public String clientType;
    public String us;
    public String uid;
    public String operator;
    public String dateId;
    public String netType;
    public String actLab;
    public String deviceNum;
    public String deviceNumType;
    public String act;
    public String fullUrl;
    public String actDesc;
    public String actionTime;
    public String browser;



    @Override
    public String toString() {
        return "HomeLog{" +
                "appServerIp='" + appServerIp + '\'' +
                ", appServerPort='" + appServerPort + '\'' +
                ", syncTaskId='" + syncTaskId + '\'' +
                ", syncTaskStartTime='" + syncTaskStartTime + '\'' +
                ", dbName='" + dbName + '\'' +
                ", msgSyncStartTime='" + msgSyncStartTime + '\'' +
                ", msgSyncUsedTime='" + msgSyncUsedTime + '\'' +
                ", msgSequence='" + msgSequence + '\'' +
                ", msgSize='" + msgSize + '\'' +
                ", kafkaTopic='" + kafkaTopic + '\'' +
                ", kafkaPartition='" + kafkaPartition + '\'' +
                ", kafkaOffset='" + kafkaOffset + '\'' +
                ", tableName='" + tableName + '\'' +
                ", appName='" + appName + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", clientType='" + clientType + '\'' +
                ", us='" + us + '\'' +
                ", uid='" + uid + '\'' +
                ", operator='" + operator + '\'' +
                ", dateId='" + dateId + '\'' +
                ", netType='" + netType + '\'' +
                ", actLab='" + actLab + '\'' +
                ", deviceNum='" + deviceNum + '\'' +
                ", deviceNumType='" + deviceNumType + '\'' +
                ", act='" + act + '\'' +
                ", fullUrl='" + fullUrl + '\'' +
                ", actDesc='" + actDesc + '\'' +
                ", actionTime='" + actionTime + '\'' +
                ", browser='" + browser + '\'' +
                '}';
    }
}