//create some lists the old way

List('a', 'b', 'c')
List(1, 2, 3, 4)

val myList = List(10, 20, 30)

//Lists are immutable
//Create some lists the new/lazy way

val turtles = "I like turtles!".toList

//lets try to change turtles

"hello" :: turtles
123 :: turtles

//////////////////////////////////////////////////

List()
List[String]()

//Create list using cons operator elem :: list
123 :: Nil
"abc" :: "def" :: Nil

//head, tail, isEmpty
val wisc = "Wisconsin".toList
wisc.head
wisc.tail
wisc.isEmpty

//take
wisc take 4

//drop
wisc drop 4

//splitAt
wisc splitAt 4

///////////////////////////////////////////////////

val name = "carrie".toList
name.tail.tail.head
def removeThird(name : List[Any]) : List[Any] = {
  if(myList.isEmpty)
    Nil
  else{
    name.head :: name.tail.head :: name.tail.tail.tail
  }
}
removeThird(name)

/////////////////////////////////////////////////////
//anonymous function with filter
val nums = List(32, 50, 19, 78)
def moreThan42(nums : List[Int]) : List[Int] = {
  nums.filter(_ > 42)
}
moreThan42(nums)

////////////////////////////////////////////////////////
type inches = Int
type centimeters = Int

var var1 : inches = 10
var var2 : centimeters = var1

//////////////////////////////////////////////////////////
//anonymous function
val testList = List(1, 3, 5, 7)
def triplePlusOne(list : List[Int]) : List[Int] = {
  list.map(x => (x * 3) + 1)
}
triplePlusOne(testList)

////////////////////////////////////////////////////////
//foldLeft
def sum(list: List[Int]) : Int = {
  list.filter(_ > 4)
    .map(x => (3*x)+1)
    .foldLeft(0)(_+_)
}
sum(testList)

///////////////////////////////////////////////////////
def bor(aList : List[Boolean]) : Boolean = {
  aList.foldLeft(false)(_ || _)
}
def band(aList : List[Boolean]) : Boolean ={
  aList.foldLeft(true)(_ && _)
}
def evens(aList : List[Int]) : List[Int] = {
  aList.filter(_ % 2 == 0)
}
evens(testList)
val boolList = List(true, true, false, true)
def convert(aList : List[Boolean]) : List[Int] = {
  aList.map{
    case false => 0
    case true => 1
  }
}
convert(boolList)
