package edu.towson.cosc455.project1

import java.io.FileWriter
import java.lang.Compiler

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class MySemanticAnalyzer {

  var inputStack = new mutable.Stack[String]
  var parseTree = new mutable.Stack[String]
  val variables = new mutable.HashMap[String, String]()
  var lexeme: String = ""
  var fileName: String = ""
  var scope = new mutable.HashMap[String, String]()
  var scopeNames = new ArrayBuffer[String](100)
  var listItem: String = "z"

  def convert(parseStack: mutable.Stack[String]): String = {
    fileName = Compiler.fileName
    inputStack = parseStack
    while (!inputStack.isEmpty) {
      lexeme = inputStack.pop()
      lexeme match {
        case Sequence.beginSequence(_) => begin()
        case Sequence.titleSequence(_) => title()
        case Sequence.headingSequence(_) => heading()
        case Sequence.variableDefSequence(_) => findValues()
        case Sequence.variableUseSequence(_) => varUse()
        case Sequence.boldSequence(_) => bold()
        case Sequence.listSequence(_) => list()
          while (!listItem.equalsIgnoreCase("") && !inputStack.isEmpty && !listItem.equalsIgnoreCase("+")) {
            listItem = inputStack.pop
            listItem match {
              case "+" => list()
              case "" => space()
              case _ => parseTree.push(listItem)
            }
          }
          inputStack.push("<li>")
        case Sequence.paragraphStartSequence(_) => paragraphStart()
        case Sequence.paragraphEndSequence(_) => paragraphEnd()
        case Sequence.imageSequence(_) => image()
        case Sequence.linkSequence(_) => link()
        case Sequence.newLineSequence(_) => newLine()
        case Sequence.textSequence(_) => addText()
        case "<li>" => parseStack.push("</li>")
        case "" =>
        case Sequence.endSequence(_) => end()
        case _ => println("No match found: " + lexeme)
      }
    }
    output()
    fileName
  }

  def resetVariables(): Unit = {
    var i: Int = 0
    variables.clear()
    while (i < scopeNames.length) {
      variables += (scopeNames(i) -> scope(scopeNames(i)))
      i += 1
    }
  }

  def storeVariables(vName: String, valValue: String): Unit = {
    scope += (vName -> valValue)
    scopeNames += vName
  }

  def space(): Unit = {
    parseTree.push(" ")
  }

  def output(): Unit = {
    val fileWriter = new FileWriter(fileName + ".html", true)
    while (!parseTree.isEmpty) {
      lexeme = parseTree.pop()
      lexeme match {
        case Sequence.variableDefSequence(_) => findRunTimeValues()
        case Sequence.variableUseSequence(_) => fileWriter.append(retrieveValue())
        case "" => fileWriter.append(" ")
        case "</p>" => fileWriter.append(lexeme)
          resetVariables()
        case "\\n" => newLine()
        case _ => fileWriter.append(lexeme)
      }
    }
    fileWriter.close()
  }

  def findRunTimeValues(): Unit = {
    var defToken: String = lexeme
    defToken = defToken.filter(!" ".contains(_))
    val valName: String = defToken.substring(defToken.indexOf("[") + 1, defToken.indexOf("="))
    val valValue: String = defToken.substring(defToken.indexOf("=") + 1, defToken.indexOf("]"))
    if (variables.contains(valName)) {
      variables.put(valName, valValue)
    }
    else {
      variables += (valName -> valValue)
      storeVariables(valName, valValue)
      scopeNames += valName
    }
  }

  def begin(): Unit = {
    parseTree.push("<html>")
  }

  def title(): Unit = {
    parseTree.push("</head>")
    parseTree.push("</title>")
    parseTree.push(lexeme.substring(lexeme.indexOf("[") + 1, lexeme.indexOf("]")))
    parseTree.push("<title>")
    parseTree.push("<head>")
  }

  def heading(): Unit = {
    parseTree.push("</h1>")
    parseTree.push(lexeme.filter(!"#".contains(_)))
    parseTree.push("<h1>")
  }

  def paragraphStart(): Unit = {
    parseTree.push("<p>")
  }

  def paragraphEnd(): Unit = {
    parseTree.push("</p>")
  }

  def newLine() = {
    parseTree.push("<br/>")
  }

  def link(): Unit = {
    var linkLex: String = lexeme
    parseTree.push("<a href=\"" + linkLex.substring(linkLex.indexOf("(") + 1, linkLex.indexOf(")")) + "\">" + linkLex.substring(linkLex.indexOf("[") + 1, linkLex.indexOf("]")) + "</a>")
  }


  def image(): Unit = {
    var imgLex: String = lexeme
    imgLex = imgLex.filter(!"!".contains(_))
    parseTree.push("<img src=\"" + imgLex.substring(imgLex.indexOf("(") + 1, imgLex.indexOf(")")) + "\" alt=\"" + imgLex.substring(imgLex.indexOf("[") + 1, imgLex.indexOf("]")) + "\">")
  }

  def list() = {
    parseTree.push("<li>")
  }

  def bold(): Unit = {
    parseTree.push("</b>")
    parseTree.push(lexeme.filter(!"**".contains(_)))
    parseTree.push("<b>")
  }

  def varDef(): Unit = {
    parseTree.push(lexeme)
  }

  def varUse(): Unit = {
    parseTree.push(lexeme)
  }

  def end(): Unit = {
    parseTree.push("</html>")
  }

  def addText(): Unit = {
    parseTree.push(lexeme)
    parseTree.push(" ")
  }

  def retrieveValue(): String = {
    val fileWriter = new FileWriter(fileName + ".html", true)
    var useToken: String = lexeme
    val varName = useToken.substring(useToken.indexOf("[") + 1, useToken.indexOf("]"))
    if (variables.contains(varName)) {
      variables(varName)
    }
    else {
      println("SEMANTIC ERROR: Undefined variable: " + varName)
      System.exit(-1)
      ""
    }
  }

  def findValues(): Unit = {
    var token: String = lexeme
    token = token.filter(!" ".contains(_))
    val valName: String = token.substring(token.indexOf("[") + 1, token.indexOf("="))
    val valValue: String = token.substring(token.indexOf("=") + 1, token.indexOf("]"))
    variables += (valName -> valValue)
    storeVariables(valName, valValue)
    scopeNames += valName
    varDef()
  }
}