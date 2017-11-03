package edu.towson.cis.cosc455.cdough11.project1

import scala.collection.mutable.Stack


class MySyntaxAnalyzer extends SyntaxAnalyzer {

  var parseTree = Stack[String]()
  var scanner = Compiler.Scanner

  override def gittex(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.DOCB)){
      addToStack()
      variableDefine()
      title()
      body()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.DOCE)){
        addToStack()
      }
      else{
        syntaxError("Gittex file does not end with proper keyword.")
      }
      if(scanner.hasNextToken()){
        syntaxError("No code should be added after end keyword.")
      }
    }
    else{
      syntaxError("Gittex file does not start with proper keyword.")
    }
  }

  override def title(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.TITLEB)){
      addToStack()
      reqText()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BRACKETE)){
        addToStack()
      }
      else{
        syntaxError("Title does not end with proper keyword. Should be "+ CONSTANTS.BRACKETE +  "instead of " + Compiler.currentToken)
      }
    }
    else{
      syntaxError("Title does not begin with proper keyword. Should be" + CONSTANTS.TITLEB + " instead of " + Compiler.currentToken)
    }
  }

  override def body(): Unit = {
    if (CONSTANTS.innerText.exists(x => x.equalsIgnoreCase(Compiler.currentToken))) {
      innerText()
      body()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARB)){
      paragraph()
      body()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.NEWLINE)){
      newline()
      body()
    }
  }

  override def paragraph(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARB)){
      addToStack()
      variableDefine()
      innerText()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARE)){
        addToStack()
      }
      else
        syntaxError("Paragraph does not end with proper keyword. Should be " + CONSTANTS.PARE + " instead of " + Compiler.currentToken)
    }
    else{
      syntaxError("Paragraph does not begin with proper keyword. Should be " + CONSTANTS.PARB + " instead of " + Compiler.currentToken)
    }
  }

  override def heading(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.HEADING)){
      addToStack()
      reqText()
    }
  }

  override def variableDefine(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.VARDEFB)){
      addToStack()
      handleVariable()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.EQUALS)){
        addToStack()
      }
      else
        syntaxError(CONSTANTS.EQUALS + " expected")
      handleVariable()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BRACKETE)){
        addToStack()
      }
      else
        syntaxError("Variable definition does not end with proper keyword. Should be "
          + CONSTANTS.BRACKETE + " instead of " + Compiler.currentToken)
    }
    else
      syntaxError("Variable definition does not begin with proper keyword. Should be "
        + CONSTANTS.VARDEFB + " instead of " + Compiler.currentToken)
  }

  override def variableUse(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.VARUSEB)){
      addToStack()
      handleVariable()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BRACKETE)) {
        addToStack()
      }
      else
        syntaxError("Variable use does not end with proper keyword. Should be "
          + CONSTANTS.BRACKETE + " instead of " + Compiler.currentToken)
    }
    else
      syntaxError("Variable use does not begin with proper keyword. Should be "
        + CONSTANTS.VARUSEB + " instead of " + Compiler.currentToken)
  }

  override def bold(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BOLD)){
      addToStack()
      text()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BOLD)){
        addToStack()
      }
      else
        syntaxError("Bold does not end with proper keyword. Should be " + CONSTANTS.BOLD + " instead of " + Compiler.currentToken)
    }
    else
      syntaxError("Bold does not begin with proper keyword. Should be " + CONSTANTS.BOLD + " instead of " + Compiler.currentToken)
  }

  override def listItem(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.LISTITEMB)){
      addToStack()
      innerItem()
      listItem()
    }
  }

  override def link(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.LINKB)){
      addToStack()
      reqText()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BRACKETE)) {
        addToStack()
        if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARENB)) {
          addToStack()
          reqText()
          if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARENE)) {
            addToStack()
          }
          else
            syntaxError(CONSTANTS.PARENE + " expected")
        }
        else
          syntaxError(CONSTANTS.PARENB + " expected")
      }
      else
        syntaxError(CONSTANTS.BRACKETE + " expected")
    }
    else
      syntaxError(CONSTANTS.LINKB + " expected")
  }

  override def image(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.IMAGEB)){
      addToStack()
      reqText()
      if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BRACKETE)) {
        addToStack()
        if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARENB)) {
          addToStack()
          reqText()
          if (Compiler.currentToken.equalsIgnoreCase(CONSTANTS.PARENE)) {
            addToStack()
          }
          else
            syntaxError(CONSTANTS.PARENE + " expected")
        }
        else
          syntaxError(CONSTANTS.PARENB + " expected")
      }
      else
        syntaxError(CONSTANTS.BRACKETE + " expected")
    }
    else
      syntaxError(CONSTANTS.IMAGEB + " expected")
  }

  override def newline(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.NEWLINE)){
      addToStack()
    }
  }

  def innerItem(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.VARUSEB)){
      variableUse()
      innerItem()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BOLD)){
      bold()
      innerItem()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.LINKB)){
      link()
      innerItem()
    }
    if(CONSTANTS.REQTEXT.exists(x => x.equalsIgnoreCase(Compiler.currentToken))){
      addToStack()
      innerItem()
    }
  }

  def handleVariable(): Unit ={
    var input : String = ""
    while(CONSTANTS.VALIDTEXT.exists(x => x.equalsIgnoreCase(Compiler.currentToken))){

      if(!CONSTANTS.BLANKSPACE.exists(x => x.equals(Compiler.currentToken))){
        input += Compiler.currentToken
      }

      scanner.getNextToken()
    }
    parseTree.push(input)
  }

  def innerText(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.VARUSEB)){
      variableUse()
      innerText()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.HEADING)){
      heading()
      innerText()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.BOLD)){
      bold()
      innerText()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.LISTITEMB)){
      listItem()
      innerText()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.IMAGEB)){
      image()
      innerText()
    }
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.LINKB)){
      link()
      innerText()
    }
    if(CONSTANTS.VALIDTEXT.exists(x => x.equalsIgnoreCase(Compiler.currentToken))){
      addToStack()
      innerText()
    }
  }

  def text(): Unit ={
    while(CONSTANTS.VALIDTEXT.exists(x => x.equalsIgnoreCase(Compiler.currentToken)))
      addToStack()
  }

  def reqText(): Unit = {
    while(CONSTANTS.REQTEXT.exists(x => x.equalsIgnoreCase(Compiler.currentToken)))
      addToStack()
  }

  def addToStack(): Unit = {
    parseTree.push(Compiler.currentToken)
    if(scanner.hasNextToken()){
      scanner.getNextToken()
    }
  }
  def syntaxError(message : String): Unit ={
    println("SYNTAX ERROR: " + message)
    System.exit(1)
  }
}
