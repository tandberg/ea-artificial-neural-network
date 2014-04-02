package ANN


class CTRNeuron(val label:String,var activationFunction:(Double) => Double, val gain:Double, val timeConstant:Double) {
	var sum:Double = 0.0
	var y: Double = 0.0
	def this(label:String) = this(label,(x) => { if (0.5 <= 1.0/(1 + math.exp(-x))) 1.0 else 0.0 },-5, -5) 
	def this(label:String, activationFunction:(Double) => Double) = this(label, activationFunction, -5, -5)
	def this(label:String, gain:Double, timeConstant:Double) = this(label, null, gain, timeConstant)
	def reset = sum = 0.0

	def ∑(number:Double) = sum += number 
	def dy = (1/timeConstant)*(-y + sum)

	def σ (x:Double):Double = 1 / (1 + math.exp(-x))
		
 	def output = {
		activationFunction(sum)
	}

	if (gain >= 0){
		activationFunction = (x) => {
			val returnValue:Double = σ(y*gain)
			y += dy
			returnValue
		}
	}
	
}

object CTRNeuron {

	def main(args: Array[String]) = {
	}
}
