package controllers.widgets

import play.api.data.Forms._
import models.widgetConfigs.SprintTitleConfig
import models.{Sprint, DisplayItem, Display}
import play.api.templates.Html
import models.utils.DataDigest

object SprintTitle extends Widget[SprintTitleConfig] {
  val configMapping = mapping(
    "labelFont" -> optional(text),
    "labelSize" -> optional(number)
  )(SprintTitleConfig.apply)(SprintTitleConfig.unapply)

  def renderHtml(display: Display, displayItem: DisplayItem): Html = {
    Sprint.findById(display.sprintId).map {
      sprint =>
        views.html.display.widgets.sprintTitle.render(display, displayItem, sprint)
    }.getOrElse(Html(""))
  }
}
