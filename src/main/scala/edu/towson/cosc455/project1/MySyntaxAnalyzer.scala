package edu.towson.cosc455.project1

class MySyntaxAnalyzer extends SyntaxAnalyzer{

  var errorFound: Boolean = false
  var optDef: Boolean = false
  var parBCount: Integer = 0
  var parECount: Integer = 0
  var parseTree = new scala.collection.mutable.Stack[String]
  var endList: Boolean = false

  def gittexStart(): Boolean = {
    resetError()
    if (!errorFound) gittexBegin()
    true
  }

  def gittexBegin(): Unit = {
    if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.DOCB)) {
      parseTree.push(Compiler.currentToken)
    } else {
      println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected " + CONSTANTS.DOCB + ". Instead "
        + Compiler.currentToken + " was found.")
      setError()
    }
  }

  def gittexEnd(): Boolean = {
    if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.DOCE)) {
      parseTree.push(Compiler.currentToken)
      true
    } else {
      println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected " + CONSTANTS.DOCE + ". Instead "
        + Compiler.currentToken + " was found.")
      setError()
      false
    }
  }

  override def title(): Boolean = {
    var hasTitle: Boolean = false
    Compiler.currentToken match {
      case Sequence.titleSequence(_) => hasTitle = true
      case _ => setError()
    }
    parseTree.push(Compiler.currentToken)
    hasTitle
  }

  override def body(): Unit = {
    if (varDef()) {}
    else
      innerText()
    Compiler.Scanner.getNextToken()
    while (!Compiler.currentToken.equalsIgnoreCase("\\n")) {
      body()
    }
  }

  override def paragraph(): Unit = {
    parBCount += 1
    parseTree.push(Compiler.currentToken)
  }

  def paragraphEnd(): Unit = {
    parECount += 1
    if (parECount > parBCount) {
      println("Line " + Compiler.lineCount + " SYNTAX ERROR: Must have " + CONSTANTS.PARB + " before using " + CONSTANTS.PARE)
      setError()
    }
    parseTree.push(Compiler.currentToken)
  }

  override def varDef(): Boolean = {
    if (Compiler.currentToken.equalsIgnoreCase("\\title[")) {
      return false
    }
    Compiler.currentToken match {
      case Sequence.variableDefSequence(_) => parseTree.push(Compiler.currentToken)
        true
      case _ => false
    }
  }

  override def varUse(): Unit = {
    Compiler.currentToken match {
      case Sequence.variableUseSequence(_) => parseTree.push(Compiler.currentToken)
      case _ => print("ERROR: " + Compiler.currentToken)
    }
  }

  def text(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  def newLine(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  def parentheses(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  override def bold(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  override def heading(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  def comment(): Unit = {
    var hasParentheses: Boolean = false
    val commentBuffer: String = Compiler.currentToken
    Compiler.Scanner.getNextToken()
    Compiler.currentToken = commentBuffer + Compiler.currentToken
    innerText()
  }

  def innerText(): Unit = {
    Compiler.currentToken = Compiler.currentToken.filter(!" ".contains(_))
    Compiler.currentToken match {
      case Sequence.variableUseSequence(_) => varUse()
      case Sequence.headingSequence(_) => heading()
      case Sequence.boldSequence(_) => bold()
      case Sequence.listSequence(_) => listItem()
      case Sequence.imageSequence(_) => image()
      case Sequence.linkSequence(_) => link()
      case Sequence.newLineSequence(_) => newLine()
      case Sequence.textSequence(_) => text()
      case Sequence.paragraphStartSequence(_) => paragraph()
      case Sequence.paragraphEndSequence(_) => paragraphEnd()
      case Sequence.variableDefSequence(_) => varDef()
      case Sequence.commentSequence(_) => comment()
      case "" => parseTree.push(Compiler.currentToken)
      case "\\n" => EOL()
      case Sequence.endSequence(_) => parseTree.push("\\end")
      case _ => println("Line: " + Compiler.lineCount + " LEXICAL ERROR: Unknown Lexeme: " + Compiler.currentToken)
        setError()
    }
  }

  def innerItem(): Unit = {
    while (!Compiler.currentToken.equalsIgnoreCase("\\n") && !endList) {
      Compiler.currentToken match {
        case Sequence.variableUseSequence(_) => varUse()
        case Sequence.boldSequence(_) => bold()
        case Sequence.linkSequence(_) => link()
        case Sequence.textSequence(_) => text()
        case Sequence.listSequence(_) => endList = true
        case "" => parseTree.push(Compiler.currentToken)
        case _ => endList = true
      }
      if (!endList)
        Compiler.Scanner.getNextToken()
    }
  }

  def variableName(): Unit = {
    if (isText(Compiler.currentToken)) {
      if (Compiler.currentToken.endsWith("=")) {
        if (Compiler.debug)
          println("Ends in equals : " + Compiler.currentToken)
      }
      else {
        setError()
        println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected " + CONSTANTS.EQUALS + ". Instead "
          + Compiler.currentToken + " was found.")
      }
    }
    else {
      setError()
      println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected variable name. Instead" + Compiler.currentToken + " was found.")
    }
  }

  def isText(text: String): Boolean = {
    val totalMatches = Sequence.textSequence.findAllIn(text)
    if (totalMatches.size == 1) {
      return true
    }
    return false
  }

  override def listItem(): Unit = {
    innerItem()
    Compiler.Scanner.getNextToken()
    while (!Compiler.currentToken.equalsIgnoreCase("\\n") && !endList) {
      listItem()
    }
    parseTree.push("+")
  }

  override def link(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  override def image(): Unit = {
    parseTree.push(Compiler.currentToken)
  }

  def newline(): Unit = {
    if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.NEWLINE)) {
      Compiler.Scanner.getNextToken()
    } else {
      println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected " + CONSTANTS.NEWLINE + ". Instead "
        + Compiler.currentToken + " was found.")
      setError()
    }
  }

  def EOL(): Unit = {
    if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.EOLS) || Compiler.currentToken.equalsIgnoreCase("")) {
    } else {
      println("Line: " + Compiler.lineCount + ": SYNTAX ERROR: Expected " + CONSTANTS.NEWLINE + ". Instead"
        + Compiler.currentToken + " was found.")
      setError()
    }
  }

  def setError() = errorFound = true

  def resetError() = errorFound = false

  def getError: Boolean = errorFound
}