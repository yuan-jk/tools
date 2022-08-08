package com.yuanjk.spark

import org.apache.spark.sql.SparkSession

/**
 * @author yuanjk
 * @version 22/1/24
 */
object ConnectToRemote {

/*  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("spark remote test").master("spark://10.0.168.19:7077").getOrCreate()
    val df1 = spark.range(1, 100, 1)
    val step1 = df1.repartition(6)
    val step2 = step1.selectExpr("sum(id)")
    println("total count: \n" + step2.collectAsList().get(0))

  }*/


}
