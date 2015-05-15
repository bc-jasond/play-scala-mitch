package models

import play.api.libs.json._

/**
 * Created by jason.dubaniewicz on 3/5/15.
 */

case class MitchList(id: Long, title: String, items: List[MitchListItem])

object MitchList {

  implicit val mitchListFormat = Json.format[MitchList]

  def get(id: Long): Option[MitchList] = {
      MitchDB.getList(id) match {
        case Some((id, title)) => Some(MitchList(id, title, MitchDB.getListItems(id)))
        case _ => None
      }
  }

  def save(list: MitchList): Long = {
    MitchDB.saveList(list)
  }

  /*def update(list: MitchList): Long = {
    MitchDB.updateList(list)
  }*/

  def delete(id: Long) = ???
}
