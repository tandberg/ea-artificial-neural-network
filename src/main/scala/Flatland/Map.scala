import scala.io.Source
import scala.util.parsing.json._

class Map(file:String) {
	
	def this() = this("map1.json")
	
	var state: List[Any] = 
		JSON.parseFull(Source.fromFile("src/main/scala/flatland/maps/"+ file).getLines().foldLeft("") {
			(accString: String, line:String) => accString + line
		}) match {
			case Some(elements: List[Any]) => elements
			case None => List() 
			case Some(elements: Any) => List()
		}

	def json = new JSONArray(state)
	
	override def toString() = state.toString
}


object Map {
	def main(args: Array[String]){
		val map = new Map
		println(map)
	}

}
