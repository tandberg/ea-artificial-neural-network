package Helpers
import scala.collection.mutable.ListBuffer
import scala.util.Random

object FlatLandHelpers {

	def generateRandomBitString = {
		val length = 12 * 4
		var bitString = Array[Int]() 
		val random = new Random()
		for (i <- 0 to length) {
			bitString = bitString :+ random.nextInt(2)	
		}
		bitString
	}

	def bitStringToWeights(array: Array[Int], bitstringpercision: Int):Array[Double]= {
		val split = array.grouped(bitstringpercision).toArray
		var weights:Array[Double] = Array()

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



	def mutateBitString(array: Array[Int], mutationRate:Double) = {
		val random = new Random

		def flip(num: Int): Int = {
			if (random.nextDouble() < mutationRate){
				if (num == 0) return 1
				else return 0
			}
			num
		}
		array map (flip _)
	}

}