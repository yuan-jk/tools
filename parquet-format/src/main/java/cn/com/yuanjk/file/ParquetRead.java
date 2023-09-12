package cn.com.yuanjk.file;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.metadata.ColumnChunkMetaData;
import java.io.IOException;
import java.util.List;
import org.apache.parquet.hadoop.ParquetReader;

public class ParquetRead {

    public static void main(String[] args) {
        try {
            // 指定 Parquet 文件路径
            Path parquetFilePath = new Path("D:\\data\\spatial_data\\美国\\纽约出租车\\DATA\\green_tripdata_2023-05.parquet");
            //查询总列数，参考博客https://blog.csdn.net/letterss/article/details/131189471?spm=1001.2014.3001.5501
//            int allColumnsCount = getParquetAllColumnsCount(parquetFilePath);
            int columnIndexMax = 16;
//            columnIndexMax = allColumnsCount - 1;
            // 创建 ParquetReader.Builder 实例
            ParquetReader.Builder<Group> builder = ParquetReader.builder(new GroupReadSupport(), parquetFilePath);
            // 创建 ParquetReader 实例
            ParquetReader<Group> reader = builder.build();
            // 循环读取 Parquet 文件中的记录
            Group record;

            while ((record = reader.read()) != null) {
                // 处理每个记录的逻辑
                for (int i = 0; i <= columnIndexMax; i++) {
                    Object field = record.getValueToString(i, 0);
                    System.out.println(field);
                }
            }
            // 关闭读取器
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}