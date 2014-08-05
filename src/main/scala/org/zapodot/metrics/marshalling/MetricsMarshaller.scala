package org.zapodot.metrics.marshalling

import java.util.concurrent.TimeUnit

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.json.MetricsModule
import com.fasterxml.jackson.databind.ObjectMapper

/**
 *
 * @author zapodot at gmail dot com
 */
class MetricsMarshaller {

  implicit val objectMapper = new ObjectMapper().registerModule(new MetricsModule(TimeUnit.SECONDS, TimeUnit.SECONDS, false))




}
