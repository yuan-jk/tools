package com.yuanjk.ignite.util

/**
 * @author yuanjk
 * @version 22/1/17
 */

import com.yuanjk.ignite.MemoryTablePropertyType

import java.util

case class SpatialDataInfo(spatialDataPropertiesDefinition: util.HashMap[String, MemoryTablePropertyType],
                           spatialDataValue: util.ArrayList[util.HashMap[String, Any]])
