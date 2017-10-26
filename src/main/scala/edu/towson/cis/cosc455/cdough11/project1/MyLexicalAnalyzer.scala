package edu.towson.cis.cosc455.cdough11.project1

import scala.collection.mutable.ArrayBuffer

class MyLexicalAnalyzer extends LexicalAnalyzer {

  var lexems: ArrayBuffer[String] = new ArrayBuffer[String]()
  var iterator: Iterator[String] = null
  var nextChar: Char = 0
  var currentToken: String = ""
  var position: Int = 0

  val terminal = CONSTANTS.terminalChars
  val slash: Char = '\\'
  val bracketB: Char = '['
  val bracketE: Char = ']'
  val parenB: Char = '('
  val parenE: Char = ')'
  val exclamation: Char = '!'
  val heading: Char = '#'
  val list: Char = '+'
  val bold: Char = '*'
  val newline: Char = '\n'
  val tab: Char = '\t'

  override def addChar(): Unit = {
    currentToken += nextChar
  }

  override def getChar(): Char = {
    val c: Char = Compiler.fileContents.charAt(position)
    position += 1
    return c
  }

  override def getNextToken(): Unit = {
    Compiler.currentToken = iterator.next()
  }

  def nextToken(): Unit = {
    if (text()) {
      addText()
    }
    else if (special()) {
      addChar()
      specialToken(nextChar)
    }
    else if (nextChar.toString == " ") {}
    else {
      lexicalError(nextChar.toString)
    }
  }

  def addToken(): Unit = {
    lexems += currentToken
    newToken()
  }

  def initializeLexems(): Unit = {
    lexems = ArrayBuffer(CONSTANTS.DOCB, CONSTANTS.DOCE, CONSTANTS.TITLEB, CONSTANTS.BRACKETE,
      CONSTANTS.HEADING, CONSTANTS.PARB, CONSTANTS.PARE, CONSTANTS.BOLD, CONSTANTS.LISTITEMB,
      CONSTANTS.NEWLINE, CONSTANTS.LINKB, CONSTANTS.PARENB, CONSTANTS.PARENE, CONSTANTS.IMAGEB,
      CONSTANTS.VARDEFB, CONSTANTS.EQUALS, CONSTANTS.VARUSEB)
  }

  def getLexems(): Unit = {
    while (position < Compiler.fileContents.length()) {
      nextChar = getChar()
      nextToken()
    }
    iterator = lexems.iterator
  }

  def hasNextToken(): Boolean = {
    iterator.hasNext
  }

  def specialToken(c: Char): Unit = {
    c match {
      case `bracketB` => bracket()
      case `slash` => backslash()
      case `heading` => headingAndList()
      case `list` => headingAndList()
      case `bold` => boldText()
      case `exclamation` => exclaim()
    }
  }
  def backslash(): Unit ={
    nextChar = getChar()
    if(nextChar == slash){
      addChar()
      addToken()
    }
    else{

      while(nextChar != newline && nextChar != bracketB){
        if(letters()) addChar()
        else if(space()) {}
        else lexicalError(nextChar.toString)
        nextChar = getChar()
      }
      if(nextChar == bracketB) addChar()

      if(!lookup()) lexicalError(currentToken)
      addToken()

      if(lexems.last.equalsIgnoreCase(CONSTANTS.TITLEB) || lexems.last.equalsIgnoreCase(CONSTANTS.VARUSEB) ||
        lexems.last.equalsIgnoreCase(CONSTANTS.VARDEFB)){
        nextChar = getChar()
        while(nextChar != bracketE){
          addInnerText()
          nextChar = getChar()
        }
        if(nextChar == bracketE) addText()
      }
    }
  }

  def bracket(): Unit ={
    addText()
    nextChar = getChar()
    addLink()
  }

  def exclaim(): Unit ={
    nextChar = getChar()
    if(nextChar != bracketB) lexicalError(nextChar.toString)
    addChar()
    addToken()
    nextChar = getChar()
    addLink()
  }

  def headingAndList(): Unit ={
    addText()
    nextChar = getChar()
    while(nextChar != newline){
      nextToken()
      nextChar = getChar()
    }
    addLine()
  }

  def boldText(): Unit ={
    currentToken = nextChar+""
    nextChar = getChar()
    if(nextChar == bold){
      currentToken += bold
      addToken()
    }
    else{
      addToken()
      position = position - 1

    }
  }

  def addText(): Unit ={
    if(nextChar != newline && nextChar != tab){
      newToken()
      addChar()
      addToken()
    }
  }

  def addLine(): Unit ={
    if(nextChar == newline){
      newToken()
      addChar()
      addToken()
    }
  }

  def addLink(): Unit ={
    while(nextChar != bracketE){
      addText()
      nextChar = getChar()
    }
    if(nextChar == bracketE) addText()
    nextChar = getChar()
    delete()
    if(nextChar == parenB) addText()
    nextChar = getChar()
    while(nextChar != parenE){
      addText()
      nextChar = getChar()
    }
    addText()
  }

  def delete(): Unit ={
    while(space()){
      nextChar = getChar()
    }
  }

  def newToken(): Unit ={
    currentToken = ""
  }

  def addInnerText(): Unit ={
    if(text() || CONSTANTS.EQUALS.equals("=")){
      addText()
    }
  }

  def text(): Boolean = {
    CONSTANTS.VALIDTEXT.exists(x => x.equalsIgnoreCase(nextChar+""))
  }

  def letters(): Boolean = {
    CONSTANTS.LETTERS.exists(x => x.equalsIgnoreCase(nextChar+""))
  }
  def special(): Boolean = {
    CONSTANTS.SPECIALCHARS.exists(x => x.equals(nextChar+""))
  }
  def space(): Boolean = {
    CONSTANTS.BLANKSPACE.exists(x => x.equals(nextChar+""))
  }
  override def lookup(): Boolean = {
    if(lexems.contains(currentToken))
    true
    else
    {
      lexicalError(currentToken)
      System.exit(1)
      false
    }
  }

  def lexicalError(token:String) = {
    println("Lexical error! " + token + " is not a valid symbol.")
    System.exit(1)
  }
}
