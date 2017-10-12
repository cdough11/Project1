package edu.towson.cis.cosc455.cdough11.project1

trait LexicalAnalyzer {
  def addChar() : Unit
  def getChar() : Char
  def getNextToken() : Unit
  def lookup() : Boolean
}
