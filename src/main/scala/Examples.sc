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

val nums = List(32, 50, 19, 78)
def moreThan42(nums : List[Int]) : List[Int] = {
  nums.filter(_ > 42)
}
moreThan42(nums)