package util



trait Printable[T] {


  def toPrint(t:T):String


}


object Printhelper {

  def print[A](a:A)(implicit print:Printable[A]) = print.toPrint(a)

  def printcov(a:Any) = defaultPrint.toPrint(a)

  implicit lazy val longPrint = new Printable[Long] {
    def toPrint(l:Long) = l.toString + 'L'

  }

  implicit lazy val stringPrint = new Printable[String] {
    def toPrint(t: String) = '"' + t + '"'
  }


  implicit lazy val defaultPrint:Printable[Any] = new Printable[Any] {
    def toPrint(t: Any) = t match {
      case Nil => "Nil"
      case s:String => stringPrint.toPrint(s)
      case l:Long => longPrint.toPrint(l)
      case l:List[_] => "List(" + l.map(printcov).mkString(",") + ")"
      case p:Product => productPrint.toPrint(p)
      case a => a.toString
    }
  }

  implicit lazy val productPrint:Printable[Product] = new Printable[Product] {
    def toPrint(p:Product) = {
      cleanName(p.getClass.getName) + "(" + {
        (for (v <- p.productIterator) yield {
          defaultPrint.toPrint(v)
        }).mkString(",")
      } + ")"
    }

    def cleanName(name:String) = name match {
      case n if n contains("Tuple") => ""
      case "scala.collection.immutable.Nil$" => "Nil"
      case _ => name
    }
  }



  implicit def tuplePrint[A:Printable,B:Printable](t:(A,B)):Printable[(A,B)] = new Printable[(A,B)] {
    def toPrint(t: (A, B)) = "(" + print(t._1) + ", " + print(t._2) + ')'
  }
}