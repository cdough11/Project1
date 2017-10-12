package edu.towson.cis.cosc455.cdough11.project1

class MySyntaxAnalyzer extends SyntaxAnalyzer {

  override def gittex(): Unit = {
    if(Compiler.currentToken.equalsIgnoreCase(CONSTANTS.DOCB)){
      //add to parse tree
     // Compiler.Scanner.getNextToken();
    }
    else{
      println("Error")
      System.exit(1)
    }
  }

  override def title(): Unit = ???

  override def body(): Unit = ???

  override def paragraph(): Unit = ???

  override def heading(): Unit = ???

  override def variableDefine(): Unit = ???

  override def variableUse(): Unit = ???

  override def bold(): Unit = ???

  override def listItem(): Unit = ???

  override def link(): Unit = ???

  override def image(): Unit = ???

  override def newline(): Unit = ???
}
