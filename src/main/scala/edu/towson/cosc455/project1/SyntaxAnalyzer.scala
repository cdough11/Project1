package edu.towson.cosc455.project1

trait SyntaxAnalyzer {
  def title() : Boolean
  def body() : Unit
  def paragraph() : Unit
  def heading() : Unit
  def varDef() : Boolean
  def varUse() : Unit
  def bold() : Unit
  def listItem() : Unit
  def link() : Unit
  def image() : Unit
}
