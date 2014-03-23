package Helpers
import scala.collection.mutable.ListBuffer
import scala.util.Random

object FlatLandHelpers {

	def generateRandomBitString = {
		val length = 12 * 4
		val bitString = new ListBuffer[Int]() 
		val random = new Random()
		for (i <- 0 to length) {
			bitString += random.nextInt(2)		
		}
		bitString.toList
	}

	def bitStringToWeights(array: Array[Int], bitstringpercision: Int):List[Double] = {
		val split = array.grouped(bitstringpercision).toList
		var weights:List[Double] = List()
			println(split.toList)

		for(i <- 0 to split.length - 1) {
			var weight = 0.0
			for(j <- 0 to split(i).length - 1) {

				weight += (split(i)(j) << (split(i).length - j - 1))
			}
			weight /= math.pow(2, bitstringpercision)
			weights = weights :+ weight
		}
		weights
	}


}