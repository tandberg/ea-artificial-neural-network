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



}