package com.lianshang.rmq.demo;

import com.lianshang.common.utils.general.MapUtils;
import com.lianshang.common.utils.general.StringUtil;
import com.lianshang.rmq.common.exception.SerializationException;
import com.lianshang.rmq.common.serialize.AbstractSerializer;
import com.lianshang.rmq.common.serialize.hessian.HessianSerializer;
import com.lianshang.rmq.common.serialize.jackson.JacksonSerializer;
import org.apache.commons.cli.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by yuan.zhong on 2016-02-02.
 *
 * @author yuan.zhong
 */
public class SerializeTest {

    public static void main(String[] args) throws ParseException, SerializationException {
        Options options = new Options();

        options.addOption("l", "loop", true, "循环次数");
        options.addOption("s", "size", true, "内容字符串长度");

        int loop = 1;
        int contentSize = 1024;

        CommandLineParser commandLineParser = new PosixParser();
        CommandLine commandLine = commandLineParser.parse(options, args);

        if (commandLine.hasOption("l")) {
            loop = StringUtil.parseInt(commandLine.getOptionValue("l"));
        }

        if (commandLine.hasOption("s")) {
            contentSize = StringUtil.parseInt(commandLine.getOptionValue("s"));
        }

        new SerializeTest().run(loop, contentSize);
    }

    public void run(int loop, int contentSize) throws SerializationException {

        Map<String, AbstractSerializer> serializerMap = getSerializers();

        String content = strGenerator(contentSize);
        Random random = new Random();

        Map<String, List<SerializeStatis>> statisMap = new HashMap<>();

        for (Map.Entry<String, AbstractSerializer> serializerEntry : serializerMap.entrySet()) {
            TestBean testBean = getBean(content, random.nextInt());

            String serializerName = serializerEntry.getKey();
            AbstractSerializer serializer = serializerEntry.getValue();

            for (int i = 0; i < loop; i++) {
                long time1 = System.currentTimeMillis();
                byte[] bytes = serialize(testBean, serializer);
                long time2 = System.currentTimeMillis();
                int byteSize = bytes.length;
                TestBean after = deserialize(bytes, serializer, TestBean.class);
                long time3 = System.currentTimeMillis();
                assert testBean.equals(after);

                SerializeStatis statis = new SerializeStatis(time2 - time1, time3 - time2, byteSize);

                MapUtils.putChild(statisMap, serializerName, statis);
            }
        }

        System.out.println("Name\tSerialization\tDeserialization\tByteSize");
        for (Map.Entry<String, List<SerializeStatis>> statisEntry : statisMap.entrySet()) {
            String serializerName = statisEntry.getKey();

            long totalSerialzationTime = 0;
            long totalDeserializationTime = 0;
            int totalByteSize =  0;
            for (SerializeStatis statis : statisEntry.getValue()) {
                totalSerialzationTime += statis.getSerilizeTime();
                totalDeserializationTime += statis.getDeserializeTime();
                totalByteSize += statis.getByteSize();
            }

            System.out.println(String.format("%s\t%d\t%d\t%d", serializerName, totalSerialzationTime, totalDeserializationTime, totalByteSize));
        }
    }

    private Map<String, AbstractSerializer> getSerializers() {
        Map<String, AbstractSerializer> serializerMap = new HashMap<>();

        serializerMap.put("hessian", new HessianSerializer());
        serializerMap.put("jackson", new JacksonSerializer());

        return serializerMap;
    }

    private TestBean getBean(String content, int id) {

        Map<Integer, String> integerStringMap = new HashMap<>();
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            integerStringMap.put(i, content);
            stringList.add(content);
        }

        TestBean testBean = new TestBean(
                id,
                new BigInteger(String.valueOf(id)),
                new BigDecimal(Math.PI * id),
                integerStringMap,
                stringList,
                content
        );

        return testBean;
    }

    private byte[] serialize(Object obj, AbstractSerializer serializer) throws SerializationException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        serializer.serialize(os, obj);

        return os.toByteArray();
    }

    private <T> T deserialize(byte[] bytes, AbstractSerializer serializer, Class<T> clazz) throws SerializationException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);

        return serializer.deserialize(is, clazz);
    }

    public static class SerializeStatis {
        long serilizeTime;

        long deserializeTime;

        int byteSize;

        public SerializeStatis(long serilizeTime, long deserializeTime, int byteSize) {
            this.serilizeTime = serilizeTime;
            this.deserializeTime = deserializeTime;
            this.byteSize = byteSize;
        }

        public long getSerilizeTime() {
            return serilizeTime;
        }

        public void setSerilizeTime(long serilizeTime) {
            this.serilizeTime = serilizeTime;
        }

        public long getDeserializeTime() {
            return deserializeTime;
        }

        public void setDeserializeTime(long deserializeTime) {
            this.deserializeTime = deserializeTime;
        }

        public int getByteSize() {
            return byteSize;
        }

        public void setByteSize(int byteSize) {
            this.byteSize = byteSize;
        }
    }

    public static class TestBean implements Serializable {

        int id;

        BigInteger bigInteger;

        BigDecimal bigDecimal;

        Map<Integer,  String> integerStringMap;

        List<String> stringList;

        String string;

        public TestBean() {
        }

        public TestBean(int id, BigInteger bigInteger, BigDecimal bigDecimal, Map<Integer, String> integerStringMap, List<String> stringList, String string) {
            this.id = id;
            this.bigInteger = bigInteger;
            this.bigDecimal = bigDecimal;
            this.integerStringMap = integerStringMap;
            this.stringList = stringList;
            this.string = string;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (!(o instanceof TestBean)) return false;

            TestBean testBean = (TestBean) o;

            if (id != testBean.id) return false;
            if (bigDecimal != null ? !bigDecimal.equals(testBean.bigDecimal) : testBean.bigDecimal != null)
                return false;
            if (bigInteger != null ? !bigInteger.equals(testBean.bigInteger) : testBean.bigInteger != null)
                return false;
            if (!equalMap(integerStringMap, testBean.integerStringMap))
                return false;
            if (string != null ? !string.equals(testBean.string) : testBean.string != null) return false;
            if (!equalList(stringList, testBean.stringList))
                return false;

            return true;
        }

        private boolean equalMap(Map<Integer, String> map1, Map<Integer, String> map2) {
            if (map1 == null) {
                return map2 == null;
            }

            if (map2 == null) {
                return false;
            }

            if (map1.size() != map2.size()) {
                return false;
            }

            for (Map.Entry<Integer, String> entry1 : map1.entrySet()) {
                Integer key = entry1.getKey();
                String value1 = entry1.getValue();

                String value2 = map2.get(key);

                if (!equalBean(value1, value2)) {
                    return false;
                }
            }

            return true;
        }

        private boolean equalList(List<String> list1, List<String> list2) {
            if (list1 == null) {
                return list2 == null;
            }

            if (list2 == null) {
                return false;
            }

            if (list1.size() != list2.size()) {
                return false;
            }

            for (int i = 0; i < list1.size(); i++) {
                String value1 = list1.get(i);
                String value2 = list2.get(i);

                if (!equalBean(value1, value2)) {
                    return false;
                }
            }

            return true;
        }

        private boolean equalBean(Object obj1, Object obj2) {
            if (obj1 == null) {
                return obj2 == null;
            }

            if (obj2 == null) {
                return false;
            }

            return obj1.equals(obj2);
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (bigInteger != null ? bigInteger.hashCode() : 0);
            result = 31 * result + (bigDecimal != null ? bigDecimal.hashCode() : 0);
            result = 31 * result + (integerStringMap != null ? integerStringMap.hashCode() : 0);
            result = 31 * result + (stringList != null ? stringList.hashCode() : 0);
            result = 31 * result + (string != null ? string.hashCode() : 0);
            return result;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public BigInteger getBigInteger() {
            return bigInteger;
        }

        public void setBigInteger(BigInteger bigInteger) {
            this.bigInteger = bigInteger;
        }

        public BigDecimal getBigDecimal() {
            return bigDecimal;
        }

        public void setBigDecimal(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        public Map<Integer, String> getIntegerStringMap() {
            return integerStringMap;
        }

        public void setIntegerStringMap(Map<Integer, String> integerStringMap) {
            this.integerStringMap = integerStringMap;
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }

    private String strGenerator(int contentSize) {
        StringBuilder stringBuilder = new StringBuilder();

        char base = 'A';
        Random random = new Random();
        for (int i = 0; i < contentSize; i++) {
            stringBuilder.append((char)(base + random.nextInt(26)));
        }

        return stringBuilder.toString();
    }
}
