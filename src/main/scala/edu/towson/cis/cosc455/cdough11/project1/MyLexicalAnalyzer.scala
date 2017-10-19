package edu.towson.cis.cosc455.cdough11.project1

class MyLexicalAnalyzer extends LexicalAnalyzer{

  override def addChar(): Unit = ???

  override def getChar(): Char = ???

  override def getNextToken(): Unit = {
    val c = getChar()
  }

  override def lookup(): Boolean = ???

  def initializeLexems() : Unit = ???

  def hasNextToken(): Boolean = ???
}
