package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current
/**
 * Created by jason.dubaniewicz on 3/5/15.
 */

object MitchDB {

  private def stupidEsc(s: String): String = {
    // stupid escape tactic: remove all double quotes
    s.replaceAll("\"", "")
  }

  def getList(id: Long) = {
    val mitchList = {
      long("id") ~ str("title") map {
        case id ~ title => (id, title)
      }
    }
    DB.withConnection { implicit c => {
        SQL("SELECT id, title FROM list WHERE id = " + id).as( mitchList * ).headOption
      }
    }
  }

  def getListItems(listId: Long) = {
    val mitchListItem = {
      long("id") ~ str("content") map {
        case id ~ content => MitchListItem(id, content)
      }
    }
    DB.withConnection { implicit c =>
      SQL("""
       SELECT li.id as id, content
        FROM list_item li
        INNER JOIN list l on li.list_id = l.id
       WHERE l.id = """ + listId).as( mitchListItem * )
    }
  }

  /**
   * create a new list in the DB
   *
   * @param mitchList
   * @return id if success or 0 if failed
   */
  def saveList(mitchList: MitchList): Long = {
    DB.withTransaction { implicit c =>
      // list table first
      SQL("INSERT INTO list SET title = \"" + stupidEsc(mitchList.title) + "\"").executeInsert() match {
        // if that worked, then we save the items
        case Some(id: Long) => {
          SQL("INSERT INTO list_item (list_id, content) VALUES " + {
            mitchList.items map {
              case MitchListItem(_, content) => "(" + id + ",\"" + stupidEsc(content) + "\")"
            } mkString(",")
          }).executeInsert( scalar[Long] * ) match {
              case x :: xs => id
              // items insert failed, return 0
              case _ => 0
            }
        }
        // list insert failed, return 0
        case _ => 0
      }
    }
  }

  /*def updateList(mitchList: MitchList): Long = {
    DB.withTransaction { implicit c =>
      val stuff = SQL("UPDATE list SET title = \"" + stupidEsc(mitchList.title) + "\"").executeUpdate()
    }
    1
  }*/

}
