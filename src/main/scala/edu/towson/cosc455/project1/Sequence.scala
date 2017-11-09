package edu.towson.cosc455.project1

import scala.util.matching.Regex

object Sequence {

  val beginSequence : Regex = """(\\[b,B][E,e][G,g][I,i][N,n])""".r

  val endSequence : Regex = """(\\[E,e][n,N][D,d])""".r

  val paragraphStartSequence : Regex = """(\\[P,p][A,a][R,r][B,b])""".r

  val paragraphEndSequence : Regex = """(\\[P,p][A,a][R,r][E,e])""".r

  val textSequence : Regex = """([A-Za-z0-9\,\.\"\:\?_\/]+)""".r

  val validTextSequence : Regex = """([A-Za-z0-9\,\.\"\:\?_\/])""".r

  val multiTextSequence : Regex = """[A-Za-z0-9\,\.\"\:\?_\/]+""".r

  val titleSequence : Regex = """(\\[T,t][I,i][T,t][L,l][E,e]\[[a-zA-Z_0-9\s\']+\])""".r

  val headingSequence : Regex = """(\#[a-zA-Z_0-9\s]+)""".r

  val variableDefSequence : Regex = """(\\[D,d][E,e][F,f]\[[a-zA-z_0-9]+\s?\=\s?[a-zA-z_0-9]+\])""".r

  val variableUseSequence : Regex = """(\\[U,u][S,s][E,e]\[[a-zA-z_0-9]+\])""".r

  val boldSequence : Regex = """(\*\*[a-zA-Z_0-9\s]+\*\*)""".r// ** text **

  val commentSequence : Regex = """(\[.+\])""".r

  val addressSequence : Regex = """(\(.+\))""".r

  val imageSequence : Regex = """(\!\s?\[.+\]\s?\(.+\)?\s?)""".r

  val newLineSequence : Regex = """(\\\\)""".r

  val linkSequence : Regex = """(\[.+\]\(.+\))""".r

  val listSequence : Regex = """(\+.?)""".r
}

