package models

import org.squeryl.KeyedEntity
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Transient
import play.api.libs.json.{JsValue, Json}
import widgetConfigs.{AlarmsConfig, ClockConfig, SprintTitleConfig, BurndownChartConfig}

class DisplayItem(val id: Long,
                  val displayId: Long,
                  var posx: Int,
                  var posy: Int,
                  var width: Int,
                  var height: Int,
                  var widgetNum: Int,
                  var widgetConfigJson: String) extends KeyedEntity[Long] {

  def this() = this(0, 0, 0, 0, 0, 0, 0, "{}")

  @Transient
  def widget: DisplayWidgets.Type = DisplayWidgets(widgetNum)

  def widget_=(displayWidget: DisplayWidgets.Type) {
    widgetNum = displayWidget.id
  }

  @Transient
  def widgetConfig = Json.parse(widgetConfigJson)

  def widgetConfig_=(widgetConfig: JsValue) {
    widgetConfigJson = Json.stringify(widgetConfig)
  }

  @Transient
  def burndownChartConfig = {
    if (widget == DisplayWidgets.BurndownChart) {
      Some(Json.fromJson[BurndownChartConfig](widgetConfig))
    } else {
      None
    }
  }

  def burndownChartConfig_=(burndownChartConfig: Option[BurndownChartConfig]) {
    if (widget == DisplayWidgets.BurndownChart) {
      widgetConfig = Json.toJson(burndownChartConfig.getOrElse(BurndownChartConfig()))
    }
  }

  @Transient
  def sprintTitleConfig = {
    if (widget == DisplayWidgets.SprintTitle) {
      Some(Json.fromJson[SprintTitleConfig](widgetConfig))
    } else {
      None
    }
  }

  def sprintTitleConfig_=(sprintTitleConfig: Option[SprintTitleConfig]) {
    if (widget == DisplayWidgets.SprintTitle) {
      widgetConfig = Json.toJson(sprintTitleConfig.getOrElse(SprintTitleConfig()))
    }
  }

  @Transient
  def clockConfig = {
    if (widget == DisplayWidgets.Clock) {
      Some(Json.fromJson[ClockConfig](widgetConfig))
    } else {
      None
    }
  }

  def clockConfig_=(clockConfig: Option[ClockConfig]) {
    if (widget == DisplayWidgets.Clock) {
      widgetConfig = Json.toJson(clockConfig.getOrElse(ClockConfig()))
    }
  }

  @Transient
  def alarmsConfig = {
    if (widget == DisplayWidgets.Alarms) {
      Some(Json.fromJson[AlarmsConfig](widgetConfig))
    } else {
      None
    }
  }

  def alarmsConfig_=(alarmsConfig: Option[AlarmsConfig]) {
    if (widget == DisplayWidgets.Alarms) {
      widgetConfig = Json.toJson(alarmsConfig.getOrElse(AlarmsConfig()))
    }
  }

  def save = inTransaction {
    WallDeeSchema.displayItems.insertOrUpdate(this)
  }

  def delete = inTransaction {
    if (isPersisted) {
      WallDeeSchema.displayItems.delete(id)
    }
  }
}

object DisplayItem {
  def findAllForDisplay(displayId: Long) = inTransaction {
    from(WallDeeSchema.displayItems)(d => where(d.displayId === displayId) select (d) orderBy (d.id asc)).toList
  }

  def findById(displayId: Long) = inTransaction {
    WallDeeSchema.displayItems.lookup(displayId)
  }

}