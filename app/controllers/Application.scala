package controllers

import models.MitchList
import play.api.libs.json.{JsValue, JsError, Json}
import play.api.mvc._

object Application extends Controller {

  private def headers = List(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "GET, POST, OPTIONS, DELETE, PUT",
    "Access-Control-Max-Age" -> "3600",
    "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept, Authorization",
    "Access-Control-Allow-Credentials" -> "true"
  )

  private def OkCORS(content: JsValue) = Ok(content)
    .withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
      ACCESS_CONTROL_ALLOW_METHODS -> "*")


  def options(id: Long) = Action {
    request => NoContent.withHeaders(headers: _*)
  }

  def indexList(id: Long) = Action {
    MitchList.get(id) match {
      case Some(mitch) => OkCORS(Json.toJson(mitch))
      case _ => BadRequest(Json.obj("status" -> NOT_FOUND, "message" -> "Not Found"))
    }
  }

  def saveList = Action(BodyParsers.parse.json) { request =>
    val mitchResult = request.body.validate[MitchList]
    mitchResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> JsError.toFlatJson(errors)))
      },
      mitchList => {
        val newId = MitchList.save(mitchList)
        if (newId > 0)
          OkCORS(Json.obj("status" -> "OK", "id" -> newId))
        else
          BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "Could not save mitchList???"))
      }
    )
  }

  def updateList(id: Long) = Action(BodyParsers.parse.json) { request => OkCORS(Json.obj("status" -> "OK")) }/*
    val mitchResult = request.body.validate[MitchList]
    mitchResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> JsError.toFlatJson(errors)))
      },
      mitchList => {
        val newId = MitchList.save(mitchList)
        if (newId > 0)
          OkCORS(Json.obj("status" -> "OK", "id" -> newId))
        else
          BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "Could not save mitchList???"))
      }
    )
  }*/

}
