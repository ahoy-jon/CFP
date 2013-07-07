package controllers

import play.api.mvc.Controller
import securesocial.core.SecureSocial
import play.api.data
import play.api.data.Form


object ProposalController extends SecureSocial {


  val Title = "title"
  val ContentAbstract = "contentAbstract"
  val Body = "body"
  val Comments = "comments"


  case class CreationProposalPayLoad(title:String, contentAbstract:String, body:String, comments:Option[String])

  val createProposalForm:Form[CreationProposalPayLoad] = {
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
       Ok(views.html.proposal.create(createProposalForm))

  })


  def handleCreate = SecuredAction( f => {


    implicit val request  = f.request
    createProposalForm.bindFromRequest.fold(
        errors => BadRequest(views.html.proposal.create(errors)),

        createProposalPayLoad => {

          Ok("Created")
        }


    )
  })
}


