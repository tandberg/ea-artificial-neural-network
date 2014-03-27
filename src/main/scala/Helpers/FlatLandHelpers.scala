package Helpers
import scala.collection.mutable.ListBuffer
import scala.util.Random

object FlatLandHelpers {

	val bitstringpercision = 8
	val numberOfWeights = 12

	def generateRandomBitString = {

		val length = numberOfWeights * (bitstringpercision+1)
		var bitString = Array[Int]() 
		val random = new Random()
		for (i <- 0 to length-1) {
			bitString = bitString :+ random.nextInt(2)	
		}
		bitString
	}

	def bitStringToWeights(array: Array[Int], bitstringpercision: Int):Array[Double]= {
		val split = array.grouped(bitstringpercision+1).toArray
		var weights:Array[Double] = Array()


		for(i <- 0 to split.length - 1) {
			var weight = 0.0
			for(j <- 1 to split(i).length - 1) {

				weight += (split(i)(j) << (split(i).length - j - 1))
			}
			weight /= math.pow(2, bitstringpercision)-1
			if(split(i)(0) == 1) {
				weight *= -1;
			}
			weights = weights :+ weight
		}
		weights
	}

	def cross(parent1Array: Array[Int], parent2Array: Array[Int], crossOverRate:Double): Array[Int] = {
		val random = new Random()
		var childArray:Array[Int] = Array()
		if (random.nextDouble() >= crossOverRate){
			for (i <- 0 to parent1Array.length - 1) {
				if (random.nextBoolean()){
					childArray(i) = parent1Array(i)
				}
				else {
					childArray(i) = parent2Array(i)
				}
			}
			return childArray
		}
		else {
			return parent1Array
		}
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
