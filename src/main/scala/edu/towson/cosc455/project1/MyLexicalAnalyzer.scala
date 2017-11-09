package edu.towson.cosc455.project1

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer

class MyLexicalAnalyzer extends LexicalAnalyzer {

  var errorFound: Boolean = false
  var sourceLine: String = ""
  var temp : String = ""
  var lexemes = new ListBuffer[String]
  var lexeme = new ArrayBuffer[Char](100)
  var nextChar: Char = ' '
  var lexLength: Int = 0
  var position: Int = 0
  var containsLexemes : Boolean = false
  var isText : Boolean = false
  var isTextToken : Boolean = false
  var tempPos : Int = 0
  var tempChar : Char = ' '

  def start(line: String): Unit = {
    initializeLexems()
    sourceLine = line
    position = 0
    lexeme.clear()
    getNextToken()
    containsLexemes = false
  }

  def initializeLexems(): Unit = {
    lexemes = CONSTANTS.lexemes
  }

  def getChar(): Unit = {
    if (position < sourceLine.length()) {
      nextChar = sourceLine.charAt(position)
      position += 1
    }
    else
      nextChar = '\n'
  }

  def getNextToken(): Unit = {
    lexeme.clear()
    lexLength = 0
    isTextToken = false
    getChar()
    if(isEOL(nextChar))
    {
      setCurrentToken("\\n")
    }
    while(!isEOL(nextChar) && !isTextToken) {
      if(isLexeme(nextChar))
      {
        addLexemeChar()
        setCurrentToken(lexeme.mkString)
        if(!isLexeme(nextChar))
          getChar()

      }
      else if(isValidText(nextChar.toString))
      {
        while(!isEOL(nextChar) && !isTextToken ) {
          if(isSpace(nextChar)) {
            setCurrentToken(lexeme.mkString)
            isTextToken = true
          }
          else {
            addChar()
            getChar()
          }
        }
      }
      else
      {
        if(isSpace(nextChar)) {
          if(!lexeme.contains(" ") && !isEOL(nextChar)) {
            setCurrentToken(lexeme.mkString)
            isTextToken = true
          }
        }
      }
      if(!lexeme.mkString.contains("\\n"))
      {
        setCurrentToken(lexeme.mkString)
        isTextToken = true
      }
    }
  }

  def isValidText(tmp : String) : Boolean = {
    tmp match {
      case Sequence.validTextSequence(_) => true
      case _ => false
    }
  }

  def isSpace(c : Char) : Boolean = {
    nextChar.toString.equalsIgnoreCase(" ")
  }

  def lookup(token: String): Boolean = {
    if (lexemes.contains(token)) {
      true
    }
    else if(isText)
    {
      isText = false
      true
    }
    else {
      Compiler.Parser.setError()
      println("Line " + Compiler.lineCount + ": LEXICAL ERROR: " + token + " is not consider a valid token.")
      setError()
      lexeme.clear()
      false
    }
  }

  def isLexeme(nextChar: Char): Boolean = {
    nextChar match {
      case '[' =>  true
      case ']' =>  true
      case '!' =>  true
      case '(' =>  true
      case ')' =>  true
      case '*' => true
      case '#' => true
      case '\\' => true
      case '+' => true
      case _ =>  false
    }
  }

  def addLexemeType() : Unit = {
    nextChar = tempChar
    position = tempPos
    lexeme += nextChar
    while(!isEOL(nextChar) && !nextChar.toString.equalsIgnoreCase("]"))
    {
      getChar()
      if(!isLexeme(nextChar))
        addChar()
      else
        addLexemeChar()
    }
  }

  def newLine(): Unit =
  {
    lexeme += nextChar
    lexeme += nextChar
    lookup(lexeme.mkString)
  }

  def closingBracket() : Unit = {
    lexeme += nextChar
    while (!nextChar.toString.equals(CONSTANTS.BRACKETE) && !isEOL(nextChar)) {
      getChar()
      addChar()
    }
    if (isEOL(nextChar) && !nextChar.toString.equals(CONSTANTS.BRACKETE)) {
      setError()
      println("Line: " + Compiler.lineCount + " SYNTAX ERROR: Expected closing bracket "
        + CONSTANTS.BRACKETE + "after " + CONSTANTS.BRACKETB + " usage.")
    }
  }

  def isEOL(nc :Char): Boolean = {
    nc.toString.contains("\n")
  }

  def addLexemeChar() : Unit = {
    nextChar match {
      case '[' => closingBracket()
      case '*' => bold()
      case '+' => list()
      case '\\' => slash()
      case '(' => parentheses()
      case '!' => image()
      case '#' => heading()
      case ']' => lexeme += nextChar
      case _ => println("Unknown Lexeme found: " + nextChar )
    }
  }

  def  image() : Unit = {
    lexeme += nextChar
    while (!isEOL(nextChar) && !nextChar.toString.equals(CONSTANTS.PARENE) )
    {
      getChar()
      addChar()
    }
  }

  //keep close eye on this
  def bold() : Unit = {
    var tempPos : Int = position
    var tempChar : Char = nextChar
    var foundTrailing : Boolean = false
    getChar()
    if(nextChar.toString.equalsIgnoreCase(CONSTANTS.BOLD))
    {
      lexeme += nextChar
      lexeme += nextChar
      while(!foundTrailing || isEOL(nextChar)) {
        getChar()
        if (!nextChar.toString.equalsIgnoreCase(CONSTANTS.BOLD))
          addChar()
        else {
          tempPos = position
          tempChar = nextChar
          getChar()
          if (nextChar.toString.equalsIgnoreCase(CONSTANTS.BOLD)) {
            lexeme += nextChar
            lexeme += nextChar
            foundTrailing = true
          }
          else {
            println("Line : " + Compiler.lineCount + " LEXICAL ERROR: Expected bold ending: " + CONSTANTS.BOLD)
          }
        }
      }
    }
    else
    {
      nextChar = tempChar
      position = tempPos
      lexeme += nextChar
      while(!foundTrailing || isEOL(nextChar))
      {
        getChar()
        if(!nextChar.toString.equalsIgnoreCase(CONSTANTS.BOLD))
          addChar()
        else {
          foundTrailing = true
          lexeme += nextChar
        }
      }
      if(!foundTrailing)
      {
        println("Line : " + Compiler.lineCount + " LEXICAL ERROR: Expected bold ending: " + CONSTANTS.BOLD)
        setError()
      }

    }
  }

  def list() : Unit = {
    lexeme += nextChar
  }

  def slash() : Unit = {
    tempPos = position
    tempChar = nextChar
    getChar()
    if(nextChar.toString.equalsIgnoreCase(CONSTANTS.NEWLINE))
    {
      newLine()
    }
    else
    {
      addLexemeType()
    }
  }

  def parentheses() : Unit = {
    lexeme += nextChar
    while (!nextChar.toString.equals(CONSTANTS.PARENE) && !isEOL(nextChar)) {
      getChar()
      addChar()
    }
    if (isEOL(nextChar) && !nextChar.toString.equals(CONSTANTS.BRACKETE)) {
      setError()
      println("Line: " + Compiler.lineCount + " SYNTAX ERROR: Expected closing parenthesis "
        + CONSTANTS.PARENE + " after " + CONSTANTS.PARENB + " usage.")
    }
  }

  def heading() : Unit = {
    lexeme += nextChar
    while (!isEOL(nextChar))
    {
      getChar()
      addChar()
    }
  }

  def addChar(): Unit =
  {
    if (lexLength <= sourceLine.length()) {
      if (!isEOL(nextChar)) {
        lexLength += 1
        lexeme += nextChar
      }
    }
  }

  def setCurrentToken(currToken: String): Unit = {
    Compiler.currentToken_$eq(currToken)
  }

  def setError() = errorFound = true

  def resetError() = errorFound = false

  def getError: Boolean = errorFound
}