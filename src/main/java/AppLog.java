
/**
 * @program: agileopskafka2hbase
 * @description:
 * @author: jiangyun
 * @create: 2018-06-06 17:53
 **/
public class AppLog {

    public String tableName;
    public String appName;
    public String sourceType;
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
    public String pos;
    public String pid;
    public String userId;
    public String pageId;
    public String us;
    public String appVersion;
    public String clientType;
    public String ip;
    public String operator;
    public String exitTime;
    public String deviceType;
    public String act;
    public String pno;
    public String actionType;
    public String actionNo;
    public String netType;
    public String actionTime;
    public String dateId;
    public String lab;
    public String val;
    public String lac;
    public String time;


    @Override
    public String toString() {
        return "AppLog{" +
                "tableName='" + tableName + '\'' +
                ", appName='" + appName + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", appServerIp='" + appServerIp + '\'' +
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
                ", pos='" + pos + '\'' +
                ", pid='" + pid + '\'' +
                ", userId='" + userId + '\'' +
                ", pageId='" + pageId + '\'' +
                ", us='" + us + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", clientType='" + clientType + '\'' +
                ", ip='" + ip + '\'' +
                ", operator='" + operator + '\'' +
                ", exitTime='" + exitTime + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", act='" + act + '\'' +
                ", pno='" + pno + '\'' +
                ", actionType='" + actionType + '\'' +
                ", actionNo='" + actionNo + '\'' +
                ", netType='" + netType + '\'' +
                ", actionTime='" + actionTime + '\'' +
                ", dateId='" + dateId + '\'' +
                ", lab='" + lab + '\'' +
                ", val='" + val + '\'' +
                ", lac='" + lac + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}