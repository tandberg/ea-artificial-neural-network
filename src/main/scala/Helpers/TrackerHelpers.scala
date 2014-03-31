package Helpers
import scala.util.Random


object TrackerHelpers {

	val bitstringpercision = 8

	def generateRandomBitString = {

		val length = 34 * bitstringpercision
		var bitString = Array[Int]() 
		val random = new Random()
		for (i <- 0 to length-1) {
			bitString = bitString :+ random.nextInt(2)	
		}
		bitString
	}

	def bitToBase10(array: Array[Int]) = {
		var x = 0.0

		for(i <- 0 to array.length - 1) {
			if(array(i) == 1) {
				x += math.pow(2, array.length - i - 1)
			}
		}

		x
	}

	def convertOne(array: Array[Int], max: Int, min: Int) = {
		var x = bitToBase10(array)
		var maxArrayBase10 = math.pow(2, array.length) - 1

		var percent = x / maxArrayBase10
		(math.abs(max - min) * percent) + min
	}

	def bitStringConverter(array: Array[Int], bitstringprecision: Int) = {
		val splits = array.grouped(bitstringprecision).toArray
		println(splits.length)

		val w:Array[Double] = new Array(22)
		for(i <- 0 until 22) {
			w(i) = convertOne(splits(i), 5, -5)
		}

		val b:Array[Double] = new Array(4)
		for(i <- 0 until 4) {
			println(i)
			b(i) = convertOne(splits(i+22), 0, -10)
		}

		val g:Array[Double] = new Array(4)
		for(i <- 0 until 4) {
			g(i) = convertOne(splits(i+22+4), 5, 1)
		}

		val t:Array[Double] = new Array(4)
		for(i <- 0 until 4) {
			t(i) = convertOne(splits(i+22+4+4), 2, 1)
		}

		Map("weights" -> w.toList, "biases" -> b.toList, "gains" -> g.toList, "timeconstants" -> t.toList)
	}

	def cross(parent1Array: Array[Int], parent2Array: Array[Int], crossOverRate:Double): Array[Int] = {
		val random = new Random()
		var childArray:ArrayBuffer[Int] = ArrayBuffer()
		if (random.nextDouble() >= crossOverRate){
			for (i <- 0 to parent1Array.length - 1) {
				if (random.nextBoolean()){
					childArray += parent1Array(i)
				}
				else {
					childArray += parent2Array(i)
				}
			}
			return childArray.toArray
		}
		else {
			return parent1Array
		}
	}

}