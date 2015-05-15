package models

import play.api.libs.json.Json

/**
 * Created by jason.dubaniewicz on 3/10/15.
 */
case class MitchListItem(id: Long, content: String)

object MitchListItem {

  implicit val mitchListItemFormat = Json.format[MitchListItem]
}
