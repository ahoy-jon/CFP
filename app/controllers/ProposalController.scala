package controllers

import play.api.mvc.Controller
import securesocial.core.SecureSocial
import play.api.data


object ProposalController extends SecureSocial {


  val Title = "email"
  val ContentAbstract = "contentAbstract"
  val Body = "body"
  val Comments = "comments"


  case class CreationProposalPayLoad(title:String, contentAbstract:String, body:String, comments:Option[String])

  val createProposalForm = {
    import play.api.data.Form
    import play.api.data.Forms._

    Form[CreationProposalPayLoad] (
      mapping(
          Title -> nonEmptyText,
          ContentAbstract -> nonEmptyText,
          Body -> nonEmptyText,
          Comments -> optional(text)
      )(CreationProposalPayLoad.apply _)(CreationProposalPayLoad.unapply _)
    )
  }

  def create = SecuredAction( f => {
       Ok("ALLLLLLLO")

  })

}
