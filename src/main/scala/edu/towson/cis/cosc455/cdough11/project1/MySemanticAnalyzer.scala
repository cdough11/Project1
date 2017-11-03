package edu.towson.cis.cosc455.cdough11.project1

import java.util

import scala.collection.mutable
import java.io._
import java.awt.Desktop

class MySemanticAnalyzer {

  var html: String = ""
  var file = Compiler.fileName
  var parseTree = Compiler.Parser.parseTree
  var docStack = mutable.Stack[String]()
  var tempStack = mutable.Stack[String]()
  var varStack = mutable.Stack[String]()
  var boldTag: Int = 0
  var italTag: Int = 0
  var focus: String = ""

  val HTMLOPEN: String = "<HTML>"
  val HTMLCLOSE: String = "</HTML>"
  val HEADOPEN: String = "<HEAD>"
  val HEADCLOSE: String = "</HEAD>"
  val TITLEOPEN: String = "<TITLE>"
  val TITLECLOSE: String = "</TITLE>"
  val BODYOPEN: String = "<BODY>"
  val BODYCLOSE: String = "</BODY>"
  val PARAOPEN: String = "<P>"
  val PARACLOSE: String = "</P>"
  val H1OPEN: String = "<H1>"
  val H1CLOSE: String = "</H1>"
  val LIOPEN: String = "<LI>"
  val LICLOSE: String = "</LI>"
  val BOLDOPEN: String = "<B>"
  val BOLDCLOSE: String = "</B>"
  val NEWLINE: String = "<BR />"

  def convertToHTML(): Unit = {

    while (!parseTree.isEmpty) {
      focus = parseTree.pop()
      getHTML()
    }
    html = docStack.mkString
    writeToFile()
    openInBrowser("file.html")
  }

  def getHTML(): Unit = {

    focus.toUpperCase() match {
      case CONSTANTS.DOCB => docStack.push(HTMLOPEN)
      case CONSTANTS.PARB => docStack.push(PARAOPEN)
      case CONSTANTS.PARE => docStack.push(PARACLOSE)
      case CONSTANTS.BRACKETE => handleBracketTag()
      case CONSTANTS.PARENE => handleImageLink()
      case CONSTANTS.BOLD => generateBold()
      case CONSTANTS.NEWLINE => docStack.push(NEWLINE)
      case CONSTANTS.DOCE => docStack.push(HTMLCLOSE, BODYCLOSE)
      case "\n" => handleHeadList()
      case _ => docStack.push(focus)
    }
  }

  def writeToFile(): Unit = {
    val file = new File("file.html")
    val writer = new BufferedWriter(new FileWriter(file))

    writer.write(html)
    writer.close()
  }

  def openInBrowser(htmlString: String) = {
    val file: File = new File(htmlString.trim)
    println(file.getAbsolutePath)
    if (!file.exists())
      sys.error("File " + htmlString + " does not exist.")

    try {
      Desktop.getDesktop.browse(file.toURI)
    }
    catch {
      case ioe: IOException => sys.error("Failed to open file:  " + htmlString)
      case e: Exception => sys.error("System Error!")
    }
  }

  def handleBracketTag(): Unit = {
    while (parseTree.top.charAt(0) != '\\') {
      tempStack.push(parseTree.pop())
    }
    val root: String = parseTree.pop()
    root.toUpperCase() match {
      case CONSTANTS.TITLEB => generateTitle()
      case CONSTANTS.VARDEFB => generateVarDef()
      case CONSTANTS.VARUSEB => generateVarUse()
    }
  }

  def handleImageLink(): Unit = {
    while (!parseTree.top.equals(CONSTANTS.LINKB) && !parseTree.top.equals(CONSTANTS.IMAGEB)) {
      tempStack.push(parseTree.pop())
    }
    val root = parseTree.pop()
    if (root.equals(CONSTANTS.LINKB)) generateLink()
    else generateImage()
  }

  def handleHeadList(): Unit ={
    while(!parseTree.top.equals("#") && !parseTree.top.equals("+")){
      tempStack.push(parseTree.pop())
    }
    val root : String = parseTree.pop()
    if(root.equals("#")) generateHead()
    else generateList()
  }

  def generateBold(): Unit = {
    if (boldTag == 0) {
      docStack.push(BOLDCLOSE)
      boldTag = 1
    }
    else {
      docStack.push(BOLDOPEN)
      boldTag = 0
    }
  }

  def generateTitle(): Unit ={
    var title : String = ""
    while(!tempStack.isEmpty){
      title += tempStack.pop()
    }
    docStack.push(BODYOPEN, TITLECLOSE, title, TITLEOPEN)
  }

  def generateVarDef(): Unit ={}

  def generateVarUse(): Unit ={
    val varName = tempStack.pop()
    docStack.push(findVariable(varName))
    if(!tempStack.isEmpty)
      tempStack.pop()
  }

  def generateLink(): Unit ={
    var link : String = ""
    var text : String = ""
    var tag : String = ""
    tempStack = tempStack.reverse
    while(!tempStack.top.equals(CONSTANTS.PARENB)){
      link = tempStack.pop() + link
    }
    tempStack.pop()
    while(!tempStack.isEmpty){
      if(!tempStack.top.equals("]")) text = tempStack.pop() + text
      else tempStack.pop()
    }
    tag = "<a href=\"" + link + "\">"+ text +"</a>"
    docStack.push(tag)
  }

  def generateImage(): Unit ={
    var link : String = ""
    var text : String = ""
    var tag : String = ""
    tempStack = tempStack.reverse
    while(!tempStack.top.equals(CONSTANTS.PARENB)){
      link = tempStack.pop() + link
    }
    tempStack.pop()
    while(!tempStack.isEmpty){
      if(!tempStack.top.equals("]")) text = tempStack.pop() + text
      else tempStack.pop()
    }
    tag = "<img src=\"" + link + "\" alt=\"" + text +"\">"
    docStack.push(tag)
  }

  def generateHead(): Unit ={
    var heading : String = ""
    while(!tempStack.isEmpty) heading += tempStack.pop()
    docStack.push(H1CLOSE,heading,H1OPEN)
  }

  def generateList(): Unit ={
    docStack.push(LICLOSE)
    tempStack = tempStack.reverse

    while(!tempStack.isEmpty){
      val temp : String = tempStack.pop()
      if(temp.equals(CONSTANTS.BOLD))
        generateBold()
      else if(temp.equals(CONSTANTS.PARENE)){
        var link : String = ""
        var text : String = ""
        var tag : String = ""

        while(!tempStack.top.equals(CONSTANTS.PARENB)){
          link = tempStack.pop() + link
        }
        tempStack.pop()
        while(!tempStack.top.equals(CONSTANTS.LINKB)){
          if(!tempStack.top.equals("]"))
            text = tempStack.pop() + text
          else
            tempStack.pop()
        }
        tempStack.pop()
        tag = "<a href=\"" + link + "\">"+ text +"</a>"
        docStack.push(tag)
      }
      else if(temp.equals(CONSTANTS.BRACKETE))
        generateVarUse()
      else
        docStack.push(temp)
    }
    docStack.push(LIOPEN)
  }

  def findVariable(varName : String): String = {
    var varDef : String = ""
    var found = false
    var isInScope = true

    while(!found && !parseTree.isEmpty){
      if(parseTree.top.equalsIgnoreCase(CONSTANTS.PARE))
        isInScope = false
      if(parseTree.top.equalsIgnoreCase(CONSTANTS.PARB))
        isInScope = true
      if(parseTree.top.equalsIgnoreCase(CONSTANTS.VARDEFB)) {
        if (varStack.top.equals(varName) && isInScope){

          parseTree.push(varStack.pop())
          parseTree.push(varStack.pop())
          varDef = varStack.top
          found = true
        }
        varStack.push(parseTree.pop())
      }
      else varStack.push(parseTree.pop())
    }
    if(varDef.equals("")) SemanticError("Variable is not defined")

    while(!varStack.isEmpty){
      parseTree.push(varStack.pop())
    }
    varDef
  }

  def SemanticError(error : String): Unit ={
    println("Semantic error! " + error + " Exiting.")
    System.exit(1)
  }
}
