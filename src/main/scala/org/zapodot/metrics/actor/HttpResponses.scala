package org.zapodot.metrics.actor

import spray.http.CacheDirectives._
import spray.http.ContentTypes._
import spray.http.HttpHeaders._
import spray.http.{ContentType, StatusCode, HttpEntity, HttpResponse}
import spray.http.StatusCodes._

/**
 *
 * @author sondre
 */
object HttpResponses {

  val notFoundResponse = HttpResponse(status = NotFound, entity = "Unknown resource")
  val notImplementedResponse = HttpResponse(NotImplemented, HttpEntity(`application/json`, "No HealthChecks is defined"))

  def response(status: StatusCode, contentType: ContentType, body: String) =
    HttpResponse(status, HttpEntity(`application/json`, body), List(`Cache-Control`(`no-cache`, `must-revalidate`, `no-store`)))

  def errorResponse(content: String) = response(InternalServerError, `application/json`, content)

  def okResponse(content: String) = response(OK, `application/json`, content)
}
