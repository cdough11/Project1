package edu.towson.cis.cosc455.cdough11.project1

object CONSTANTS {
  val DOCB : String = 	"\\BEGIN"
  val DOCE : String = 	"\\END"
  val TITLEB : String = "\\TITLE["
  val PARB : String = "\\PARB"
  val PARE : String = "\\PARE"
  val VARDEFB : String = "\\DEF["
  val VARUSEB : String = "\\USE["
  val BRACKETE : String = "]"
  val HEADING : String = "#"
  val BOLD : String = "*"
  val LISTITEMB : String = "+"
  val NEWLINE : String = "\\\\"
  val IMAGEB : String = "!["
  val LINKB : String = "["
  val EQUALS : String = "="
  val PARENB : String = "("
  val PARENE : String = ")"

  val BLANKSPACE : List[String] = List(" ", "\t", "\n", "\b", "\f", "\r")

  val NUMSANDSPEC : List[String] = List("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",",".","/",":","?","_","/", "'", "")

  val LETTERS : List[String] = List("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z")

  val VALIDTEXT : List[String] = List("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
    "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
    "0","1","2","3","4","5","6","7","8","9",",",".","?","_","/", """ """) ::: BLANKSPACE

  val REQTEXT : List[String] = List("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
  "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
  "0","1","2","3","4","5","6","7","8","9",",",".","?","_","/", """ """)

  val innerText = List(VARUSEB, HEADING, BOLD, LISTITEMB, IMAGEB, LINKB) ::: VALIDTEXT

  val BEGINNINGS = List(DOCB, TITLEB, HEADING, PARB, LISTITEMB, LINKB, IMAGEB, VARDEFB, VARUSEB)

  val ENDINGS = List(DOCE, BRACKETE, PARE, "\n")

  val NEWLINELIST = List(NEWLINE)

  val BOLDLIST = List(BOLD)

  val PARENTHESES = List(PARENB, PARENE)

  val SPECIALCHARS : List[String] = List("\\","[","#","*","!","+")

  val terminalChars = BEGINNINGS ::: ENDINGS ::: NEWLINELIST ::: BOLDLIST ::: PARENTHESES ::: SPECIALCHARS :: BLANKSPACE :: LETTERS :: NUMSANDSPEC

}
