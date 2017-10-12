package edu.towson.cis.cosc455.cdough11.project1

import scala.io.Source

object Compiler {

  var currentToken : String = ""
  var fileContents : String = ""

  val Scanner = new MyLexicalAnalyzer
  val Parser = new MySyntaxAnalyzer
  val Converter = new MySemanticAnalyzer

  def main(args : Array[String]) = {
    checkFile(args)
    readFile(args(0))

    //Scanner.initializeLexems()
    Scanner.getNextToken()
    Parser.gittex()
  }

  def checkFile(args: Array[String]) = {
    if(args.length != 1) {
      println("ERROR : Argument length is insufficient")
      System.exit(1)
    } else if(!args(0).endsWith(".gtx")){
      println("ERROR: The file being used is not of the correct extension. You must use a file ending in .gtx")
      System.exit(1)
    }
  }

  def readFile(file: String): Unit ={
    val source = scala.io.Source.fromFile(file)
    fileContents = try source mkString finally source.close()
  }
}
