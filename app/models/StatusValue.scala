package models

import play.api.db._
import play.api.Play.current

import org.scalaquery.ql.TypeMapper._
import org.scalaquery.ql.extended.{ExtendedTable => Table}

import org.scalaquery.ql.extended.H2Driver.Implicit._

import org.scalaquery.session.{Database, Session}
import org.scalaquery.ql.Query

case class StatusValue(id: Option[Long], statusMonitorId: Long, valuesJson: String) {
  def insert = StatusValue.database.withSession {
    implicit db: Session =>
      StatusValue.insert(this)
  }

  def update = StatusValue.database.withSession {
    implicit db: Session =>
      StatusValue.where(_.id === id).update(this)
  }

  def delete = StatusValue.database.withSession {
    implicit db: Session =>
      StatusValue.where(_.id === id).delete
  }
}

object StatusValue extends Table[StatusValue]("STATUSVALUE") {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def id = column[Long]("ID", O PrimaryKey, O AutoInc)

  def statusMonitorId = column[Long]("STATUSMONITORID", O NotNull)

  def valuesJson = column[String]("VALUESJSON", O NotNull)

  def * = id.? ~ statusMonitorId ~ valuesJson <>((apply _).tupled, unapply _)

  def query = Query(this)

  def findAllForStatusMonitor(statusMonitorId: Long) = database.withSession {
    implicit db: Session =>
      query.where(s => s.statusMonitorId === statusMonitorId).orderBy(id.desc).list
  }
}