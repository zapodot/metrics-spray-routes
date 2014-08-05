package org.zapodot.metrics.actor

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.codahale.metrics.health.HealthCheck.Result
import com.codahale.metrics.health.{HealthCheck, HealthCheckRegistry}
import com.typesafe.config.ConfigFactory
import org.scalatest._
import spray.http._


/**
 *
 * @author sondre
 */
class HealthActorTest(_system: ActorSystem) extends TestKit(_system) with FlatSpecLike with Matchers with ImplicitSender with BeforeAndAfter with BeforeAndAfterAll {

  def this() = this(ActorSystem(classOf[HealthActorTest].getSimpleName, ConfigFactory.parseString("akka.loggers = [akka.testkit.TestEventListener]")))

  "The Health actor " should " return status code NOT IMPLEMENTED when no health checks are registered" in {
    val reg = new HealthCheckRegistry()
    val healthActor = createActor("healthCheckActorNotImplemented", reg)
    healthActor ! HttpRequest(HttpMethods.GET, Uri("/health"))
    expectMsg(HttpResponses.notImplementedResponse)
    healthActor ! PoisonPill
  }
  it should " return NOT_FOUND when a different resource is requested " in {
    val reg = new HealthCheckRegistry()
    val actor = createActor("healthCheckActor", reg)
    actor ! HttpRequest(HttpMethods.GET, Uri("/unknown"))
    expectMsg(HttpResponses.notFoundResponse)
    actor ! PoisonPill
  }
  it should " return INTERNAL_SERVER_ERROR when at least one health check fails" in {
    val reg = new HealthCheckRegistry()
    reg.register("test", new HealthCheck() {
      override def check(): Result = Result.unhealthy("failed")
    })

    val actor = createActor("healthCheckActor", reg)
    actor ! HttpRequest(HttpMethods.GET, Uri("/health"))
    expectMsg(HttpResponses.errorResponse("""{"test":{"healthy":false,"message":"failed"}}"""))
    actor ! PoisonPill
  }
  it should " return OK when all health checks succeeds" in {
    val reg = new HealthCheckRegistry()
    reg.register("test", new HealthCheck {
      override def check(): Result = Result.healthy()
    })

    val actor = createActor("ok", reg)
    actor ! HttpRequest(HttpMethods.GET, Uri("/health"))
    expectMsg(HttpResponses.okResponse("""{"test":{"healthy":true}}"""))
  }

  def createActor(name: String, healthCheckRegistry: HealthCheckRegistry): ActorRef = system.actorOf(Props(classOf[HealthActor], healthCheckRegistry, "/health", None), name)

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

}
