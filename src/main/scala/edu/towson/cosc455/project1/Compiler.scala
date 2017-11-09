package edu.towson.cosc455.project1

import scala.io.Source

import java.awt.Desktop
import java.io.{File, IOException}

object Compiler {

  var currentToken: String = ""
  var lineCount = 0
  val Scanner = new MyLexicalAnalyzer
  val Parser = new MySyntaxAnalyzer
  val Converter = new MySemanticAnalyzer
  var debug: Boolean = false
  var passCount: Integer = 0
  var hasGittexBegin: Boolean = false
  var hasGittexEnd: Boolean = false
  var hasOptVar: Boolean = false
  var hasTitle: Boolean = false
  var hasBody: Boolean = false
  var fileName: String = ""

  def main(args: Array[String]) = {
    checkInput(args)
    Compile(args(0))
  }

  def getCurrentToken(): Unit = {
    if (debug)
      println("Token passed to Parser: " + currentToken)
  }

  def Compile(fileName: String): Unit = {
    var file = fileName
    file = "./" + file
    for (line <- Source.fromFile(file).getLines()) {
      if (line.length > 0 && hasGittexEnd) {
        lineCount += 1
        println("Line: " + lineCount + " SYNTAX ERROR: No text allowed after " + CONSTANTS.DOCE + " is declared" + line)
        System.exit(-1)
      }
      var currLine: String = line
      val filteredLine: String = currLine.filter(!"\t".contains(_))
      if (!hasGittexEnd) {
        Scanner.start(filteredLine)
        lineCount += 1

        if (!hasGittexBegin && passCount == 0) {
          getCurrentToken()
          if (!Scanner.getError)
            hasGittexBegin = Parser.gittexStart()
          hasSyntaxErrors()
        }
        if (!hasOptVar && hasGittexBegin && passCount == 1) {
          getCurrentToken()
          if (!Scanner.getError)
            hasOptVar = Parser.varDef()
          hasSyntaxErrors()
        }
        if ((!hasTitle && hasGittexBegin && !hasOptVar && passCount == 1) || (!hasTitle && passCount == 2 && hasGittexBegin)) {
          getCurrentToken()
          if (!Scanner.getError)
            hasTitle = Parser.title()
          hasSyntaxErrors()
        }
        if (!hasBody && hasTitle && passCount > 2 || !hasBody && passCount == 2) {
          if (currentToken.equalsIgnoreCase(CONSTANTS.DOCE)) {
            hasBody = true
            hasGittexEnd = true
            Parser.gittexEnd()
          }
          else {
            hasBody = false
            getCurrentToken()
            if (!Scanner.getError)
              Parser.body()
          }
          hasSyntaxErrors()
        }
        passCount += 1
      }
    }

    if (Parser.parBCount != Parser.parECount) {
      if (Parser.parBCount > Parser.parECount)
        println("SYNTAX ERROR: Missing paragraph closing tag (" + CONSTANTS.PARE + ")")
      else
        println("SYNTAX ERROR: Missing paragraph start tag (" + CONSTANTS.PARB + ")")
    }
    if (!hasGittexEnd && hasBody) {
      if (!Scanner.getError)
        hasGittexEnd = Parser.gittexEnd()
    }
    hasSyntaxErrors()
    runHTMLInBrowser(Converter.convert(Parser.parseTree))
  }

  def runHTMLInBrowser(htmlFile: String) = {
    val file: File = new File(fileName.dropRight(0) + ".html")
    println(file.getAbsolutePath)
    if (!file.exists())
      sys.error("File " + htmlFile + " does not exist.")

    try {
      Desktop.getDesktop.browse(file.toURI)
    }
    catch {
      case ioe: IOException => sys.error("Failed to open file:  " + htmlFile)
      case e: Exception => sys.error("ERROR: Cannot open file.")
    }
  }

  def hasSyntaxErrors(): Unit = {
    if (Parser.getError)
      System.exit(-1)
  }

  def checkInput(inputFile: Array[String]): Unit = {

    fileName = inputFile(0).filter(!".gtx".contains(_))
    if (inputFile.length != 1) {
      println("ERROR: Invalid input length.")
      System.exit(0)
    }
    else if (!inputFile(0).endsWith(".gtx")) {
      println("Error: Input file must have .gtx extension.")
      System.exit(0)
    }
  }
}
