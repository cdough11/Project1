package edu.towson.cosc455.project1

import scala.collection.mutable.ListBuffer

object CONSTANTS {

  //follow BNF provided in Blackboard
  val DOCB : String = "\\BEGIN"
  val DOCE : String = "\\END"
  val TITLEB : String = "\\TITLE["
  val BRACKETB : String = "["
  val BRACKETE : String = "]"
  val HEADING : String = "#"
  val PARB : String = "\\PARB"
  val PARE : String = "\\PARE"
  val BOLD : String = "**"
  val LISTITEM : String = "+"
  val NEWLINE : String = "\\\\"
  val LINKB : String = "["
  val PARENB : String = "("
  val PARENE : String = ")"
  val IMAGEB : String = "!["
  val VARDEFB : String = "\\DEF["
  val EQUALS : String = "="
  val VARUSEB : String = "\\USE["
  val EOL : Char = '\n'
  val EOLS : String = "\\n"
  val lexemes = new ListBuffer[String]()

  lexemes += DOCB
  lexemes += DOCE
  lexemes += TITLEB
  lexemes += BRACKETE
  lexemes += HEADING
  lexemes += PARB
  lexemes += PARE
  lexemes += BOLD
  lexemes += LISTITEM
  lexemes += NEWLINE
  lexemes += LINKB
  lexemes += PARENB
  lexemes += PARENE
  lexemes += IMAGEB
  lexemes += VARDEFB
  lexemes += EQUALS
  lexemes += VARUSEB
  lexemes += "\n"
  lexemes += ""
  lexemes += "\r\n"
}
