package edu.towson.cosc455.project1

trait LexicalAnalyzer {
  def addChar() : Unit
  def getChar() : Unit
  def getNextToken() : Unit
  def lookup(candidateToken : String) : Boolean
}

