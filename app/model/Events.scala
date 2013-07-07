package model

import securesocial.core.UserId


case class CreateProposal(user:UserId, title:String, contentAbtract:String, body:String, comments:Option[String])
