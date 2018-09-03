package org.vaadin.bdd

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Barista extends Simulation {

  val baseUrl = "http://localhost:8080"

  val httpProtocol = http
    .baseURL(baseUrl)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:53.0) Gecko/20100101 Firefox/53.0")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1")
  val headers_4 = Map("Accept" -> "text/css,*/*;q=0.1")
  val headers_6 = Map("Content-Type" -> "application/json; charset=UTF-8")
  val headers_7 = Map("Pragma" -> "no-cache")

  val initSyncAndClientIds = exec((session) => {
    session.setAll(
      "syncId" -> 0,
      "clientId" -> 0
    )
  })

  val syncIdExtract = regex("""syncId": ([0-9]*),""").saveAs("syncId")
  val clientIdExtract = regex("""clientId": ([0-9]*),""").saveAs("clientId")
  val xsrfTokenExtract = regex("""Vaadin-Security-Key\\":\\"([^\\]+)""").saveAs("seckey")

  val checkOrderStatusRegExp = regex("""caption":"Mark as Confirmed""")
  val checkProductListRegExp = regex("""com.vaadin.shared.data.DataCommunicatorClientRpc","setData""")

  val dataCommunicatorId1Extract = regex("""\[\\"([0-9]*)\\",\\"com.vaadin.shared.data.DataCommunicatorClientRpc""").saveAs("dataCommId1")
  val dataCommunicatorId2Extract = regex("""\["([0-9]*)","com.vaadin.shared.data.DataCommunicatorClientRpc""").saveAs("dataCommId2")
  val newOrderButtonIdExtract = regex(""",\\"([0-9]*)\\":\{\\"caption\\":\\"New""").saveAs("newButtonId")
  val nameFieldIdExtract = regex(""","([0-9]*)":\{"placeholder":"Firstname""").saveAs("nameFieldId")
  val phoneFieldIdExtract = regex(""","([0-9]*)":\{"placeholder":"Phone""").saveAs("phoneFieldId")
  val productSelectIdExtract = regex(""","([0-9]*)":\{"placeholder":"Product""").saveAs("prodSelectId")
  val addItemButtonIdExtract = regex(""","([0-9]*)":\{[a-z0-9\\":\.%,]{10,50}"caption":"Add""").saveAs("addItemId")
  val placeOrderButtonIdExtract = regex(""","([0-9]*)":\{"caption":"Review""").saveAs("placeOrderId")
  val storeMenuIdExtract = regex(""",\\"([0-9]*)\\":\{[a-z0-9\\":\.%,]{10,50}"caption\\":\\"Storefront""").saveAs("storeMenuId")

  val scn = scenario("Barista")
    .exec(http("GET /")
      .get("/")
      .headers(headers_0))
    .pause(10)
    .exec(initSyncAndClientIds)
    .exec(http("Perform login")
      .post("/login")
      .headers(headers_0)
      .formParam("username", "barista@vaadin.com")
      .formParam("password", "barista"))
    .exec(http("UI init")
      .post("/?v-1495614595285")
      .formParam("v-browserDetails", "1")
      .formParam("theme", "orderstheme")
      .formParam("v-appId", "ROOT-2521314")
      .formParam("v-sh", "1692")
      .formParam("v-sw", "3008")
      .formParam("v-cw", "1254")
      .formParam("v-ch", "824")
      .formParam("v-curdate", "1495614595285")
      .formParam("v-tzo", "-180")
      .formParam("v-dstd", "60")
      .formParam("v-rtzo", "-120")
      .formParam("v-dston", "true")
      .formParam("v-vw", "1254")
      .formParam("v-vh", "0")
      .formParam("v-loc", "http://localhost:8080/")
      .formParam("v-wn", "ROOT-2521314-0.6857735007173337")
      .check(xsrfTokenExtract)
      .check(dataCommunicatorId1Extract)
      .check(newOrderButtonIdExtract)
      .check(storeMenuIdExtract)
    )
    .pause(2)
    .exec(http("Request orders list data")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .check(regex("""com.vaadin.shared.data.DataCommunicatorClientRpc"""))
      .body(ElFileBody("Barista_0006_request.txt")))
    .pause(12)
    .exec(http("Click new order")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .check(regex("""Firstname Lastname"""))
      .body(ElFileBody("Barista_0009_request.txt"))
      .check(nameFieldIdExtract)
      .check(phoneFieldIdExtract)
      .check(productSelectIdExtract)
      .check(addItemButtonIdExtract)
      .check(placeOrderButtonIdExtract)
    )
    .pause(3)
    .exec(http("Set full name")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0012_request.txt")))
    .pause(5)
    .exec(http("Set phone")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0013_request.txt")))
    .pause(7)
    .exec(http("Select product 1")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0014_request.txt")))
    .pause(5)
    .exec(http("Add new product")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0015_request.txt"))
      .check(productSelectIdExtract)
    )
    .pause(3)
    .exec(http("Select product 2")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0016_request.txt")))
    .pause(15)
    .exec(http("Click review order")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0017_request.txt")))
    .pause(15)
    .exec(http("Click place order")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0018_request.txt"))
      .check(checkOrderStatusRegExp))
    .pause(3)
    .exec(http("Open orders list")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0019_request.txt"))
      .check(dataCommunicatorId2Extract)
    )
    .pause(3)
    .exec(http("Fetch orders list data")
      .post("/vaadinServlet/UIDL/?v-uiId=0")
      .headers(headers_6)
      .check(syncIdExtract).check(clientIdExtract)
      .body(ElFileBody("Barista_0020_request.txt"))
      .check(checkProductListRegExp))

  setUp(scn.inject(rampUsers(1) over (250 seconds))).protocols(httpProtocol)
}
