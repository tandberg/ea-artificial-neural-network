package Helpers

object TrackerHelpers {

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
		//val splits = weightsArray.grouped(bitstringprecision)
	}

}