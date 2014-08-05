package org.zapodot.metrics.actor

import java.util.concurrent.ExecutorService

import akka.actor.{Actor, ActorLogging}
import com.codahale.metrics.health.{HealthCheck, HealthCheckRegistry}
import com.codahale.metrics.json.HealthCheckModule
import com.fasterxml.jackson.databind.ObjectMapper
import spray.can.Http
import spray.http.CacheDirectives._
import spray.http.ContentTypes._
import spray.http.HttpHeaders._
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.http._

import scala.collection.JavaConverters._
import scala.util.Left


/**
 *
 * @author zapodot at gmail dot com
 */
class HealthActor(healthRegistry: HealthCheckRegistry, path: String = "/health", executorService: Option[ExecutorService] = None) extends Actor with ActorLogging {

  val mapper = new ObjectMapper().registerModule(new HealthCheckModule())

  override def receive: Receive = {
    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/health"), _, _, _) =>
      sender ! runHealthChecksAndTransform

    case _: HttpRequest => sender ! HttpResponse(status = NotFound, entity = "Unknown resource")

    case Timedout(HttpRequest(method, uri, _, _, _)) =>
      sender ! HttpResponse(
        status = InternalServerError,
        entity = "The " + method + " request to '" + uri + "' has timed out..."
      )

  }

  import HttpResponses._

  def runHealthChecksAndTransform: HttpResponse = {
    val results = runHealthChecks
    if(results.isEmpty)
      HttpResponses.notImplementedResponse
    else
      calculateStatus(results.asScala.toMap) match {
        case InternalServerError => errorResponse(mapper.writeValueAsString(results))
        case _ => okResponse(mapper.writeValueAsString(results))
      }
  }

  def calculateStatus(results: Map[String, HealthCheck.Result]) =
    if (results.isEmpty)
      NotImplemented
    else {
      results.find(k => ! k._2.isHealthy()).map(r => InternalServerError).getOrElse(OK)
    }

  def runHealthChecks = executorService.map(e => healthRegistry.runHealthChecks(e)).getOrElse(healthRegistry.runHealthChecks())
}
